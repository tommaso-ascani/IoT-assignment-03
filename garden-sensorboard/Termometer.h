#ifndef __TERMOMETER__
#define __TERMOMETER__

class Termometer{                                             //Interfaccia base di un termometro

  int Pin;                                                    //Attributo che tiene il nuemro del pin a cui è agganciato il termometro sulla breadboard

  public:
    Termometer(int);
    float getTemperature(); 
    int getTemperatureValue();
};
#endif
