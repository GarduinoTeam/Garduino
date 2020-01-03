import json, socket
import subprocess
import datetime
from threading import Thread
##from jsonsocket import Server

###############################################################################

HOST = '192.168.43.70'
PORT = 12348

# List of all accepted device and operations for that raspberry pi
accepted_devices = [ 123, 124 ]
accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ]

# Irrigation paths
main_path = "house/"
pub_path =  main_path + "response/"

# List of current threads
threads = []

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

# Function called by each device
def threaded_func(server):
    #subprocess.Popen(["mosquitto_sub", "-h", "localhost", "-t", "house/response/" + dev])
    
    while True:
        conn, addr = server.accept()
        with conn:
            print('Connected by', addr)

            data = _recv(conn)
            # If accepted device ( {device_1}, {device_2}, ...)
            print("{0} => RCV: data: {1}".format(datetime.datetime.now(), data))
            if data["device_id"] in accepted_devices:
                device_id = str(data["device_id"])
                # If accepted operation (sensor, webcam, irrigate, stop_irrigate)
                if data["operation"] in accepted_operations:
                    operation = data["operation"]
                    path = main_path + operation + "/" + device_id
                    """params = ["mosquitto_pub", "-h", "localhost", "-t", "house/" + operation + "/" + device_id, "-m", ""]
                    rc = run_command(params)
                    #output = subprocess.Popen(params, stdout = subprocess.PIPE ).communicate()[0]
                    print("{0} => SND: output: {1}".format(datetime.datetime.now(), rc))
                    conn.send({ "response" : rc })
                    """
                    print("{0} => SND: output: {1}".format(datetime.datetime.now(), "asdf"))
                    #conn.send({ "response" : "Aqui aniria la resposta" })
                else:
                    continue
                    #conn.send({ "response" : "invalid operation " +  data })
            else:
                continue
                #conn.send({ "response" : "invalid device " +  data })

#
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

#
def run_command(params):
    process = subprocess.Popen(params, stdout=subprocess.PIPE)
    while True:
        output = process.stdout.readline()
        if output == '' and process.poll() is not None:
            break
        if output:
            print("output: " + output.strip())
    rc = process.poll()
    print("Return code: " + str(rc))
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
                print('Connected by', addr)
                data = _recv(conn)
                # If accepted device ( {device_1}, {device_2}, ...)
                print("{0} => RCV: data: {1}".format(datetime.datetime.now(), data))
                if data["device_id"] in accepted_devices:
                    device_id = str(data["device_id"])
                    # If accepted operation (sensor, webcam, irrigate, stop_irrigate)
                    if data["operation"] in accepted_operations:
                        operation = data["operation"]
                        path = main_path + operation + "/" + device_id
                        """params = ["mosquitto_pub", "-h", "localhost", "-t", "house/" + operation + "/" + device_id, "-m", ""]
                        rc = run_command(params)
                        #output = subprocess.Popen(params, stdout = subprocess.PIPE ).communicate()[0]
                        print("{0} => SND: output: {1}".format(datetime.datetime.now(), rc))
                        conn.send({ "response" : rc })
                        """
                        print("{0} => SND: output: {1}".format(datetime.datetime.now(), "asdf"))
                        #conn.send({ "response" : "Aqui aniria la resposta" })
                    else:
                        continue
                        #conn.send({ "response" : "invalid operation " +  data })
                else:
                    continue
                    #conn.send({ "response" : "invalid device " +  data })
"""
        # For each accepted device create a thread to execute the "server" function 
        for dev in accepted_devices:   
            thread = Thread(target = threaded_func, args = (s, str(dev)))
            thread.start()
            threads.append(thread)
            print(":main info: thread finished...exiting")

        # Wait for all threads to complete
        for t in threads:
            t.join()
        
        print(":main info: exiting Main Thread")    
"""

# Main program call
if __name__ == "__main__":
    main()