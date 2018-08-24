/*
 * Authors: Aaron Ambrose, Junhan Wang, RJ Hemmingsen, Reed Stock
 * Description: Smart devices used to tint windows. Secondary hub recieves commands from a main hub which forwards
 * commands from an application made for Android devices and controls the tinting of the film on the window.
*/

// Libraries
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

// Variables
RF24 radio(9, 10); // CE, CSN
byte address[] = "00001";
char text[] = "";

// Intitialize/setup
void setup() {
  pinMode(6, OUTPUT);
  Serial.begin(9600);
  radio.begin();
  radio.openReadingPipe(0, address);
  radio.setPALevel(RF24_PA_MIN);
  radio.startListening();
}

// Main loop
void loop() {

  delay(1000);
  Serial.println("Checking");
  // Check for incoming command
  if (radio.available()) {
    Serial.println("here1");
    radio.read(&text, sizeof(text));
    Serial.println(text[0]);
    
    if(text[0] == 'u'){
      Serial.println("here2");
    }

    // Turn on
    if(text[0] == 'y'){
      turnOn();
      Serial.println("here3");
      Serial.println("On");
    }

    // Turn off
    if(text[0] == 'n'){
      turnOff();
      Serial.println("here4");
      Serial.println("Off");
    }
  }
}

// Turn on function
void turnOn(){
  digitalWrite(6, HIGH);
  Serial.println("turnOn called");
}

// Turn off function
void turnOff(){
  digitalWrite(6, LOW);
  Serial.println("turnoff called");
}

