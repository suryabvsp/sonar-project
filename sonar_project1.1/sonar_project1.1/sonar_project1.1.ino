/*ver 1.1*/

// Includes the Servo library
#include <Servo.h>. 

// Defines Trig and Echo pins of the Ultrasonic Sensor
const int trigPin = 2;
const int echoPin = 4;

// Variables for the duration & distance measured from the sensor
long duration;
int distance;

//Angle offset between graph and motor's XY axes
int offset = 0;
//Initial and final angles of motor's rotation
//Note that the motor can only take values 0-180.
int init_ang = 55+offset;
int fin_ang = 180+offset;

// Declares a structure variable/object of the type Servo,
//(defined in the servo library) for controlling the servo motor.
Servo myServo; 

void setup() {
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  Serial.begin(9600); //Sets baud rate of serial communication
  myServo.attach(9); // Defines on which pin is the servo motor attached
}

void loop() {
// if (Serial.available() > 0)  /*piece of code to be included in case of serial communication with IoT board*/
// {
    // rotates the servo motor from init_ang to fin_ang degrees
    for(int i=init_ang;i<=fin_ang;i++)
    {  
      myServo.write(i-offset);  //angle value to be passed to the servo library object for writing into the motor
      delay(30);  //DELAY #1, contains time taken in motor movement to the next degree before calculating distance.
      distance = calculateDistance();// Calls a function for calculating the distance measured by the Ultrasonic sensor for each degree
      Serial.print(newang); // Sends the current degree into the Serial Port
      Serial.print(","); // Sends addition character right next to the previous value needed later in the Processing IDE for indexing
      Serial.print(distance); // Sends the distance value into the Serial Port
      Serial.print("."); // Sends addition character right next to the previous value needed later in the Processing IDE for indexing
    }
    // Repeats the previous lines from fin_ang to init_ang degrees
    for(int i=fin_ang;i>init_ang;i--)
    {  
      myServo.write(i);
      delay(30);
      distance = calculateDistance();
      int newang = i;//-(init_ang-0);
      Serial.print(newang);
      Serial.print(",");
      Serial.print(distance);
      Serial.print(".");
    }
//}
  
}
// Function for calculating the distance measured by the Ultrasonic sensor
int calculateDistance(){ 
  
  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH); 
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH); // Reads the echoPin, returns the sound wave travel time in microseconds
  //distance= duration*0.034/2;
  distance = (duration/2)/29.1;     //in cm
  return distance;
}
