#!/usr/bin/env python2
# -*- coding: utf-8 -*-
#file : server.py

"""
Created on Mon Dec  9 19:54:22 2019

@author: Roger Truchero Visa
"""

from jsonsocket import Server
import subprocess

host = "192.168.1.131"
port = 12346

server = Server(host, port)

# List of all accepted device for that raspberry pi
accepted_devices = [ 123 ]
accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ]

# Irrigation paths
main_path = "house/"
pub_path =  main_path + "response/"

"""
sub_sensor = "house/sensor/{device_id}"
sub_webcam = "house/webcam/{device_id}"
sub_irrigate = "house/irrigate/{device_id}"
sub_stop_irrigate = "house/stop_irrigate/{device_id}"
"""

subprocess.Popen(["mosquitto_sub", "-h", "localhost", "-t", "house/response/" + str(accepted_devices[0])])

while True:
    server.accept()
    data = server.recv()
    
    # If accepted device ( {device_1}, {device_2}, ...)
    print "device_id: " + str(data["device_id"])
    if data["device_id"] in accepted_devices:
        device_id = data["device_id"]
        # If accepted operation (sensor, webcam, irrigate, stop_irrigate)
        if data["operation"] in accepted_operations: 
            operation = data["operation"]
            path = main_path + operation + "/" + str(device_id)
            subprocess.call(["mosquitto_pub", "-h", "localhost", "-t", "house/" + operation + "/" + str(device_id), "-m", ""])
            server.send({ "response" : path })        
                
        else:
            server.send({ "response" : "invalid operation " +  data })
    else:
        server.send({ "response" : "invalid device " +  data })

server.close()