#include <WiFi.h>
#include <PubSubClient.h>
#include "Termometer.h"

#define MSG_BUFFER_SIZE  50
#define PINTERMOMETER 5
#define PINLED 4
#define PINPHOTORESISTOR 6

/*Task declaration*/

TaskHandle_t Task1;
TaskHandle_t Task2;

/*Variabile di utility*/
bool msgPublish = false;
bool msgPublishTermo = false;

/* wifi network info */

const char* ssid = "WiFi Borgiani";
const char* password = "divano1234!";

/* MQTT server address */
const char* mqtt_server = "broker.mqtt-dashboard.com";

/* MQTT client management */

WiFiClient espClient;
PubSubClient client(espClient);


unsigned long lastMsgTime = 0;
char msg[MSG_BUFFER_SIZE];
int valuePrecMisurationThermo = 0;
int valuePrecMisurationPhoto = 0;

Termometer* TermometerObject = new Termometer(PINTERMOMETER);

void setup_wifi() {

  delay(10);

  Serial.println(String("Connecting to ") + ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

/* MQTT subscribing callback */

void callback(char* topic, byte* payload, unsigned int length) {
  char chars[length + 1];
  memcpy(chars, payload, length);
  chars[length] = '\0';
  String msg = chars;
  if(msg == "AlarmOff"){
    Serial.println(String("Message arrived on [") + topic + "] len: " + length + " message is: " + msg);
    digitalWrite(PINLED, HIGH);
  }
  Serial.println(String("Message arrived on [") + topic + "] len: " + length + " message is: " + msg);
}

void reconnect() {
  
  // Loop until we're reconnected
  
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    
    // Create a random client ID
    String clientId = String("esiot-2122-client-")+String(random(0xffff), HEX);

    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe("pc to esp");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
//      delay(5000);
    }
  }
}

void setup() {
  
  Serial.begin(115200); 
  pinMode(PINLED, OUTPUT);
  digitalWrite(PINLED, HIGH);

  setup_wifi();
  randomSeed(micros());
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
      int lightVal = map (analogRead(PINPHOTORESISTOR), 0, 4095, 0, 7);
      if(valuePrecMisurationPhoto != lightVal) {
        switch(lightVal){
          case 7: case 6: case 5: {
            Serial.println("case 7-6-5 photo: " + String(lightVal));
            snprintf (msg, MSG_BUFFER_SIZE, "Led1234Off");
            Serial.println(msg);
            client.publish("esp to pc", msg);
            msg[0] = '\0';
            msgPublish = true;
            break;
          }
          case 4: case 3: case 2: {
            Serial.println("case 4-3-2 photo: " + String(lightVal));
            snprintf (msg, MSG_BUFFER_SIZE, "Led1234On");
            client.publish("esp to pc", msg);
            msg[0] = '\0';
            msgPublish = true;
            break;
          }
          case 1: case 0: {
            Serial.println("case 1-0 photo: " + String(lightVal));
            snprintf (msg, MSG_BUFFER_SIZE, "IrOn");
            //client.publish("esp to pc", msg);
            msg[0] = '\0';
            msgPublish = true;
            break;
          }
        }
        valuePrecMisurationPhoto = lightVal;
      }
      int val = TermometerObject->getTemperatureValue();
      if(valuePrecMisurationThermo != val){
        Serial.println("val " + String(val));
        switch(val){
          case 5: {
            snprintf (msg, MSG_BUFFER_SIZE, "AlarmOn");
            digitalWrite(PINLED, LOW);
            client.publish("esp to pc", msg);
            msg[0] = '\0';
            Serial.println("case 5 termo");
            msgPublishTermo = true;
            break;
          }
          case 4: {
            snprintf (msg, MSG_BUFFER_SIZE, "IrSpeed1");
            //client.publish("esp to pc", msg);
            msg[0] = '\0';
            Serial.println("case 4 termo");
            msgPublishTermo = true;
            break;
          }
          case 3: {
            snprintf (msg, MSG_BUFFER_SIZE, "IrSpeed2");
            //client.publish("esp to pc", msg);
            msg[0] = '\0';
            Serial.println("case 3 termo");
            msgPublishTermo = true;
            break;
          }
          case 2: {
            snprintf (msg, MSG_BUFFER_SIZE, "IrSpeed3");
            //client.publish("esp to pc", msg);
            msg[0] = '\0';
            Serial.println("case 2 termo");
            msgPublishTermo = true;
            break;
          }
          case 1: {
            snprintf (msg, MSG_BUFFER_SIZE, "IrSpeed4");
            //client.publish("esp to pc", msg);
            msg[0] = '\0';
            Serial.println("case 1 termo");
            msgPublishTermo = true;
            break;
          }
          default: {
            Serial.println("default " + String(val));
            msgPublishTermo = true;
          }
        }
        valuePrecMisurationThermo = val;
      }
      delay(500);
 }
