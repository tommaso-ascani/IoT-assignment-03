#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Timer.h"
#include "Task.h"

//Definisco il numero massimo di task che lo scheduler può avere
#define MAX_TASKS 10

class Scheduler {                                                           //Interfaccia di base di uno scheduler

    int basePeriod;                                                         //Attributo che contiene il periodo dello scheduler
    int nTasks;                                                             //Attributo che contiene il numero corrente delle task memorizzate nello scheduler
    Task* taskList[MAX_TASKS];                                              //Attributo che tiene un riferimento ad un'array di Task di lunghezza massima MAX_TASK
    Timer timer;                                                            //Attributo che contiene un timer per lo scorrere del periodo dello scheduler

  public:
    void init(int basePeriod);
    virtual bool addTask(Task* task);
    virtual void schedule();
};
#endif
