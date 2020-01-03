import json, socket
import subprocess
import datetime
import time
from threading import Thread
##from jsonsocket import Server

###############################################################################

"""
({ "device_id" : 123, "operation" : operation })

* Sensor
* Webcam 
* Irrigate
* Stop irrigate

sub_sensor = "house/sensor/{device_id}"
sub_webcam = "house/webcam/{device_id}"
sub_irrigate = "house/irrigate/{device_id}"
sub_stop_irrigate = "house/stop_irrigate/{device_id}"
"""

###############################################################################

HOST = '192.168.43.70'
PORT = 12345

# List of all accepted device and operations for that raspberry pi
accepted_devices = [ "123", "124" ]
accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ]

# Irrigation paths
main_path = "house/"
pub_path =  main_path + "response/"

# List of current threads
irrigate_threads = []

###############################################################################

# Function to control when to stop irrigation
def threaded_func(irrigation_time, device_id):
    
    # MQTT irrigate 
    params = ["mosquitto_pub", "-h", "localhost", "-t", "house/irrigate" + "/" + device_id, "-m", ""]  
    #rc = run_command(params)

    start_time = time.time()
    end_time = start_time + irrigation_time * 60

    print("{0} => start_time: {1} end_time: {2} device_id: {3} info: starting irrigation".format(datetime.datetime.now(), start_time, end_time, device_id))

    while(end_time > time.time()):
        time.sleep(5)
    
    # MQTT stop_irrigate
    params = ["mosquitto_pub", "-h", "localhost", "-t", "house/stop_irrigate" + "/" + device_id, "-m", ""]  
    #rc = run_command(params)                      

    print("{0} => start_time: {1} end_time: {2} device_id: {3} info: stopping irrigation".format(datetime.datetime.now(), start_time, end_time, device_id))
    return 0

# Custom recv function
def _recv(conn):
    # read the length of the data, letter by letter until we reach EOL    
    length_str = conn.recv(1024)
    total = int(length_str)
    print("Length: {0}".format(total))

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

# Function to read the MQTT output
def run_command(params):
    process = subprocess.Popen(params, stdout = subprocess.PIPE)
    while True:
        output = process.stdout.readline()
        if output == '' and process.poll() is not None:
            break
        """if output:
            print("output: " + output.strip())
        """
    rc = process.poll()
    #print("Return code: " + str(rc))

    return rc

# Main function
def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.bind((HOST, PORT))
        server.listen()

        for dev in accepted_devices:
            break
            #subprocess.Popen(["mosquitto_sub", "-h", "localhost", "-t", "house/response/" + dev])
            
        while True:
            conn, addr = server.accept()
            with conn:
                print("{0} => Connected by: {1}".format(datetime.datetime.now(), addr))                
                data = _recv(conn)
                # If accepted device ( {device_1}, {device_2}, ...)
                print("{0} => RCV: data: {1}".format(datetime.datetime.now(), data))

                if data["device_id"] in accepted_devices:
                    device_id = str(data["device_id"])
                    # If accepted operation (sensor, webcam, irrigate, stop_irrigate)
                    if data["operation"] in accepted_operations:
                        operation = data["operation"]                    
                        path = main_path + operation + "/" + device_id

                        if operation == "irrigate":
                            irrigation_time = int(data["irrigation_time"])
                            thread = Thread(target = threaded_func, args = (irrigation_time, dev))
                            thread.start()
                            irrigate_threads.append(thread)

                        path = main_path + operation + "/" + device_id
                        params = ["mosquitto_pub", "-h", "localhost", "-t", "house/" + operation + "/" + device_id, "-m", ""]
                        #rc = run_command(params)
                        #output = subprocess.Popen(params, stdout = subprocess.PIPE ).communicate()[0]
                        #print("{0} => SND: output: {1}".format(datetime.datetime.now(), rc))
                        #conn.send({ "response" : rc })                        
                        print("{0} => SND: output: {1}".format(datetime.datetime.now(), "asdf"))
                        #conn.send({ "response" : "Aqui aniria la resposta" })
                    else:
                        continue
                        #conn.send({ "response" : "invalid operation " +  data })
                else:
                    continue
                    #conn.send({ "response" : "invalid device " +  data })

        # Wait for all threads to complete
        for t in irrigate_threads:
            t.join()

# Main program call
if __name__ == "__main__":
    main()