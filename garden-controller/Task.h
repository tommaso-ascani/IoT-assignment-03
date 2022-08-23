#ifndef __TASK__
#define __TASK__

class Task {                                                              //Interfaccia base di una qualsiasi task
  int Period;                                                             //Attributo che mantiene il periodo della task
  int timeElapsed;                                                        //Attributo che mantiene il tempo trascorso
  
public:
  virtual void init(int period){
    this->Period = period;  
    this->timeElapsed = 0;
  }

  virtual void tick() = 0;                                                //Metodo virutale e astratto che DEVE essere implementato dalle classi che estenderanno questa

  bool updateAndCheckTime(int basePeriod){                                
    this->timeElapsed += basePeriod;                                      //Aggiorna il valore del tempo trascorso aggiungendogli il periodo dello scheduler
    if (this->timeElapsed >= Period){                                     //Se il tempo trascorso è >= al periodo della task
      this->timeElapsed = 0;                                              //  azzero il valore del tempo trascorso
      return true;                                                        //  e torno true
    }
    return false;                                                         //Altrimenti torno false
  }

  int getPeriod(){
    return Period;
  }
 
};
#endif
