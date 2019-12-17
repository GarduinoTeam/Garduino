#!/usr/bin/env python2
# -*- coding: utf-8 -*-
#file : client.py

"""
Created on Mon Dec  9 21:04:12 2019

@author: Roger Truchero Visa
"""

from jsonsocket import Client
import time

host = "192.168.43.181"
port = 12348

accepted_operations = [ "sensor", "webcam", "irrigate", "stop_irrigate" ] # For testing

while True:
    for operation in accepted_operations:    
        client = Client()
        
        client.connect(host, port).send({ "device_id" : 123, "operation" : operation, "rule_id" : "",  })
        response = client.recv()
        print response
        client.close()
        time.sleep(4)

# python client.py {device_id} {operation} {rule_id} {id} {args}

# rule_conditions: python client.py {device_id} {operation} {id} {rule_id} {} 
rules = {
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
}

# rule_time_condition -> IRRIGATE
# rule_conditions -> Compliment de totes per fer el irrigate
            

# 