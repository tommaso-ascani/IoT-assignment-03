#ifndef __ADJUSTABLELED__
#define __ADJUSTABLELED__

class AdjustableLed{                                   //Interfaccia base di un led regolabile

  public:
    int Pin;                                                         //Attributo dove viene memorizzato il pin del Led
    int Brightness;                                                  //Attributo dove viene memorizzato il valore dell'intensità del led (da 0 a 4)
    bool IsOn;    
  
  public:
    AdjustableLed(int p);
    bool isOn();
    void setBrightness(int);
};

#endif
