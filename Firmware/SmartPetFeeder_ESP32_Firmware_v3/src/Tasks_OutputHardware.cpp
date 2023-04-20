#include "Tasks_OutputHardware.h"

void solenoid_valve_task(void* pvParameters) {
  // MQTT data struct
  message_t message;

  while(true) {
    if(subscribe_queue != 0) {
      // Peeks topic and message from subscribe queue
      if(xQueuePeek(subscribe_queue, &(message), (TickType_t)10)) {
        // Check the received MQTT topic and message
        if(strcmp(message.mqttTopic, MQTT_SUB_VALVE_WATER) == 0) {
          // Received topic equals expected topic
          // Clears buffer from queue
          xQueueReceive(subscribe_queue, &message, portMAX_DELAY);
          
          Serial.println("Message received on LED");
          Serial.println(message.mqttTopic);
          Serial.println(message.mqttMessage);

          int value = atoi(message.mqttMessage);
          if(value == 0) {
            digitalWrite(VALVE_WATER_GPIO_OUTPUT, LOW);
          }
          if(value == 1) {
            digitalWrite(VALVE_WATER_GPIO_OUTPUT, HIGH);
          }
        }
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);
	}  
}

void led_notification_task(void* pvParameters) {
	// MQTT data struct
  message_t message;

	while(true) {
    if(subscribe_queue != 0) {
      // Peeks topic and message from subscribe queue
      if(xQueuePeek(subscribe_queue, &(message), (TickType_t)10)) {
        // Check the received MQTT topic and message
        if(strcmp(message.mqttTopic, MQTT_SUB_LED_NOTIFICATION) == 0) {
          // Received topic equals expected topic
          // Clears buffer from queue
          xQueueReceive(subscribe_queue, &message, portMAX_DELAY);
          
          Serial.println("Message received on LED");
          Serial.println(message.mqttTopic);
          Serial.println(message.mqttMessage);

          int value = atoi(message.mqttMessage);
          if(value == 0) {
            digitalWrite(LED_NOTIFICATION_GPIO_OUTPUT, LOW);
          }
          if(value == 1) {
            digitalWrite(LED_NOTIFICATION_GPIO_OUTPUT, HIGH);
          }
        }
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);
	}  
}

void led_level_task(void* pvParameters) {
  // MQTT data struct
  message_t message;

	while(true) {
    if(subscribe_queue != 0) {
      // Peeks topic and message from subscribe queue
      if(xQueuePeek(subscribe_queue, &(message), (TickType_t)10)) {
        // Check the received MQTT topic and message
        if(strcmp(message.mqttTopic, MQTT_SUB_LED_LEVEL) == 0) {
          // Received topic equals expected topic
          // Clears buffer from queue
          xQueueReceive(subscribe_queue, &message, portMAX_DELAY);
          
          Serial.println("Message received on LED");
          Serial.println(message.mqttTopic);
          Serial.println(message.mqttMessage);

          int value = atoi(message.mqttMessage);
          if(value == 0) {
            digitalWrite(LED_LEVEL_GPIO_OUTPUT, LOW);
          }
          if(value == 1) {
            digitalWrite(LED_LEVEL_GPIO_OUTPUT, HIGH);
          }
        }
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);
	}
}

void led_state_task(void* pvParameters) {
  // MQTT data struct
  message_t message;

	while(true) {
    if(subscribe_queue != 0) {
      // Peeks topic and message from subscribe queue
      if(xQueuePeek(subscribe_queue, &(message), (TickType_t)10)) {
        // Check the received MQTT topic and message
        if(strcmp(message.mqttTopic, MQTT_SUB_LED_STATE) == 0) {
          // Received topic equals expected topic
          // Clears buffer from queue
          xQueueReceive(subscribe_queue, &message, portMAX_DELAY);
          
          Serial.println("Message received on LED");
          Serial.println(message.mqttTopic);
          Serial.println(message.mqttMessage);

          int value = atoi(message.mqttMessage);
          if(value == 0) {
            digitalWrite(LED_STATE_GPIO_OUTPUT, LOW);
          }
          if(value == 1) {
            digitalWrite(LED_STATE_GPIO_OUTPUT, HIGH);
          }
        }
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);
	}
}

void stepper_motor_task(void* pvParameters) {
  const uint8_t feeder_div = 5;                                 // Number of feeder divisions
  const uint16_t steps_perRevolution = 2048;                    // Steps for each 360 rotation
  const uint16_t steps_perDiv = steps_perRevolution/feeder_div; // Steps for each feeder division
  //int8_t stepper_dir = 1;

  // Stepper definition
  Stepper stepper_feeder(steps_perRevolution, 
                         STEPPER_IN1_GPIO_OUTPUT,
                         STEPPER_IN2_GPIO_OUTPUT,
                         STEPPER_IN3_GPIO_OUTPUT,
                         STEPPER_IN4_GPIO_OUTPUT);
  // Set stepper speed [rpm]
  stepper_feeder.setSpeed(3);

	// MQTT data struct
  message_t message;

  while(true) {
    if(subscribe_queue != 0) {
      // Peeks topic and message from subscribe queue
      if(xQueuePeek(subscribe_queue, &(message), (TickType_t)10)) {
        // Check the received MQTT topic and message
        if(strcmp(message.mqttTopic, MQTT_SUB_MOTOR_FOOD) == 0) {
          // Received topic equals expected topic
          // Clears buffer from queue
          xQueueReceive(subscribe_queue, &message, portMAX_DELAY);
          
          Serial.println("Message received on MOTOR");
          Serial.println(message.mqttTopic);
          Serial.println(message.mqttMessage);

          int portions = atoi(message.mqttMessage);
          Serial.println(portions);
          if(portions != 0) {
            Serial.print("Stepper Moving...");
            for(int i=0; i<portions; i++) {
              stepper_feeder.step(steps_perDiv);
              vTaskDelay(500 / portTICK_PERIOD_MS);   // Task delay in ms
            }
            Serial.println("Stepper Stopped");

            // Resumes food bowl level sensor reading task
            vTaskResume(TaskHandle_FoodSensor);
            // Resumes food dispenser level sensor reading task
            vTaskResume(TaskHandle_DispenserSensor);
          }
        }
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);
  }
}