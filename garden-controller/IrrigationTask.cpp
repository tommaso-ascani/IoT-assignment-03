#include "IrrigationTask.h"

IrrigationTask::IrrigationTask(ServoTimer2* ser, SharedVariables* sv){
  this->servo = ser;
  this->sharedVar = sv;

}

void IrrigationTask::init(int p){
  Task::init(p);
  this->state = READY;
}

void IrrigationTask::tick(){
  switch(this->state) {
    case READY: {
      if(this->sharedVar->irrigationOn){
        this->state = ACTIVE;
      }
      break;
    }
    case ACTIVE: {
      if(!this->sharedVar->irrigationOn){
        this->state = READY;
        break;
      }
      if (this->servo->work()){
        break;
      }
      this->state = PAUSE;
      this->sharedVar->irrigationOn = false;
      this->sharedVar->currentMillisPauseIrrigation = this->getPeriod();
      this->sharedVar->irrigationReady = false;
      break;
    }
    case PAUSE: {
      this->sharedVar->currentMillisPauseIrrigation += this->getPeriod();
      if(this->sharedVar->currentMillisPauseIrrigation >= this->servo->PAUSE_TIME){
        this->sharedVar->irrigationReady = true;
        this->sharedVar->currentMillisPauseIrrigation = 0;
        this->state = READY;
      }
      break;
    }
  }
}
