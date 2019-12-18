#!/usr/bin/env python2
# -*- coding: utf-8 -*-
#file : client.py

"""
Created on Mon Dec  9 21:04:12 2019

@author: Roger Truchero Visa
"""

from jsonsocket import Client
import time
import datetime

host = "192.168.43.181"
port = 12345

#accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ] # For testing

while True:
    data=raw_input("Introduce operation and your device_id: ")
    data = data.split(" ")
    if(len(data) == 2):        
        operation = data[0]
        device_id = int(data[1])
        client = Client()
        to_send = { "device_id" : device_id, "operation" : operation, "rule_id" : ""}
        print "{0} => SND: device_id: {1} operation: {2}".format(datetime.datetime.now(), device_id, operation)
        client.connect(host, port).send(to_send)
        response = client.recv()
        print "{0} => RCV: {1}".format(datetime.datetime.now(), response)
        client.close()
    else:
        print("Introduced invalid parameters. You should do it like this: sensor 123")

# python client.py {device_id} {operation} {rule_id} {id} {args}



# rule_conditions: python client.py {device_id} {operation} {id} {rule_id} {} 
"""rules = {
    "123" : {
        "1" : { # AND
            "rule_conditions" : { # AND
                "1"     : [ "25", "1" ], # temperature higher than 
                "2"     : [ "15", "4" ]  # humidity lower than   
            },
            "rule_time_conditions" : { # OR
                "1"     : [ "10:30", "10:40", "1001001" , "100000000100", ["2019-12-28", "2019-12-29" ] ], # OR
                "2"     : [ "10:30", "10:40", "0110110" , "011111111011", ["2020-01-28", "2020-01-29" ] ], # OR
            }
        }
    }
}"""