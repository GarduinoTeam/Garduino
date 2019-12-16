#!/usr/bin/env python2
# -*- coding: utf-8 -*-
#file : server.py

"""
Created on Mon Dec  9 19:54:22 2019

@author: Roger Truchero Visa
"""

from threading import Thread
from jsonsocket import Server
import subprocess

###############################################################################

host = "192.168.43.181"
port = 12348
    
# List of all accepted device and operations for that raspberry pi
accepted_devices = [ 123, 124 ]
accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ]



"""
({ "device_id" : 123, "operation" : operation })

* Sensor
* Webcam 
* Irrigate
* Stop irrigate


"""

# Irrigation paths
main_path = "house/"
pub_path =  main_path + "response/"

# List of current threads
threads = []

"""
sub_sensor = "house/sensor/{device_id}"
sub_webcam = "house/webcam/{device_id}"
sub_irrigate = "house/irrigate/{device_id}"
sub_stop_irrigate = "house/stop_irrigate/{device_id}"
"""

###############################################################################

# Function called by each device
def threaded_func(server, dev):
    subprocess.Popen(["mosquitto_sub", "-h", "localhost", "-t", "house/response/" + dev])
    
    while True:
        server.accept()
        data = server.recv()
        # If accepted device ( {device_1}, {device_2}, ...)
        print(":threaded_func device_id: " + str(data["device_id"]))
        if data["device_id"] in accepted_devices:
            device_id = str(data["device_id"])
            # If accepted operation (sensor, webcam, irrigate, stop_irrigate)
            if data["operation"] in accepted_operations:
                operation = data["operation"]
                path = main_path + operation + "/" + device_id
                subprocess.call(["mosquitto_pub", "-h", "localhost", "-t", "house/" + operation + "/" + device_id, "-m", ""])
                server.send({ "response" : path })  
                    
            else:
                server.send({ "response" : "invalid operation " +  data })
        else:
            server.send({ "response" : "invalid device " +  data })

# Main function
def main():
    server = Server(host, port)
    
    # For each accepted device create a thread to execute the "server" function 
    for dev in accepted_devices:   
        thread = Thread(target = threaded_func, args = (server, str(dev)))
        thread.start()
        threads.append(thread)
        print(":main info: thread finished...exiting")
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
        
    print(":main info: exiting Main Thread")
    server.close()
    
# Main program call
if __name__ == "__main__":
    main()