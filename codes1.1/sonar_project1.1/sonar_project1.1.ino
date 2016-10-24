/*ver 1.1*/
/*Work in progress*/

// Includes the Servo library
#include <Servo.h>. 

// Defines Trig and Echo pins of the Ultrasonic Sensor
const int trigPin = 2;
const int echoPin = 4;

// Variables for the duration & distance measured from the sensor
float duration;
float distance;

//Angle offset between graph and motor's XY axes
int offset = 0;
//Initial and final angles of motor's rotation
//Note that the motor can only take values 0-180.
//Make sure init_angle-offset >=60 to avoid jumper wire obstruction.
int init_ang = 60+offset;
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
      delay(30);//DELAY #1:for time taken in motor rotation for one degree before calculating distance
      distance = calculateDistance();// Calls a function for calculating the distance measured by the Ultrasonic sensor for each degree
      Serial.print(i); // Sends the current degree into the Serial Port for graphical representation
      Serial.print(","); // Sends addition character right next to the previous value needed later in the Processing IDE for indexing
      Serial.print(distance); // Sends the distance value into the Serial Port for the graph
      Serial.print(";"); // Sends addition character right next to the previous value needed later in the Processing IDE for indexing
    }
    // Repeats the previous lines from fin_ang to init_ang degrees
    for(int i=fin_ang;i>init_ang;i--)
    {  
      myServo.write(i);
      delay(30);  //DELAY #1
      distance = calculateDistance();
      Serial.print(i);
      Serial.print(",");
      Serial.print(distance);
      Serial.print(";");
    }
//}
}
// Function for calculating the distance measured by the Ultrasonic sensor
float calculateDistance(){ 
  digitalWrite(trigPin, LOW); // trigPin needs a fresh LOW pulse before sending a HIGH pulse that can be detected from echoPin
  delayMicroseconds(2);//DELAY #2:time for which low trig pulse is maintained before making it high
  digitalWrite(trigPin, HIGH); 
  delayMicroseconds(10);//DELAY #3:Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH); // Reads the echoPin, returns the sound wave travel time in microseconds
  //distance= duration*0.034/2;
  distance = (duration/2)/29.1;     //in cm, new way of calculating distance after calibration
  return distance;
}
