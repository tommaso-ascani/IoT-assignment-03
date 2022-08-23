#ifndef __SHAREDVARIABLES__
#define __SHAREDVARIABLES__

#include "Arduino.h"

class SharedVariables {                                                                                                         //Interfaccia che mantiene tutte le variabili/flag condivise tra le varie task

private:
  bool manualMode;
  bool autoMode;
  bool alarmMode;


public:
  int currentTemp;
  int currentLight;
  bool irrigationOn;
  bool irrigationReady;
  int irrigationIntensity;
  long currentMillisPauseIrrigation;
  bool volatile stopIr;


public:
  
  SharedVariables(){
    this->manualMode = false;
    this->alarmMode = false;
    this->autoMode = true;
  }
  
  bool isManualMode() { return this->manualMode; }

  void setManualMode() { 
    this->autoMode = false;
    this->alarmMode = false;
    this->manualMode = true;
  }

  
  bool isAlarmMode() { return this->alarmMode; }
  
  void setAlarmMode() { 
    this->manualMode = false;
    this->autoMode = false;
    this->alarmMode = true;
  }

  bool isAutoMode() { return this->autoMode; }
  
  void setAutoMode() { 
    this->manualMode = false;
    this->alarmMode = false;
    this->autoMode = true;
  }

  void setIrrigation(){
    this->irrigationOn = !this->irrigationOn;
    return;
  }
};
#endif
