/*
* == ESP32 FreeRTOS Tasks: MQTT Comms ==
* 
* - Part of Smart Pet Feeder ESP32 Firmware:
* > corresponds to all MQTT communication related tasks
* > uses Queues to communicate to and from Core0
*/

#ifndef TASKS_MQTT_H
#define TASKS_MQTT_H

#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
//#include "Task_Handles"

#define MQTT_SUB_LED_NOTIFICATION "esp32/led1"
#define MQTT_SUB_LED_LEVEL "esp32/led2"
#define MQTT_SUB_LED_STATE "esp32/led3"
#define MQTT_SUB_VALVE_WATER "esp32/valve"
#define MQTT_SUB_MOTOR_FOOD "esp32/motor"

#define MQTT_PUB_BT_MAINTENANCE "esp32/bt1"
#define MQTT_PUB_SW_LID "esp32/sw1"
#define MQTT_PUB_SW_BOWL "esp32/sw2"
#define MQTT_PUB_SENSOR_PIR "esp32/pir"
#define MQTT_PUB_SENSOR_DISPENSER "esp32/s1"
#define MQTT_PUB_SENSOR_FOOD "esp32/s2"
#define MQTT_PUB_SENSOR_WATER "esp32/s3"

// WiFi and MQTT broker config
extern const char* WIFI_SSID;
extern const char* WIFI_PASSWORD;
extern const char* MQTT_BROKER_HOSTNAME;
extern const int MQTT_BROKER_PORT;

extern WiFiClient wifiClient;
extern PubSubClient mqttClient;

// MQTT topic queue for FreeRTOS implementation
extern QueueHandle_t publish_queue,
										 subscribe_queue;

typedef struct {
	char mqttTopic[100];
	char mqttMessage[100];
} message_t;

// Task Declarations
void connect_task(void* pvParameters);
void publish_task(void* pvParameters);
void subscribe_task(void* pvParameters);
void subCallback(char* topic, byte* payload, uint32_t lenght);

#endif