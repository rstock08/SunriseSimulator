/*
 * Authors: Aaron Ambrose, Junhan Wang, RJ Hemmingsen, Reed Stock
 * Description: Smart devices used to tint windows. Secondary hub recieves commands from a main hub which forwards
 * commands from an application made for Android devices and controls the tinting of the film on the window.
*/

// Include libraries
#include <OneWire.h>
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h> 
#include <SoftwareSerial.h>
#include <SPI.h>
#include <EEPROM.h>

// Define variables
#define BLUETOOTH_RX 0
#define BLUETOOTH_TX 1

// Setup bluetooth communication
SoftwareSerial BT(BLUETOOTH_RX, BLUETOOTH_TX);

// 2D Array holds all windows
//int window [30][3] = {{null}};

// RF Controls
RF24 radio(7, 8); // CE, CSN
const byte address[6] = "00001";

String bufferRec = "";

// Initializes components
void setup() {
  // RF Setup
  Serial.begin(9600);
  Serial.println("Setup");

  radio.begin();
  radio.openWritingPipe(address);
  radio.setPALevel(RF24_PA_MIN);
  radio.stopListening();

  // Bluetooth Setup
  Serial.begin(9600);
  BT.begin(9600);

  delay(500);
  
  if (BT.available() > 0)
    Serial.println(BT.readString());
}

void loop() {

    // Checks if bluetooth has sent a message - switch to interrupt
    Serial.println("Here 1");
    delay(2000);
    if (BT.available() > 0) {
        Serial.println("Message recieved!");
        Serial.println(BT.readString());
        bufferRec = BT.readString();
        if (bufferRec.indexOf("on") >= 0) {
          Serial.println("Here 2");
          rfSend(1);
        }
        else if (bufferRec.indexOf("off") >= 0) {
          Serial.println("Here 3");
          rfSend(0);
        }
        BT.flush();
    }
}

// Sends RF signal
void rfSend(int stat){

    // START: RF Send
    if (stat == 1) {
      const char text[] = "y";
      radio.write(&text, sizeof(text));    
      Serial.print("Sent ");
      Serial.print(text);
      Serial.print(" to mini hub.\n");
    }

    else if (stat == 0) {
      const char text2[] = "n";
      radio.write(&text2, sizeof(text2));    
      Serial.print("Sent ");
      Serial.print(text2);
      Serial.print(" to mini hub.\n");
    }
    
    Serial.println();
    // END: RF Send
}

// Sends bluetooth communication
void BTop(){
    //BT.print("Hello Bluetooth!");
    BT.println("Message not recieved!");
    Serial.println("Message not recieved!");
    BT.flush();
    delay(2000);
 
    if (BT.available() > 0) {
        Serial.println("Message recieved!");
        Serial.println(BT.readString());
    }
}

