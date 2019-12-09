/*
 * slave.ino - Code for esp8266 slave to send sensor data to master
 * Created by Roger Truchero, December 6, 2019.
 */

// ------------ Definitions -------------

#include "DHT.h" // includes the library of DHT11 sensor
#define DHTTYPE DHT11
#include <ESP8266WiFi.h>
#include <PubSubClient.h>

// DHT11 sensor pin conf
#define dht_dpin D5
const int sensor_pin = A0;
DHT dht(dht_dpin, DHTTYPE); 

// Update these with values suitable for your network.
/*
const char* ssid = "<wifi_name>";
const char* password = "<wifi_pass>";
const char* mqtt_server = "<ip/host>";
*/

WiFiClient espClient;
PubSubClient client(espClient);

long lastMsg = 0;
char msg[100];

// Device id = 123
char* pub_path = "house/response/123";
char* sub_sensor = "house/sensor/123";
char* sub_webcam = "house/webcam/123";
char* sub_irrigate = "house/irrigate/123";
char* sub_stop_irrigate = "house/stop_irrigate/123";

// ------------ Functions -------------

void setup_wifi() 
{
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}


void callback(char* topic, byte* payload, unsigned int length) 
{
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.println("] ");

  if(strcmp(topic, sub_sensor) == 0)
  {
    // Send DHT11 data and soil moisture sensor data
    Serial.println("Proceeding to send sensor data!");
    float temperature = dht.readTemperature();
    float humidity = dht.readHumidity();
    float moisture = ( 100.00 - ( (analogRead(sensor_pin)/1023.00) * 100.00 ) );
    
    snprintf(msg, 100, "Temperature: %f ºC  Ambient humidity: %f pc  Moisture soil: %f pc", temperature, humidity, moisture);
    client.publish(pub_path, msg); 
  }
  else if(strcmp(topic, sub_webcam) == 0)
  {  
    // Get webcam data
    Serial.println("Get webcam data!");
    // TODO
    // Communicate with the ESP32 CAM and send image to the RPI
    snprintf(msg, 50, "Sending webcam data");
    client.publish(pub_path, msg); 
  } 
  else if(strcmp(topic, sub_irrigate) == 0)
  {       
    // Irrigate device    
    Serial.println("Irrigating device!");
    // TODO
    // ACTIVATE relee and irrigate the plant
    snprintf(msg, 50, "Starting irrigation response code: %d", 1);
    client.publish(pub_path, msg); 
  }
  else if(strcmp(topic, sub_stop_irrigate) == 0)
  {       
    // Stop Irrigate device    
    Serial.println("Deactivating device irrigation!");
    // TODO
    // DEACTIVATE relee and stop irrigating the plant
    snprintf(msg, 50, "Stopping irrigation response code: %d", 0);
    client.publish(pub_path, msg); 
  }
}

void reconnect() 
{
  // Loop until we're reconnected
  while (!client.connected()) 
  {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    
    // Attempt to connect
    if(client.connect(clientId.c_str())) 
    {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish(pub_path, "Hello Raspi, i am back!");
      // ... and resubscribe
      client.subscribe(sub_sensor);
      client.subscribe(sub_webcam);
      client.subscribe(sub_irrigate);
      client.subscribe(sub_stop_irrigate);
    } 
    else 
    {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setup() 
{
  // Initialize the DHT11 sensor and wifi connection
  dht.begin();
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop() 
{
  if(!client.connected()) 
  {
    reconnect();
  }
  client.loop();
}
