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
#include "Adafruit_BMP085.h"
#include "DHT.h"

#define DHTPIN 9
#define DHTTYPE DHT22

extern HardwareSerial Serial;

Timer timer;
Adafruit_BMP085 bmp;
DHT dht(DHTPIN, DHTTYPE);


uint8_t aliveLed1   = 10;
uint8_t aliveLed2   = 11;
uint8_t btReset     = 12;
uint8_t btStatus    = 13;
uint8_t propeller   = 2;
uint8_t wind0       = 4;
uint8_t wind1       = 5;
uint8_t wind2       = 6;
uint8_t wind3       = 7;

unsigned long propellerCount = 0;

void onEvery100ms();
void onEverySecond();
void onEveryFiveSeconds();
void onEveryMinute();

void onPropellerInterrupt();

void measureTemperature();
void measurePresure();
void measureHumidity();
void measureWindDirection();


void setup() {
    Serial.begin(115200);
    Serial.write("+OKMeteo2\n");
    
    pinMode(aliveLed1, OUTPUT);
    pinMode(aliveLed2, OUTPUT);
    
    pinMode(btReset, OUTPUT);
    pinMode(btStatus, INPUT);
   
    pinMode(wind0, INPUT);
    pinMode(wind1, INPUT);
    pinMode(wind2, INPUT);
    pinMode(wind3, INPUT);
    
    //attachInterrupt(0, onPropellerInterrupt, RISING);
    
    bmp.begin();
    dht.begin();
    
    timer.every(100, onEvery100ms);
    timer.every(1000, onEverySecond);
    timer.every(5000, onEveryFiveSeconds);
    timer.every(60000, onEveryMinute);
        
    timer.oscillate(aliveLed1, 500, HIGH);
    timer.oscillate(aliveLed2, 500, LOW);
}


void loop() {
    timer.update();
}

void onEvery100ms() {
    
}

void onEverySecond() {
    Serial.print("presure:");
    Serial.println(bmp.readPressure());
    
    Serial.print("temperature:");
    Serial.println(bmp.readTemperature());
    
    Serial.print("propeller:");
    Serial.println(propellerCount);
    
}

void onEveryFiveSeconds() {
    
    Serial.print("Humidity:");
    Serial.println(dht.readHumidity());
    
}

void onEveryMinute() {
    propellerCount = 0;
    
}

void onPropellerInterrupt() {
    propellerCount++;
}


void measureTemperature() {
    
}

void measurePresure() {
    
}

void measureHumidity() {
    
}

void measureWindDirection() {
    
}
