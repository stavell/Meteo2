/// 
/// @mainpage	Meteo2Embedded 
///
/// @details	<#details#>
/// @n 
/// @n 
/// @n @a	Developed with [embedXcode](http://embedXcode.weebly.com)
/// 
/// @author	Stavel
/// @author	Shumen-XC
/// @date	27.04.13 11:27
/// @version	<#version#>
/// 
/// @copyright	© Stavel, 2013 г.
/// @copyright	CC = BY NC SA
///
/// @see	ReadMe.txt for references

#include "Arduino.h"
#include "Wire.h"

#include "Timer.h"
//#include "Adafruit_BMP085.h"
//#include "DHT.h"

extern HardwareSerial Serial;

Timer timer;

uint8_t aliveLed1 = 10;
uint8_t aliveLed2 = 11;

void onEvery100ms();
void onEverySecond();
void onEveryFiveSeconds();
void onEveryMinute();



void setup() {
    Serial.begin(115200);
    Serial.write("+OKMeteo2\n");
    
    pinMode(aliveLed1, OUTPUT);
    pinMode(aliveLed2, OUTPUT);
    
    timer.every(100, onEvery100ms);
    timer.every(1000, onEverySecond);
    timer.every(5000, onEveryFiveSeconds);
    timer.every(60000, onEveryMinute);
    
    
    timer.oscillate(aliveLed1, 500, HIGH);
    timer.oscillate(aliveLed2, 500, LOW);
    
}

void onEvery100ms() {
    
}

void onEverySecond() {
    
    
}

void onEveryFiveSeconds() {
    
}

void onEveryMinute() {
    
}


void loop() {
    timer.update();
}
