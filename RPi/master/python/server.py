import json, socket
import subprocess
import datetime
import time
import sys
from threading import Thread

# ----------------------------------------------------------------------------------------------
#                                       Global variables
# ----------------------------------------------------------------------------------------------

# List of all accepted device and operations for that raspberry pi 
# and a boolean to know if it's irrigating
accepted_devices = { '123' : 0 , '124' : 0 }
accepted_operations = [ 'sensor', 'webcam', 'irrigate', 'stop_irrigate', 'create_device', 'delete_device', 'list_devices' ]

# Irrigation paths
main_path = 'house/'
pub_path =  main_path + 'response/'

# List of current threads
threads = []

# List to contol the rules
rules = {}

'''
rules = {
    '123' : {
        '1' : { # AND
            'rule_conditions' : { # AND
                '1'     : [ '25', '1' ], # temperature higher than 
                '2'     : [ '15', '4' ]  # humidity lower than   
            },
            'rule_time_conditions' : { # OR
                '1'     : [ '10:30', '10:40', '1001001' , '100000000100', ['2019-12-28', '2019-12-29' ] ], # OR
                '2'     : [ '10:30', '10:40', '0110110' , '011111111011', ['2020-01-28', '2020-01-29' ] ], # OR
            }
        }
    }
}''' 

# ----------------------------------------------------------------------------------------------
#                                       Socket functions
# ----------------------------------------------------------------------------------------------

# Custom recv function
def _recv(conn):
    # read the length of the data, letter by letter until we reach EOL    
    length_str = conn.recv(1024)
    total = int(length_str)
    #print('Length: {0}'.format(total))

    # use a memoryview to receive the data chunk by chunk efficiently
    view = memoryview(bytearray(total))
    next_offset = 0
    while total - next_offset > 0:        
        recv_size = conn.recv_into(view[next_offset:], total - next_offset)
        next_offset += recv_size
    try:
        deserialized = json.loads(view.tobytes())
    except (TypeError, ValueError):
        raise Exception('Data received was not in JSON format')
    
    return deserialized

# Custom send function
def _send(conn, data):
    try:
        serialized = json.dumps(data) + '\n'
    except (TypeError, ValueError):
        raise Exception('You can only read JSON-serializable data')

    conn.send(serialized.encode('utf-8'))


# ----------------------------------------------------------------------------------------------
#                                       Thread functions
# ----------------------------------------------------------------------------------------------

# Function to control the rule conditions
def rule_thread_func():
    # TODO
    # Code to control the rule conditions and consequently irrigate or not
    return 0

# Function to control when to stop irrigation
def irrigate_thread_func(irrigation_time, device_id):
    
    # MQTT irrigate 
    params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/irrigate' + '/' + device_id, '-m', '']  
    rc = run_command(params)
    accepted_devices[device_id] = 1

    start_time = time.time()
    end_time = start_time + irrigation_time * 60

    print('{0} => start_time: {1} end_time: {2} device_id: {3} info: starting irrigation'.format(datetime.datetime.now(), start_time, end_time, device_id))

    while(end_time > time.time()):
        time.sleep(5)

    # MQTT stop_irrigate
    params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/stop_irrigate' + '/' + device_id, '-m', '']  
    rc = run_command(params)                      
    accepted_devices[device_id] = 0

    print('{0} => start_time: {1} end_time: {2} device_id: {3} info: stopping irrigation'.format(datetime.datetime.now(), start_time, end_time, device_id))
    return 0

# ----------------------------------------------------------------------------------------------
#                                       Run functions
# ----------------------------------------------------------------------------------------------

# Function to read the MQTT output
def run_command(params):
    process = subprocess.Popen(params, stdout = subprocess.PIPE)
    '''while True:
        output = process.stdout.readline()
        if output == '' and process.poll() is not None:
            break
            if output:
                print('output: ' + output.strip())        
    rc = process.poll()
    #print('Return code: ' + str(rc))'''
    return 0


# Function that runs the main server code
def run_server(HOST, PORT):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.bind((HOST, PORT))
        server.listen()

        # Subscribe to the topic for each device
        for dev in accepted_devices:
            #subprocess.Popen(['mosquitto_sub', '-h', 'localhost', '-t', 'house/response/' + dev])
            break
            
        # Create the thread to control rules
        thread = Thread(target = rule_thread_func, args = ())
        thread.start()
        threads.append(thread)

        # Execute the main server code
        while True:
            conn, addr = server.accept()
            with conn:
                print('{0} => Connected by: {1}'.format(datetime.datetime.now(), addr))                
                data = _recv(conn)

                # If accepted device ( {device_1}, {device_2}, ...)
                print('{0} => RCV: data: {1}'.format(datetime.datetime.now(), data))

                # If operation is valid
                if data['operation'] in accepted_operations:                    
                    operation = data['operation']
                    device_id = data['device_id']

                    # Irrigate operation                    
                    if operation == 'irrigate' and device_id in accepted_devices.keys():
                        irrigation_time = int(data['irrigation_time'])
                        thread = Thread(target = irrigate_thread_func, args = (irrigation_time, device_id))
                        thread.start()
                        threads.append(thread)

                    # Stop irrigate, webcam and sensor operation
                    elif (operation == 'stop_irrigate' or operation == 'webcam' or operation == 'sensor') and device_id in accepted_devices:
                        path = main_path + operation + '/' + device_id
                        params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/' + operation + '/' + device_id, '-m', '']
                        
                        rc = run_command(params)
                        #output = subprocess.Popen(params, stdout = subprocess.PIPE ).communicate()[0]
                        
                        print('{0} => SND: output: {1}'.format(datetime.datetime.now(), rc))
                        _send(conn, { 'response' : rc })

                    # Create device operation
                    elif operation == 'create_device':
                        accepted_devices[device_id] = 0
                        print('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices.keys()))
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # Delete device operation
                    elif operation == 'delete_device':
                        accepted_devices.pop(device_id)
                        print('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices.keys()))                        
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # List devices operation
                    elif operation == 'list_devices':
                        print('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices))
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # Create rule operation
                    elif operation == 'create_rule':
                        rule_type = data['rule_type']    
                        rule_id = data['rule_id']

                        if rule_type == '0': # rule_condition
                            rule_condition_id = data['rule_condition_id']
                            value = data['value']
                            rule_condition_type = data['rule_condition_type']

                            if rule_condition_id != '' and value != '' and rule_condition_type != '':       

                                # Si existeix el device_id                           
                                if rules[device_id] in rules.keys():
                                    
                                    # Si existeix el device_id i el rule_id
                                    if rules[device_id][rule_id] in rules[device_id].keys(): 

                                        # Si existeix el device_id, el rule_id i el rule_condition_id
                                        if rules[device_id][rule_id]['rule_conditions'][rule_condition_id] in rules[device_id][rule_id]['rule_conditions'].keys():
                                            # Actualizem la informació
                                            rules[device_id][rule_id]['rule_conditions'][rule_condition_id] = [ value, rule_condition_type ]

                                        # Si existeix el device_id, el rule_id pero no existeix el rule_condition_id
                                        else:
                                            rules[device_id][rule_id]['rule_conditions'][rule_condition_id] = [ value, rule_condition_type ]

                                    # Si existeix el device_id pero no existeix el rule_id
                                    else:
                                        rules[device_id][rule_id] = {
                                            'rule_conditions' : {
                                                rule_condition_id : [ value, rule_condition_type ] 
                                            }
                                        } 

                                # Si no existeix el device_id el creem
                                else:
                                    rules[device_id] = { 
                                        rule_id : {
                                            'rule_conditions' : { 
                                                rule_condition_id : [ value, rule_condition_type ] 
                                            }
                                        }
                                    }                            

                            else:
                                print('{0} => data: {1} error: Invalid rule condition params'.format(datetime.datetime.now(), data))

                        elif rule_type == '1': # rule_time_condition
                            rule_time_condition_id = data['rule_time_condition_id']
                            start_time = data['start_time']
                            end_time = data['end_time']
                            weeks = data['week']
                            months = data['months']
                            specific_dates = data['specific_dates']                        

                            # Si existeix el device_id                           
                            if rules[device_id] in rules.keys():
                                
                                # Si existeix el device_id i el rule_id
                                if rules[device_id][rule_id] in rules[device_id].keys(): 

                                    # Si existeix el device_id, el rule_id i el rule_time_condition_id
                                    if rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] in rules[device_id][rule_id]['rule_time_conditions'].keys():
                                        # Actualizem la informació
                                        rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] = [ start_time, end_time, weeks, months, specific_dates] 

                                    # Si existeix el device_id, el rule_id pero no existeix el rule_time_condition_id
                                    else:
                                        rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] = [ start_time, end_time, weeks, months, specific_dates] 

                                # Si existeix el device_id pero no existeix el rule_id
                                else:
                                    rules[device_id][rule_id] = {
                                        'rule_time_conditions' : {
                                            rule_time_condition_id : [ start_time, end_time, weeks, months, specific_dates] 
                                        }
                                    } 

                            # Si no existeix el device_id el creem
                            else:
                                rules[device_id] = { 
                                    rule_id : {
                                        'rule_time_conditions' : { 
                                            rule_time_condition_id : [ start_time, end_time, weeks, months, specific_dates]  
                                        }
                                    }
                                }                            
                  
                        else:
                            print('{0} => rule_type: {1} error: Invalid rule_id'.format(datetime.datetime.now(), rule_type))
                            continue

                    # Modify rule operation
                    elif operation == 'modify_rule':
                        # TODO
                        # Podem eliminar l'anterior registre i crearlo de nou
                        continue                

                    # Delete rule operation
                    elif operation == 'delete_rule':
                        # TODO
                        # Eliminar la rule
                        continue

                        #conn.send({ 'response' : 'Aqui aniria la resposta' })
                else:
                    continue
                    #conn.send({ 'response' : 'invalid device ' +  data })

        # Wait for all threads to complete
        for t in threads:
            t.join()


# Main function
def main():
    if len(sys.argv) == 3:
        HOST = sys.argv[1]
        PORT = int(sys.argv[2])
        run_server(HOST, PORT)

    else:
        print('Usage: python server.py <host_ip> <port>')
        exit(0)


# Main program call
if __name__ == '__main__':
    main()