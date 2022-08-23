#include "Scheduler.h"

void Scheduler::init(int basePeriod){
  this->basePeriod = basePeriod;
  timer.setupPeriod(basePeriod);  
  nTasks = 0;
}

bool Scheduler::addTask(Task* task){
  if (nTasks < MAX_TASKS-1){                                                //Si controlla se lo scheduler può aggiungere la task o la sua coda è piena
    taskList[nTasks] = task;                                                //Se può aggiungerla la aggiunge alla sua lista
    nTasks++;                                                               //Aggiorna il numero di task che "possiede"
    return true;                                                            //Torna true in questo caso
  } else {
    return false;                                                           //Torna false nel caso in cui la sua lista/coda interna sia piena
  }
}
  
void Scheduler::schedule(){
  timer.waitForNextTick();                                                  //Aspetta il tick successivo del suo periodo
  for (int i = 0; i < nTasks; i++){                                         //Per ogni task nella sua lista
    if (taskList[i]->updateAndCheckTime(basePeriod)){                       //Se aggiornando il loro periodo risulta che quella task deve essere eseguita allora 
      taskList[i]->tick();                                                  // vienen eseguito il metodo tick di quella task
    }
  }
}
