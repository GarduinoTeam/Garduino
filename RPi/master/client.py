#!/usr/bin/env python2
# -*- coding: utf-8 -*-
#file : client.py

"""
Created on Mon Dec  9 21:04:12 2019

@author: Roger Truchero Visa
"""

from jsonsocket import Client
import time

host = "192.168.1.131"
port = 12346

accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ] # For testing

while True:
    for operation in accepted_operations:    
        client = Client()
        client.connect(host, port).send({ "device_id" : 123, "operation" : operation })
        response = client.recv()
        print response
        client.close()
        time.sleep(1)