#include "DHT.h"        // including the library of DHT11 temperature and humidity sensor
#define DHTTYPE DHT11   // DHT 11

#define dht_dpin D5
DHT dht(dht_dpin, DHTTYPE); 

// Setup function
void setup()
{ 
  dht.begin();
  Serial.begin(9600);
  delay(700);
}

// Loop function
void loop() 
{
    // Wait RPi request data
    
    // Read DHT11 Sensor data
    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();         
    Serial.print("Current humidity = ");
    Serial.print(humidity);
    Serial.print("%  ");
    Serial.print("temperature = ");
    Serial.print(temperature); 
    Serial.println("C  ");
    
    // Read Soil moisture sensor

    // Send data to the RPi

    delay(800);
}
