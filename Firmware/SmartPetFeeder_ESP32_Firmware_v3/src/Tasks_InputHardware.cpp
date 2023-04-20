#include "Tasks_InputHardware.h"

int32_t compute_distance_percentage(bool sensorType, uint32_t inputDistance) {
  // sensorType = 0 : Sharp IR (Bowls)
  // sensorType = 1 : Ultrasonic Sensor (Dispenser)
  const uint32_t maxDistance_type0 = 9;  // Empty (9)
  const uint32_t minDistance_type0 = 6;   // Full (6)

  const uint32_t maxDistance_type1 = 5;  // Empty (9)
  const uint32_t minDistance_type1 = 2;   // Full (6)

  int32_t range;
  int32_t measuredRange;

  if(sensorType == 0) {
    range = maxDistance_type0 - minDistance_type0;
    measuredRange = inputDistance - minDistance_type0;
  }
  else if (sensorType == 1) {
    range = maxDistance_type1 - minDistance_type1;
    measuredRange = inputDistance - minDistance_type1;
  }
  
  int32_t inputPercentage = 100-((measuredRange*100)/range);

  if(inputPercentage > 100) {
    inputPercentage = 100;
  }
  else if(inputPercentage < 0) {
    inputPercentage = 0;
  }

  return inputPercentage;
}

// Reads PIR sensor input
// !!!Task control for level sensors needs to be implemented!!!
void pirSensor_task(void * parameter) {
  static uint8_t pirState = LOW;
  static uint8_t lastState = LOW;
  
  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SENSOR_PIR[i];
    if (MQTT_PUB_SENSOR_PIR[i] == '\0') {
      break;
    }
  }

  // Task infinite loop
  while(true) {
    pirState = digitalRead(SENSOR_PIR_GPIO_INPUT);

    if(pirState != lastState) {
      // Checks if current state is HIGH AND != from lastState
      if(pirState == LOW) {
        Serial.println("\nMovement not Dectected");

        // If state goes LOW, waits a few seconds to check if state goes HIGH
        // Blocking time 2.5s (HC-SR501 datasheet)
        vTaskDelay(2500 / portTICK_PERIOD_MS);

        pirState = digitalRead(SENSOR_PIR_GPIO_INPUT);
        if(pirState == LOW) {
          // Falling edge trigger after chencking if PIR is definitely LOW
          Serial.println("pirSensor_task state sent");

          // Converts button_state to char arr
          sprintf(message.mqttMessage, "%d", pirState);

          // Send the message struct to the publish_task via publish_queue
          xQueueSend(publish_queue, &message, 0);

          // Resumes food bowl level sensor reading task
          vTaskResume(TaskHandle_FoodSensor);
          // Resumes water bowl level sensor reading task
          vTaskResume(TaskHandle_WaterSensor);
        }
      }
      else {        
        Serial.println("\nMovement Detected");
        Serial.println("pirSensor_task state sent");

        // Converts button_state to char arr
        sprintf(message.mqttMessage, "%d", pirState);

        // Send the message struct to the publish_task via publish_queue
        xQueueSend(publish_queue, &message, 0);

        // Resumes water bowl level sensor reading task
        //vTaskResume(TaskHandle_WaterSensor);
      }
      lastState = pirState;
    }
    else if(pirState == HIGH) {
      // Resumes water bowl level sensor reading task
      vTaskResume(TaskHandle_WaterSensor);
    }

    vTaskDelay(500 / portTICK_PERIOD_MS);   // Task delay in ms
  }
}

void levelSensor_dispenser_task(void* pvParameters) {
  uint64_t waveDuration;  // Wave sound duration
  uint64_t sensorDistance;      // Measured distance
  static uint64_t distanceMean;  // Measured distance after mean
  uint8_t samples = 5;

  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SENSOR_DISPENSER[i];
    if (MQTT_PUB_SENSOR_DISPENSER[i] == '\0') {
      break;
    }
  }

  // Clear trigger pin
  digitalWrite(TRIG_DISPENSER_GPIO_OUTPUT, LOW);
  vTaskDelay(0.002 / portTICK_PERIOD_MS);   // Task delay in us

  while(true) {
    // Suspends food dispenser level sensor reading task
    vTaskSuspend(TaskHandle_DispenserSensor);
    Serial.println("\nlevelSensor_dispenser_task initialized");
    
    // Sets trigger pin HIGH for 10us 
    digitalWrite(TRIG_DISPENSER_GPIO_OUTPUT, HIGH);
    vTaskDelay(0.010 / portTICK_PERIOD_MS);   // Task delay in us
    digitalWrite(TRIG_DISPENSER_GPIO_OUTPUT, LOW);
    
    // Reads the echo pin
    waveDuration = pulseIn(ECHO_DISPENSER_GPIO_INPUT, HIGH);

    // Calculates the distance
    sensorDistance = (waveDuration * 0.034) / 2;
    Serial.println(sensorDistance);

    // Compute percentage: 0 = Sharp IR Level Sensor
    uint32_t sensorPercentage = compute_distance_percentage(1, sensorDistance);

    // Converts value to char arr
    sprintf(message.mqttMessage, "%d", sensorDistance);

    // Send the message struct to the publish_task via publish_queue
    xQueueSend(publish_queue, &message, 0);
    Serial.println("levelSensor_dispenser_task stopped");

    vTaskDelay(1000 / portTICK_PERIOD_MS);   // Task delay in ms
  }
}

void levelSensor_food_task(void* pvParameters) {
  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SENSOR_FOOD[i];
    if (MQTT_PUB_SENSOR_FOOD[i] == '\0') {
      break;
    }
  }

  while(true) {
    // Suspends food bowl level sensor reading task
    vTaskSuspend(TaskHandle_FoodSensor);
    Serial.println("\nlevelSensor_food_task initialized");

    float sensorVoltage = analogRead(SENSOR_FOOD_GPIO_INPUT) * (3.3/4095);
  	uint32_t sensorDistance = 13*pow(sensorVoltage, -1);

    // Compute percentage: 0 = Sharp IR Level Sensor
    uint32_t sensorPercentage = compute_distance_percentage(0, sensorDistance);

    Serial.println(sensorDistance);
    Serial.println(sensorPercentage);

    // Converts value to char arr
    sprintf(message.mqttMessage, "%d", sensorPercentage);

    // Send the message struct to the publish_task via publish_queue
    xQueueSend(publish_queue, &message, 0);

    Serial.println("levelSensor_food_task stopped");

    vTaskDelay(1000 / portTICK_PERIOD_MS);   // Task delay in ms
  }
}

void levelSensor_water_task(void* pvParameters) {
  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SENSOR_WATER[i];
    if (MQTT_PUB_SENSOR_WATER[i] == '\0') {
      break;
    }
  }
  
  while(true) {
    // Suspends food bowl level sensor reading task
    vTaskSuspend(TaskHandle_WaterSensor);
    Serial.println("\nlevelSensor_water_task initialized");

    float sensorVoltage = analogRead(SENSOR_WATER_GPIO_INPUT) * (3.3/4095);
  	uint32_t sensorDistance = 13*pow(sensorVoltage, -1);
    
    // Compute percentage: 0 = Sharp IR Level Sensor
    uint32_t sensorPercentage = compute_distance_percentage(0, sensorDistance);

    Serial.println(sensorDistance);
    Serial.println(sensorPercentage);

    // Converts value to char arr
    sprintf(message.mqttMessage, "%d", sensorPercentage);

    // Send the message struct to the publish_task via publish_queue
    xQueueSend(publish_queue, &message, 0);

    Serial.println("levelSensor_food_task stopped");

    vTaskDelay(1000 / portTICK_PERIOD_MS);   // Task delay in ms
  }
}

void button_maintenance_task(void* pvParameters) {
  uint8_t last_button_state;

  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_BT_MAINTENANCE[i];
    if (MQTT_PUB_BT_MAINTENANCE[i] == '\0') {
      break;
    }
  }

  // Task infinite loop
  while (true) {
    // Read the button state
    uint8_t button_state = digitalRead(BUTTON_MAINTENANCE_GPIO_INPUT);

    if(button_state != last_button_state) {
      if(button_state == HIGH) {
        // Press button for 3s to enter maintenance mode
        vTaskDelay(3000 / portTICK_PERIOD_MS);

        button_state = digitalRead(BUTTON_MAINTENANCE_GPIO_INPUT);
        if(button_state == HIGH) {
          Serial.println("\nMaintenance mode");

          // Converts button_state to char arr
          sprintf(message.mqttMessage, "%d", button_state);
          Serial.println(message.mqttMessage);

          // Send the message struct to the publish_task via publish_queue
          xQueueSend(publish_queue, &message, 0);
        }
      }
    }
    last_button_state = button_state;
    vTaskDelay(500 / portTICK_PERIOD_MS);
  }  
}

void safetySwitch_lid_task(void* pvParameters) {
  bool switchState_lid = digitalRead(SWITCH_LID_GPIO_INPUT);
  bool last_switchState_lid = switchState_lid;

  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SW_LID[i];
    if (MQTT_PUB_SW_LID[i] == '\0') {
      break;
    }
  }

  while(true) {
    // Reads state of switches
    switchState_lid = digitalRead(SWITCH_LID_GPIO_INPUT);
    
    if(switchState_lid != last_switchState_lid) {
      if(switchState_lid == HIGH) {
        // Task Control when safety switch is triggered
        vTaskSuspend(TaskHandle_pirSensor);
        vTaskSuspend(TaskHandle_SolenoidValve);
        vTaskSuspend(TaskHandle_StepperMotor);
        digitalWrite(VALVE_WATER_GPIO_OUTPUT, LOW);
      }
      if(switchState_lid == LOW) {
        // Task Control when safety switch is back to normal
        vTaskResume(TaskHandle_SolenoidValve);
        vTaskResume(TaskHandle_StepperMotor);
        vTaskResume(TaskHandle_pirSensor);

        // Resumes food dispenser level sensor reading task
        vTaskResume(TaskHandle_DispenserSensor);
        // Resumes food bowl level sensor reading task
        vTaskResume(TaskHandle_FoodSensor);
        // Resumes water bowl level sensor reading task
        vTaskResume(TaskHandle_WaterSensor);
      }
      // Converts button_state to char arr
      sprintf(message.mqttMessage, "%d", switchState_lid);

      // Send the message struct to the publish_task via publish_queue
      xQueueSend(publish_queue, &message, 0);
    }
    last_switchState_lid = switchState_lid;
    vTaskDelay(500 / portTICK_PERIOD_MS);
  }
}

void safetySwitch_bowl_task(void* pvParameters) {
  bool switchState_bowl = digitalRead(SWITCH_BOWL_GPIO_INPUT);
  bool last_switchState_bowl = switchState_bowl;

  // Create a message struct to send to publish_task
  message_t message;

  // Copy the characters from the topic macro to the mqttTopic field
  for (int i = 0; i < sizeof(message.mqttTopic); i++) {
    message.mqttTopic[i] = MQTT_PUB_SW_BOWL[i];
    if (MQTT_PUB_SW_BOWL[i] == '\0') {
      break;
    }
  }

  while(true) {
    // Reads state of switches
    switchState_bowl = digitalRead(SWITCH_BOWL_GPIO_INPUT);
    
    if(switchState_bowl != last_switchState_bowl) {
      if(switchState_bowl == HIGH) {
        // Task Control when safety switch is triggered
        vTaskSuspend(TaskHandle_pirSensor);
        vTaskSuspend(TaskHandle_SolenoidValve);
        vTaskSuspend(TaskHandle_StepperMotor);
        digitalWrite(VALVE_WATER_GPIO_OUTPUT, LOW);
      }
      if(switchState_bowl == LOW) {
        // Task Control when safety switch is back to normal
        vTaskResume(TaskHandle_SolenoidValve);
        vTaskResume(TaskHandle_StepperMotor);
        vTaskResume(TaskHandle_pirSensor);

        // Resumes food dispenser level sensor reading task
        vTaskResume(TaskHandle_DispenserSensor);
        // Resumes food bowl level sensor reading task
        vTaskResume(TaskHandle_FoodSensor);
        // Resumes water bowl level sensor reading task
        vTaskResume(TaskHandle_WaterSensor);
      }
      // Converts button_state to char arr
      sprintf(message.mqttMessage, "%d", switchState_bowl);

      // Send the message struct to the publish_task via publish_queue
      xQueueSend(publish_queue, &message, 0);
    }
    last_switchState_bowl = switchState_bowl;
    vTaskDelay(500 / portTICK_PERIOD_MS);
  }
}