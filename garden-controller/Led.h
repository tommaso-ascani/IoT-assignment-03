#ifndef __LED__
#define __LED__


class Led {                                                          //Interfaccia base di un led

  public:
    int Pin;                                                         //Attributo dove viene memorizzato il pin del Led
    bool IsOn;                                                       //Attributo che mantiene lo stato del led, true = acceso, false = spento
  
  public:
    Led(int);
    bool isOn();
    void turnOn();
    void turnOff();
};

#endif
