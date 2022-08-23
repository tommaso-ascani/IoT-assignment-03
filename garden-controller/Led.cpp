#include "Led.h"
#include "Arduino.h"

Led::Led(int p){
  this->Pin = p;
  this->IsOn = false;
}

bool Led::isOn() {
  return this->IsOn;
}

void Led::turnOn(){
  if(!this->IsOn){
    digitalWrite(this->Pin, HIGH);
    this->IsOn = !this->IsOn;
  } 
}

void Led::turnOff(){
  if(this->IsOn){
    digitalWrite(this->Pin, LOW);
    this->IsOn = !this->IsOn;
  }
}
