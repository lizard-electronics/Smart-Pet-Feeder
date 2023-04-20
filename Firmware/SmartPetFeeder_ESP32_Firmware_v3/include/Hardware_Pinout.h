/*
* == ESP32: Smart Pet Feeder Hardware Pinout ==
* 
* - Part of Smart Pet Feeder ESP32 Firmware:
* > pin numbers for the ESP32 based on the defined hardware design
*/

#ifndef HARDWARE_PINOUT_H
#define HARDWARE_PINOUT_H

// PIR Sensor
#define SENSOR_PIR_GPIO_INPUT 13

// Ultrassonic Level Sensors
#define TRIG_DISPENSER_GPIO_OUTPUT 14
#define ECHO_DISPENSER_GPIO_INPUT 27

//#define TRIG_FOOD_GPIO_OUTPUT 27
//#define ECHO_FOOD_GPIO_INPUT 26
#define SENSOR_FOOD_GPIO_INPUT 39   // ADC1_CH3

//#define TRIG_WATER_GPIO_OUTPUT 25
//#define ECHO_WATER_GPIO_INPUT 33
#define SENSOR_WATER_GPIO_INPUT 33  // ADC1_CH5

// Switches / Buttons
#define SWITCH_LID_GPIO_INPUT 32
#define SWITCH_BOWL_GPIO_INPUT 35
#define BUTTON_MAINTENANCE_GPIO_INPUT 34

// Solenoid Valve
#define VALVE_WATER_GPIO_OUTPUT 15

// Status LEDs
#define LED_NOTIFICATION_GPIO_OUTPUT 0
#define LED_LEVEL_GPIO_OUTPUT 4
#define LED_STATE_GPIO_OUTPUT 16

// Stepper Motor
#define STEPPER_IN4_GPIO_OUTPUT 17
#define STEPPER_IN3_GPIO_OUTPUT 5
#define STEPPER_IN2_GPIO_OUTPUT 18
#define STEPPER_IN1_GPIO_OUTPUT 19

// Reserved Pins
#define RESERVED2_GPIO 2
#define RESERVED21_GPIO 21
#define RESERVED22_GPIO 22
#define RESERVED23_GPIO 23


#endif