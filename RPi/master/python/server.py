import json, socket
import subprocess
import datetime
import time
import sys
import base64
import random
from threading import Thread

# ----------------------------------------------------------------------------------------------
#                                       Global variables
# ----------------------------------------------------------------------------------------------

# List of all accepted device and operations for that raspberry pi 
# and a boolean to know if it's irrigating
accepted_devices = { '13' : 0, '123' : 0 , '124' : 0 }
accepted_operations = [ 'sensor', 'webcam', 'irrigate', 'stop_irrigate', 'create_device', 'delete_device', 'list_devices', 'create_rule', 'delete_rule', 'delete_rule_condition', 'delete_rule_time_condition' ]

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
#                                       Auxiliar functions
# ----------------------------------------------------------------------------------------------

# Function to do debug
def debug(line):
    with open('logs/server.log', 'a+') as file:  # Use file to refer to the file object
        file.write(line + '\n')

# Select random image path
def get_image_name():
    image_path = '../../../PlagueDetection/test_images/'
    images = [ 'apple_black_rot.jpg', 'apple_healthy.jpg', 'corn_cercospora_leaf_spot.jpg', 'blueberry_healthy.jpg' ]
    #debug('{0} => image_path: {1}'.format(datetime.datetime.now(), image_path + random.choice(images)))

    return image_path + random.choice(images)


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
    rc = run_command(params, 'irrigate')
    accepted_devices[device_id] = 1

    start_time = time.time()
    end_time = start_time + irrigation_time * 60

    debug('{0} => start_time: {1} end_time: {2} device_id: {3} info: starting irrigation'.format(datetime.datetime.now(), start_time, end_time, device_id))

    while(end_time > time.time()):
        time.sleep(5)

    # MQTT stop_irrigate
    params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/stop_irrigate' + '/' + device_id, '-m', '']  
    rc = run_command(params, 'stop_irrigate')                      
    accepted_devices[device_id] = 0

    debug('{0} => start_time: {1} end_time: {2} device_id: {3} info: stopping irrigation'.format(datetime.datetime.now(), start_time, end_time, device_id))
    return 0


# ----------------------------------------------------------------------------------------------
#                                       Run functions
# ----------------------------------------------------------------------------------------------

# Function to read the MQTT output
def run_command(params, operation):
    #process = subprocess.Popen(params, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    """process = processes[device_id]
    print(process)
    data = process.stdout.readlines()
    print("data:", data.rstrip())"""
    
    # Wait for sensor data
    if operation == 'sensor':
        file = open('output.txt', 'r+')
        while True:
            for line in file:
                #print('line: {0}'.format(line))
                if len(line.split(',')) == 3:                    
                    file.truncate(0)
                    file.close()
                    return line[:-1].strip('\u0000')
    return ''

# Function that runs the main server code
def run_server(HOST, PORT):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.bind((HOST, PORT))
        server.listen()

        # Subscribe to the topic for each device
        for dev in accepted_devices:
            break
            #process = subprocess.Popen(['mosquitto_sub', '-h', 'localhost', '-t', 'house/response/' + dev])        
            #processes[dev] = process
            
        # Create the thread to control rules
        thread = Thread(target = rule_thread_func, args = ())
        thread.start()
        threads.append(thread)

        # Execute the main server code
        while True:
            conn, addr = server.accept()
            with conn:
                debug('{0} => Connected by: {1}'.format(datetime.datetime.now(), addr))                
                data = _recv(conn)

                # If accepted device ( {device_1}, {device_2}, ...)
                debug('{0} => RCV: data: {1}'.format(datetime.datetime.now(), data))

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

                    # Webcam operation
                    elif operation == 'webcam':                        
                        with open(get_image_name(), "rb") as image_file:
                            image_base64 = base64.b64encode(image_file.read())
                        #debug('{0} => image_base64: {1}'.format(datetime.datetime.now(), image_base64))
                        path = main_path + operation + '/' + device_id
                        params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/' + operation + '/' + device_id, '-m', '']
                        response = run_command(params, operation)
                        debug('{0} => operation: {1} SND: image_base64: {2}'.format(datetime.datetime.now(), operation, image_base64))
                        _send(conn, { 'image_base64' : len(image_base64.decode("utf-8"))  })

                    # Stop irrigate and sensor operation
                    elif (operation == 'stop_irrigate' or operation == 'sensor') and device_id in accepted_devices:
                        path = main_path + operation + '/' + device_id
                        params = ['mosquitto_pub', '-h', 'localhost', '-t', 'house/' + operation + '/' + device_id, '-m', '']                            
                        response = run_command(params, operation)                                                
                        debug('{0} => operation: {1} SND: response: {2}'.format(datetime.datetime.now(), operation, response))
                        _send(conn, { 'response' : response })

                    # Create device operation
                    elif operation == 'create_device':
                        accepted_devices[device_id] = 0
                        debug('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices.keys()))
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # Delete device operation
                    elif operation == 'delete_device':
                        accepted_devices.pop(device_id)
                        debug('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices.keys()))                        
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # List devices operation
                    elif operation == 'list_devices':
                        debug('{0} => devices: {1}'.format(datetime.datetime.now(), accepted_devices))
                        _send(conn, { 'response' : ' '.join(map(str, accepted_devices.keys())) })

                    # Create rule operation , also acts as a modify rule_condition and rule_time_condition
                    elif operation == 'create_rule':
                        rule_type = data['rule_type']    
                        rule_id = data['rule_id']

                        if rule_type == '0': # rule_condition
                            rule_condition_id = data['rule_condition_id']
                            value = data['value']
                            rule_condition_type = data['rule_condition_type']

                            if rule_condition_id != '' and value != '' and rule_condition_type != '':       

                                # Si existeix el device_id                           
                                if device_id in rules.keys():
                                    
                                    # Si existeix el device_id i el rule_id
                                    if rule_id in rules[device_id].keys(): 

                                        # Si existeix el camp rule_conditions
                                        if 'rule_conditions' in rules[device_id][rule_id].keys():

                                            # Si existeix el device_id, el rule_id i el rule_condition_id
                                            if rule_condition_id in rules[device_id][rule_id]['rule_conditions'].keys():
                                                # Actualizem la informació
                                                rules[device_id][rule_id]['rule_conditions'][rule_condition_id] = [ value, rule_condition_type ]

                                            # Si existeix el device_id, el rule_id pero no existeix el rule_condition_id
                                            else:
                                                rules[device_id][rule_id]['rule_conditions'][rule_condition_id] = [ value, rule_condition_type ]

                                        # Si no existeix el creem per evitar petades
                                        else:
                                            rules[device_id][rule_id]['rule_conditions'] = { rule_condition_id : [value, rule_condition_type] }

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
                                debug('{0} => data: {1} error: Invalid rule condition params'.format(datetime.datetime.now(), data))      
                                   
                            debug('{0} => rules: {1}: '.format(datetime.datetime.now(), json.dumps(rules, sort_keys = True, indent = 4)))      
                                                  

                        elif rule_type == '1': # rule_time_condition
                            rule_time_condition_id = data['rule_time_condition_id']
                            start_time = data['start_time']
                            end_time = data['end_time']
                            weeks = data['weeks']
                            months = data['months']
                            specific_dates = data['specific_dates']                        

                            # Si existeix el device_id                           
                            if device_id in rules.keys():
                                
                                # Si existeix el device_id i el rule_id
                                if rule_id in rules[device_id].keys(): 

                                    # Si existeix el camp rule_time_conditions
                                    if 'rule_time_conditions' in rules[device_id][rule_id].keys():

                                        # Si existeix el device_id, el rule_id i el rule_time_condition_id
                                        if rule_time_condition_id in rules[device_id][rule_id]['rule_time_conditions'].keys():
                                            # Actualizem la informació
                                            rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] = [ start_time, end_time, weeks, months, specific_dates] 

                                        # Si existeix el device_id, el rule_id pero no existeix el rule_time_condition_id
                                        else:
                                            rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] = [ start_time, end_time, weeks, months, specific_dates] 

                                    # Si no existeix el creem per evitar petades
                                    else:
                                        rules[device_id][rule_id]['rule_time_conditions'] = { rule_time_condition_id : [ start_time, end_time, weeks, months, specific_dates] }

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

                            # Printing the rule dictionary             
                            debug('{0} => rules: {1}: '.format(datetime.datetime.now(), json.dumps(rules, sort_keys = True, indent = 4)))      

                        else:
                            debug('{0} => rule_type: {1} error: Invalid rule_id'.format(datetime.datetime.now(), rule_type))
                            continue

                    # Modify rule operation
                    elif operation == 'modify_rule':
                        device_id = data['device_id']
                        rule_type = data['rule_type']    
                        rule_id = data['rule_id']

                        if rule_type == '0' and device_id in rules.keys() and rule_id in rules[device_id].keys() and 'rule_conditions' in rules[device_id][rule_id].keys() and data['rule_condition_id'] in rules[device_id][rule_id]['rule_conditions'].keys(): # rule_condition
                            rule_condition_id = data['rule_condition_id']
                            value = data['value']
                            rule_condition_type = data['rule_condition_type']

                            rules[device_id][rule_id]['rule_conditions'][rule_condition_id] = [ value, rule_condition_type ]

                        elif rule_type == '1' and device_id in rules.keys() and rule_id in rules[device_id].keys() and 'rule_time_conditions' in rules[device_id][rule_id].keys() and data['rule_time_condition_id'] in rules[device_id][rule_id]['rule_time_conditions'].keys():
                            rule_time_condition_id = data['rule_time_condition_id']
                            start_time = data['start_time']
                            end_time = data['end_time']
                            weeks = data['weeks']
                            months = data['months']
                            specific_dates = data['specific_dates']  
                            rules[device_id][rule_id]['rule_time_conditions'][rule_time_condition_id] = [ start_time, end_time, weeks, months, specific_dates] 

                        else:
                            debug('{0} => error: Invalid modify rule petition'.format(datetime.datetime.now()))

                    # Delete rule operation
                    elif operation == 'delete_rule':
                        device_id = data['device_id']
                        rule_id = data['device_id']

                        if device_id in rules.keys() and rule_id in rules[device_id].keys():
                            rules[device_id].pop(rule_id)
                            debug('{0} =>  info: Rule deleted succesfully rules: {1}: '.format(datetime.datetime.now(), json.dumps(rules, sort_keys = True, indent = 4)))
                        else:
                            debug('{0} => error: Non existing device_id nor rule_id'.format(datetime.datetime.now()))

                    # Delete rule_condition operation
                    elif operation == 'delete_rule_condition':
                        device_id = data['device_id']
                        rule_id = data['rule_id']
                        rule_condition_id = data['rule_condition_id']

                        if device_id in rules.keys() and rule_id in rules[device_id].keys() and 'rule_conditions' in rules[device_id][rule_id].keys() and rule_condition_id in rules[device_id][rule_id]['rule_conditions'].keys():
                            rules[device_id][rule_id]['rule_conditions'].pop(rule_condition_id)
                            debug('{0} =>  info: Rule Condition deleted succesfully rules: {1}: '.format(datetime.datetime.now(), json.dumps(rules, sort_keys = True, indent = 4)))
                        else:
                            debug('{0} device_id: {1} rule_id: {2} rule_condition_id: {3} => error: Non existing id'.format(datetime.datetime.now(), device_id, rule_id, rule_condition_id))    

                        
                    # Delete rule_time_condition operation
                    elif operation == 'delete_rule_time_condition':
                        device_id = data['device_id']
                        rule_id = data['device_id']            
                        rule_time_condition_id = data['rule_time_condition_id']                        

                        if device_id in rules.keys() and rule_id in rules[device_id].keys() and 'rule_time_conditions' in rules[device_id][rule_id].keys() and rule_time_condition_id in rules[device_id][rule_id]['rule_time_conditions'].keys():
                            rules[device_id][rule_id]['rule_time_conditions'].pop(rule_time_condition_id)
                            debug('{0} =>  info: Rule Time Condition deleted succesfully rules: {1}: '.format(datetime.datetime.now(), json.dumps(rules, sort_keys = True, indent = 4)))
                        else:
                            debug('{0} device_id: {1} rule_id: {2} rule_time_condition_id: {3} => error: Non existing id'.format(datetime.datetime.now(), device_id, rule_id, rule_time_condition_id))    

                else:
                    continue
                    conn.send({ 'response' : 'invalid operation' })

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
        debug('Usage: python server.py <host_ip> <port>')
        exit(0)


# Main program call
if __name__ == '__main__':
    main()