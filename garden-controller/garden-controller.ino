#include "Arduino.h"
#include "Led.h"
#include "AdjustableLed.h"
#include "ServoTimer2.h"
#include "Scheduler.h"
#include "IrrigationTask.h"
#include "SharedVariables.h"
#include "MsgService.h"

#define PIN_LED1 7
#define PIN_LED2 8
#define PIN_ADJUSTABLE_LED1 5
#define PIN_ADJUSTABLE_LED2 6        //Usare questi pin altrimenti non funziona un cazzo per motivi sconosciuti ma connessi ai timer
#define PIN_MOTOR 10
#define PIN_RX 2
#define PIN_TX 3

Scheduler sched;
bool sendMsg = true;

SharedVariables* sharedVar = new SharedVariables();
  Led* Led1 = new Led(PIN_LED1);
  Led* Led2 = new Led(PIN_LED2);
  AdjustableLed* AdjLed1 = new AdjustableLed(PIN_ADJUSTABLE_LED1);
  AdjustableLed* AdjLed2 = new AdjustableLed(PIN_ADJUSTABLE_LED2);
  ServoTimer2* motore = new ServoTimer2();

  byte velCopy = motore->velocity;
  String precMsg = "";
  

void setup() {
  // put your setup code here, to run once:
  pinMode(PIN_LED1,OUTPUT);
  pinMode(PIN_LED2,OUTPUT);
  pinMode(13,OUTPUT);
  digitalWrite(13, LOW);

  MsgServiceBT.init();
  MsgService.init();
  
  sched.init(100);

  Serial.begin(9600);

  


//  Led1->turnOnOff();
//  Led2->turnOnOff();
//  Serial.println("Led 1 e 2 accesi");

//  AdjLed1->setBrightness(1);
//  AdjLed2->setBrightness(3);
//  Serial.println("Led regolabili 1 e 2 accesi");

  IrrigationTask* irrTask = new IrrigationTask(motore, sharedVar);
  irrTask->init(20);
  sched.addTask(irrTask);

  motore->attach(PIN_MOTOR);
  MsgService.sendMsg("state");
}

void loop() {
 
   if (MsgService.isMsgAvailable()) {
    Msg* msg = MsgService.receiveMsg();
    String completeMsg = msg->getContent();
    if(completeMsg != precMsg){
      precMsg = completeMsg;
      char secondChar = msg->getContent()[1];
      char fourthChar = msg->getContent()[3];
      char sixthChar = msg->getContent()[5];
      char eightChar = msg->getContent()[7];
      char tenthChar = msg->getContent()[9];
      char twelveChar = msg->getContent()[11];
      
        if(secondChar == '1'){
          Led1->turnOn();
        } else {
          Led1->turnOff();
        }
       
        if(fourthChar == '1'){
          Led2->turnOn();
        } else {
          Led2->turnOff();
        }
          
        int value = (int)(sixthChar);
        value = value - '0';
        AdjLed1->setBrightness(value);
        
        value = (int)(eightChar);
        value = value - '0';
        AdjLed2->setBrightness(value);
        
        if (tenthChar == 't'){
          //sharedVar->irrigationOn = true;
        } else if (tenthChar == 'f'){
          //sharedVar->irrigationOn = false;
        } 
        value = (byte)(twelveChar);
        if(velCopy != value){
          motore->setVelocity(value);
          velCopy = value;
        }
        
        
    }
    delete msg;
  }
  sched.schedule();
}
