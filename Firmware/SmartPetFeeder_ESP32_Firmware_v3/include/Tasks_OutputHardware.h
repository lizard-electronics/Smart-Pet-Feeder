/*
* == ESP32: Tasks for the Output Hardware ==
* 
* - Part of Smart Pet Feeder ESP32 Firmware:
* > tasks to control the ESP32 output hardware 
*   by reading the Queues of MQTT with the subsribed topics
*/

#ifndef TASKS_OUTPUT_HARDWARE_H
#define TASKS_OUTPUT_HARDWARE_H

#include <Arduino.h>
#include <Stepper.h>
#include "Hardware_Pinout.h"
#include "Task_Handles.h"
#include "Tasks_MQTT.h"

void solenoid_valve_task(void* pvParameters);
void led_notification_task(void* pvParameters);
void led_level_task(void* pvParameters);
void led_state_task(void* pvParameters);
void stepper_motor_task(void* pvParameters);

#endif