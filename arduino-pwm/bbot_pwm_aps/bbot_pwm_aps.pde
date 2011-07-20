#include "bbot_pwm_aps.h"

void setup(){
  // Setup pins configuration
  for (int i=0;i<2;i++){
    // Setup A, B, PWM pins
    for(int j=0; j<3; j++){
      pinMode(engines[i][j], OUTPUT);
    }
    // Setup A and B Faults
    pinMode(engines[i][3], INPUT);
    pinMode(engines[i][4], INPUT);  
  }
  
  Serial.begin(9600);
  Serial.flush();
  Serial.println("BBOT v.0.1-SNAPSHOT ready to work");
}

void loop(){
  analyzeEngineInput(0);
  analyzeEngineInput(1);
  
  // Command reading and executing
  if (Serial.available() >= 3){
    byte device    = Serial.read();
    byte operation = Serial.read();
    byte value     = Serial.read();
       
    // Write data to engine ports
    switch(operation){
      case FORWARD:
        setEngineForward(device, value);
        break;
      case BACKWARD:
        setEngineBackward(device, value);
        break;
      case STOP:
        stopEngine(device);
        break;
      case ECHO:
        setEcho(value);
        break;
      default:
        ; // unknown command
    }
      
    // Log action back
    if (echo){
      Serial.print("For DEVICE: ");
      Serial.print(device, DEC);
      Serial.print(" received COMMAND: ");
      Serial.print(operation, DEC);
      Serial.print(" with VALUE: ");
      Serial.println(value, DEC);
    }
  }
}

void analyzeEngineInput(int engine){
  int faultA = digitalRead(engines[engine][3]);
  int faultB = digitalRead(engines[engine][4]);
  if (faultA != 1 || faultB != 1 ){
    Serial.print("Fault detected for Device ");
    Serial.print(engine, DEC);    
    Serial.print(" locked. Fault A:");    
    Serial.print(faultA, DEC);
    Serial.print(" Fault B:");    
    Serial.println(faultB, DEC);
  }
}

void stopEngine(byte device){
  digitalWrite(engines[device][0], LOW);
  digitalWrite(engines[device][1], LOW);
  analogWrite(engines[device][2], 0);
  
  engineState[device] = STOP;
}

void setEngineForward(byte device, byte value){
  if (engineState[device] == BACKWARD){
    stopEngine(device); // to prevent device lock
  }
    
  digitalWrite(engines[device][0], HIGH);
  digitalWrite(engines[device][1], LOW);
  analogWrite(engines[device][2], value);
  
  engineState[device] = FORWARD;
}

void setEngineBackward(byte device, byte value){
  if (engineState[device] == FORWARD){
    stopEngine(device); // to prevent device lock
  }
  
  digitalWrite(engines[device][0], LOW);
  digitalWrite(engines[device][1], HIGH);
  analogWrite(engines[device][2], value);
  
  engineState[device] = BACKWARD;
}

void setEcho(byte value){
  echo = (value == 1);
}
