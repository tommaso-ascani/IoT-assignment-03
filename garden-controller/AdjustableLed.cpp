#include "AdjustableLed.h"
#include "Arduino.h"

AdjustableLed::AdjustableLed(int p) {
  this->Pin = p;
  this->Brightness = 0;
  this->IsOn = false;
}

void AdjustableLed::setBrightness(int value){
  if(this->Brightness != value && value == 0) {
    this->Brightness = value;
    analogWrite(this->Pin, 0);
    this->IsOn = false;
    return;
  }
  if(this->Brightness != value){
    this->Brightness = value;
    value = this->Brightness * 63.75;
    analogWrite(this->Pin, value);
    this->IsOn = true;
    return;
  } 
}
