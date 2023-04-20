#include "Tasks_MQTT.h"

// WiFi and MQTT broker config
//const char* WIFI_SSID = "PC-Manuel";
//const char* WIFI_PASSWORD = "L3NOVOy520";

//const char* WIFI_SSID = "RaspberryPi-AP";
//const char* WIFI_PASSWORD = "raspi_ap";

const char* WIFI_SSID = "NOS_Internet_56EF";
const char* WIFI_PASSWORD = "42148329";

//const char* MQTT_BROKER_HOSTNAME = "192.168.1.90";	// IP Home
//const char* MQTT_BROKER_HOSTNAME = "172.23.4.111";	// IP School #1
const char* MQTT_BROKER_HOSTNAME = "192.168.1.199";		// IP Hotspot
const int MQTT_BROKER_PORT = 1883;

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

// MQTT topic queue for FreeRTOS implementation
QueueHandle_t publish_queue = NULL,
							subscribe_queue = NULL;

void connect_task(void* pvParameters) {
	// Create a message struct
	message_t message;
	strcpy(message.mqttTopic, MQTT_SUB_LED_STATE);

	// Connect to the MQTT broker
	while(true) {
		if(!mqttClient.connected()) {
			if(mqttClient.connect("ESP32_Client")) { // Placeholder for the clientID
				// Connection successful
				Serial.println("Connection successful\n");

				// State LED (Green)
				char payload[] = "1";				
				strcpy(message.mqttMessage, payload);

				xQueueSend(subscribe_queue, &message, 0);
				Serial.println("subscribe_queue sent");
			} else {
				// Connection failed
				Serial.println("Connection failed\n");

				//WiFi.disconnect();
				//WiFi.reconnect();

				// State LED (Green)
				char payload[] = "0";				
				strcpy(message.mqttMessage, payload);

				xQueueSend(subscribe_queue, &message, 0);
				Serial.println("subscribe_queue sent");

				vTaskDelay(500 / portTICK_PERIOD_MS);
			}
		}
		vTaskDelay(1000 / portTICK_PERIOD_MS);
	}
}

void publish_task(void* pvParameters) {
	// Create a message struct
	message_t message;

	// Publish a message to a topic
	while(true) {
		// Wait for a message to be received from the queue
		xQueueReceive(publish_queue, &message, portMAX_DELAY);
		Serial.println("\npublish_queue received");
		
		// Publish the message to the MQTT topic
		mqttClient.publish(message.mqttTopic, message.mqttMessage);
		Serial.print("Message published on topic: ");
		Serial.println(message.mqttTopic);

		//vTaskDelay(1000 / portTICK_PERIOD_MS);
	}
}

void subscribe_task(void* pvParameters) {
	// Define callback function
	mqttClient.setCallback(subCallback);
	vTaskDelay(500 / portTICK_PERIOD_MS);
	
	// Subscribe to a topic
	mqttClient.subscribe(MQTT_SUB_LED_NOTIFICATION);
	mqttClient.subscribe(MQTT_SUB_LED_LEVEL);
	mqttClient.subscribe(MQTT_SUB_LED_STATE);
	mqttClient.subscribe(MQTT_SUB_MOTOR_FOOD);
	mqttClient.subscribe(MQTT_SUB_VALVE_WATER);

	while(true) {
		// Ensures the MQTT connection is maintained
		mqttClient.loop();
		vTaskDelay(1000 / portTICK_PERIOD_MS);
	}
}

void subCallback(char* topic, byte* payload, uint32_t length) {
	Serial.print("\nMessage arrived on topic: ");
	Serial.println(topic);
  
	char messageArray[length+1];
  for (int i = 0; i < length; i++) {
    //Serial.print((char)payload[i]);
    messageArray[i] = (char)payload[i];
  }
	messageArray[length] = 0;		// Char array NULL terminated
	Serial.println(messageArray);

	// Create a message struct
	message_t message;

	// Copy the received MQTT topic and message 
	// to mqttTopic and mqttMessage fields
	strcpy(message.mqttTopic, topic);
	//strcpy(message.mqttMessage, (char*)payload);
	strcpy(message.mqttMessage, messageArray);
	//Serial.println(message.mqttTopic);
	//Serial.println(message.mqttMessage);

	// Send the message struct to the handler task
	xQueueSend(subscribe_queue, &message, 0);
	Serial.println("subscribe_queue sent");
}