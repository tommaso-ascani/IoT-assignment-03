#ifndef __IRRIGATIONTASK__
#define __IRRIGATIONTASK__

#include "Task.h"
#include "SharedVariables.h"
#include "ServoTimer2.h"

class IrrigationTask: public Task{
  public:
    enum State{ PAUSE, READY, ACTIVE }state;
    ServoTimer2* servo;
    SharedVariables* sharedVar;

  public:
    IrrigationTask(ServoTimer2*, SharedVariables*);
    void init(int p);
    void tick();
    void startIrrigation(int);
    void stopIrrigation();
    
};



#endif
