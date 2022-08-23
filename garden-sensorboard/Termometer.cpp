#include "Termometer.h"
#include <Arduino.h>

Termometer::Termometer(int pin){
  this->Pin = pin;
}

float Termometer::getTemperature(){
  float valCorrente = analogRead(this->Pin);
  float temp = valCorrente/1023;
  float tempinC = (temp - 0.5) * 100;             //Formula per conversione dal voltaggio a temperatura in C° del sensore TMP36
  return tempinC;
}

int Termometer::getTemperatureValue(){
  float temp = this->getTemperature();
  int val = map(temp, -10, 40, 1, 5);
  return val;
}
