/*
* == ESP32: Task Handles Definition ==
* 
* - Part of Smart Pet Feeder ESP32 Firmware:
* > handles for created tasks
*/

#ifndef TASK_HANDLES_H
#define TASK_HANDLES_H

#include <Arduino.h>

extern TaskHandle_t TaskHandle_pirSensor;
extern TaskHandle_t TaskHandle_DispenserSensor;
extern TaskHandle_t TaskHandle_FoodSensor;
extern TaskHandle_t TaskHandle_WaterSensor;

extern TaskHandle_t TaskHandle_StateLED;
extern TaskHandle_t TaskHandle_NotificationLED;
extern TaskHandle_t TaskHandle_SolenoidValve;
extern TaskHandle_t TaskHandle_StepperMotor;

#endif