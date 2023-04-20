/*
 * == Smart Pet Feeder ESP32 Firmware: Main File ==
 * 
 * - Version: v3
 * > Project was migrated to PlatformIO: better for code development & debug
 * > Whole code was redesigned for better implementation w/ FreeRTOS
 *
 * - Developed by:
 * > Manuel Lagarto
 * > LEEC-PIC, 2022/2023
 */

#include <Arduino.h>
#include "Hardware_Pinout.h"
#include "Task_Handles.h"
#include "Tasks_MQTT.h"
#include "Tasks_InputHardware.h"
#include "Tasks_OutputHardware.h"

void task_controller(void* pvParameters);

void setup() {
  Serial.begin(115200);

	// Pin mode config
  pinMode(LED_NOTIFICATION_GPIO_OUTPUT, OUTPUT);
  pinMode(LED_LEVEL_GPIO_OUTPUT, OUTPUT);
  pinMode(LED_STATE_GPIO_OUTPUT, OUTPUT);
  pinMode(VALVE_WATER_GPIO_OUTPUT, OUTPUT);
  pinMode(TRIG_DISPENSER_GPIO_OUTPUT, OUTPUT);
	//pinMode(TRIG_FOOD_GPIO_OUTPUT, OUTPUT);
	//pinMode(TRIG_WATER_GPIO_OUTPUT, OUTPUT);
  pinMode(STEPPER_IN4_GPIO_OUTPUT, OUTPUT);
  pinMode(STEPPER_IN3_GPIO_OUTPUT, OUTPUT);
  pinMode(STEPPER_IN2_GPIO_OUTPUT, OUTPUT);
  pinMode(STEPPER_IN1_GPIO_OUTPUT, OUTPUT);

	pinMode(SENSOR_PIR_GPIO_INPUT, INPUT);
	pinMode(ECHO_DISPENSER_GPIO_INPUT, INPUT);
	//pinMode(ECHO_FOOD_GPIO_INPUT, INPUT);
	//pinMode(ECHO_WATER_GPIO_INPUT, INPUT);
	pinMode(BUTTON_MAINTENANCE_GPIO_INPUT, INPUT);
	pinMode(SWITCH_LID_GPIO_INPUT, INPUT);
	pinMode(SWITCH_BOWL_GPIO_INPUT, INPUT);

	// Connect to the WiFi network
	Serial.print("\nConnecting to: ");
	Serial.print(WIFI_SSID);
	WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
	while(WiFi.status() != WL_CONNECTED) {
		Serial.print(".");
		vTaskDelay(500 / portTICK_PERIOD_MS);
	}
	Serial.println("\nWiFi Connected");
	Serial.print("IP Address: ");
	Serial.println(WiFi.localIP());

	// Set MQTT server address and port
	mqttClient.setServer(MQTT_BROKER_HOSTNAME, MQTT_BROKER_PORT);

	// Create the topic/message queue
	publish_queue = xQueueCreate(10, sizeof(message_t));
	subscribe_queue = xQueueCreate(10, sizeof(message_t));

	// Create MQTT tasks (Tasks_MQTT)
	xTaskCreatePinnedToCore(connect_task,		// Task to call
													"ConnectTask",	// Task name
													2048,						// Volume (bytes)
													NULL,						// Parameters
													1,							// Priority
													NULL,						// Task handle
													1);							// ESP32 core ID
	
	xTaskCreatePinnedToCore(subscribe_task,		// Task to call
													"SubscribeTask",	// Task name
													2048,							// Volume (bytes)
													NULL,							// Parameters
													1,								// Priority
													NULL,							// Task handle
													1);								// ESP32 core ID

	xTaskCreatePinnedToCore(publish_task,		// Task to call
													"PublishTask",	// Task name
													2048,						// Volume (bytes)
													NULL,						// Parameters
													1,							// Priority
													NULL,						// Task handle
													1);							// ESP32 core ID

	// Create input hardware tasks (Tasks_InputHardware)
	xTaskCreatePinnedToCore(pirSensor_task,					// Task to call
													"PIR Sensor",						// Task name
													2048,										// Volume (bytes)
													NULL,										// Parameters
													1,											// Priority
													&TaskHandle_pirSensor,	// Task handle
													0);											// ESP32 core ID

	xTaskCreatePinnedToCore(levelSensor_dispenser_task,		// Task to call
													"Dispenser Level Sensor",			// Task name
													2048,													// Volume (bytes)
													NULL,													// Parameters
													1,														// Priority
													&TaskHandle_DispenserSensor,	// Task handle
													0);														// ESP32 core ID

	xTaskCreatePinnedToCore(levelSensor_food_task,				// Task to call
													"Food Bowl Level Sensor",			// Task name
													2048,													// Volume (bytes)
													NULL,													// Parameters
													1,														// Priority
													&TaskHandle_FoodSensor,				// Task handle
													0);														// ESP32 core ID

	xTaskCreatePinnedToCore(levelSensor_water_task,				// Task to call
													"Water Bowl Level Sensor",		// Task name
													2048,													// Volume (bytes)
													NULL,													// Parameters
													1,														// Priority
													&TaskHandle_WaterSensor,			// Task handle
													0);														// ESP32 core ID

	xTaskCreatePinnedToCore(button_maintenance_task,		// Task to call
													"Button for Maintenance",	// Task name
													2048,											// Volume (bytes)
													NULL,											// Parameters
													1,												// Priority
													NULL,											// Task handle
													0);												// ESP32 core ID

	xTaskCreatePinnedToCore(safetySwitch_lid_task,	// Task to call
													"Lid Safety Switch",		// Task name
													2048,										// Volume (bytes)
													NULL,										// Parameters
													1,											// Priority
													NULL,										// Task handle
													0);											// ESP32 core ID

	xTaskCreatePinnedToCore(safetySwitch_bowl_task,	// Task to call
													"Bowl Safety Switch",		// Task name
													2048,										// Volume (bytes)
													NULL,										// Parameters
													1,											// Priority
													NULL,										// Task handle
													0);											// ESP32 core ID

	// Create output hardware tasks (Tasks_OutputHardware)
	xTaskCreatePinnedToCore(solenoid_valve_task,				// Task to call
													"Water Solenoid Valve",			// Task name
													2048,												// Volume (bytes)
													NULL,												// Parameters
													1,													// Priority
													&TaskHandle_SolenoidValve,	// Task handle
													0);													// ESP32 core ID

	xTaskCreatePinnedToCore(led_notification_task,			// Task to call
													"Notification LED",					// Task name
													2048,												// Volume (bytes)
													NULL,												// Parameters
													1,													// Priority
													&TaskHandle_NotificationLED,// Task handle
													0);													// ESP32 core ID

	xTaskCreatePinnedToCore(led_level_task,							// Task to call
													"Low Dispenser Level LED",	// Task name
													2048,												// Volume (bytes)
													NULL,												// Parameters
													1,													// Priority
													NULL,												// Task handle
													0);													// ESP32 core ID

	xTaskCreatePinnedToCore(led_state_task,					// Task to call
													"Feeder State LED",			// Task name
													2048,										// Volume (bytes)
													NULL,										// Parameters
													1,											// Priority
													&TaskHandle_StateLED,		// Task handle
													0);											// ESP32 core ID

	xTaskCreatePinnedToCore(stepper_motor_task,				// Task to call
													"Stepper Motor",					// Task name
													2048,											// Volume (bytes)
													NULL,											// Parameters
													1,												// Priority
													&TaskHandle_StepperMotor,	// Task handle
													0);												// ESP32 core ID
}

void loop() {
  // put your main code here, to run repeatedly:
}