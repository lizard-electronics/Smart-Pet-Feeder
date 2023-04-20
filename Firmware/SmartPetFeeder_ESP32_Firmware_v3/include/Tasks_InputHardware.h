/*
* == ESP32: Tasks for the Input Hardware ==
* 
* - Part of Smart Pet Feeder ESP32 Firmware:
* > tasks to read the ESP32 input hardware 
*   and publish each state to MQTT via Queues
*/

#ifndef TASKS_INPUT_HARDWARE_H
#define TASKS_INPUT_HARDWARE_H

#include <Arduino.h>
#include "Hardware_Pinout.h"
#include "Task_Handles.h"
#include "Tasks_MQTT.h"

// Task Declarations
void pirSensor_task(void* pvParameters);
void levelSensor_dispenser_task(void * pvParameters);
void levelSensor_food_task(void * pvParameters);
void levelSensor_water_task(void * pvParameters);
void button_maintenance_task(void* pvParameters);
void safetySwitch_lid_task(void* pvParameters);
void safetySwitch_bowl_task(void* pvParameters);

#endif