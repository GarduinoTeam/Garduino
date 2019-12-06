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
DHT dht(dht_dpin, DHTTYPE); 

// Update these with values suitable for your network.
const char* ssid = "<wifi_name>";
const char* password = "<wifi_pass>";
const char* mqtt_server = "<ip/host>";

WiFiClient espClient;
PubSubClient client(espClient);

long lastMsg = 0;
char msg[50];
String pub_path = "house/sensors";
String sub_path = "house";


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
  Serial.print("] ");
  for (int i = 0; i < length; i++) 
  {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  
  // Fins no rebre un missatge de la raspi quedat en bucle  
  Serial.print("Publish message: ");
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();        
  snprintf (msg, 50, "Temperature:  %2f and humidity %2f", temperature, humidity);
  Serial.println(msg);
  client.publish("house/sensors", msg);
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
      client.publish("house/sensors", "Hello Raspi, i am back!");
      // ... and resubscribe
      client.subscribe("house");
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
