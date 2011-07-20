#ifndef BBOT_PWM_APS_H_
#define BBOT_PWM_APS_H_

#include "WProgram.h"

#define FORWARD   0x0
#define BACKWARD  0x1
#define STOP      0x2
#define ECHO      0x3

int operations[] = {FORWARD, BACKWARD, STOP, ECHO};

int engines[2][5]={// A  B  Pwm, Fault A, Fault B
                    {52, 50, 2,    44,      42},
                    {48, 46, 3,    40,      38}
                  };

int engineState[] = {STOP, STOP};

boolean echo = true;

void analyzeEngineInput(int engine);

void stopEngine(byte device);

void setEngineForward(byte device, byte value);

void setEngineBackward(byte device, byte value);

void setEcho(byte value);

#endif  // BBOT_PWM_APS_H_
