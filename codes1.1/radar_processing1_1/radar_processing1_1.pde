/*   Arduino Radar Project
 *    1.1
 */

import processing.serial.*; // imports library for serial communication
import java.awt.event.KeyEvent; // imports library for reading the data from the serial port
import java.io.IOException;

Serial myPort; // defines Object Serial
// defines variables
String angle="";
String distance="";
String data="";
String noObject;
float pixsDistance;
int iAngle, iDistance;
int index1=0;
int index2=0;
int countFlag=0;
int objCount=0,Count_now=0;
PFont orcFont;
int angFlag=0;
int prevAng=0;
void setup() {
  
 size (600, 700); // ***CHANGE THIS TO YOUR SCREEN RESOLUTION***
 smooth();
 myPort = new Serial(this,"/dev/ttyACM0", 9600); // Enter the COM Port address as COM4 or COM 22.starts the serial communication
 myPort.bufferUntil('.'); // reads the data from the serial port up to the character '.'. So actually it reads this: angle,distance.
 orcFont = loadFont("CenturySchL-Ital-20.vlw");

}

void draw() {
  
  fill(237,13,245);
  textFont(orcFont);
  // simulating motion blur and slow fade of the moving line
  noStroke();
  fill(184,13); 
  rect(0, 0, width, height-height*0.065); 
  
  fill(211,27,228); //  color
  // calls the functions for drawing the radar
  drawRadar(); 
  drawLine();
  drawObject();
  drawText();
}

void serialEvent (Serial myPort) { // starts reading data from the Serial Port
  // reads the data from the Serial Port up to the character '.' and puts it into the String variable "data".
  try{
    data = myPort.readStringUntil('.');
    data = data.substring(0,data.length()-1);
    
    index1 = data.indexOf(","); // find the character ',' and puts it into the variable "index1"
    angle= data.substring(0, index1); // read the data from position "0" to position of the variable index1 or thats the value of the angle the Arduino Board sent into the Serial Port
    distance= data.substring(index1+1, data.length()); // read the data from position "index1" to the end of the data pr thats the value of the distance
    
    // converts the String variables into Integer
    iAngle = int(angle);
    iDistance = int(distance);
  }
  catch(Exception e){
        println("Error parsing:");
        e.printStackTrace();
    }
}

void drawRadar() {
  pushMatrix();
  translate(width/2,height-height*0.507); // moves the starting coordinates to new location
  noFill();
  strokeWeight(2);
  stroke(407,409,305);
  // draws the arc lines
  arc(0,0,(width-width*0.0385),(width-width*0.0385),PI,TWO_PI);
  arc(0,0,(width-width*0.26),(width-width*0.26),PI,TWO_PI);
  arc(0,0,(width-width*0.498),(width-width*0.498),PI,TWO_PI);
  arc(0,0,(width-width*0.754),(width-width*0.754),PI,TWO_PI);
  arc(0,0,(width-width*0.0385),(width-width*0.0385),0,PI);
  arc(0,0,(width-width*0.26),(width-width*0.26),0,PI);
  arc(0,0,(width-width*0.498),(width-width*0.498),0,PI);
  arc(0,0,(width-width*0.754),(width-width*0.754),0,PI);
  // draws the angle lines
  line(-width/2,0,width/2,0);
  line(0,0,(-width/2)*cos(radians(30)),(-width/2)*sin(radians(30)));
  line(0,0,(-width/2)*cos(radians(60)),(-width/2)*sin(radians(60)));
  line(0,0,(-width/2)*cos(radians(90)),(-width/2)*sin(radians(90)));
  line(0,0,(-width/2)*cos(radians(120)),(-width/2)*sin(radians(120)));
  line(0,0,(-width/2)*cos(radians(150)),(-width/2)*sin(radians(150)));
  line(0,0,(-width/2)*cos(radians(180)),(-width/2)*sin(radians(180)));
  line(0,0,(-width/2)*cos(radians(210)),(-width/2)*sin(radians(210)));
  line(0,0,(-width/2)*cos(radians(240)),(-width/2)*sin(radians(240)));
  line(0,0,(-width/2)*cos(radians(270)),(-width/2)*sin(radians(270)));
  line(0,0,(-width/2)*cos(radians(300)),(-width/2)*sin(radians(300)));
  line(0,0,(-width/2)*cos(radians(330)),(-width/2)*sin(radians(330)));
  line((-width/2)*cos(radians(30)),0,width/2,0);
  popMatrix();
}

void drawObject() {
  pushMatrix();
  translate(width/2,height-height*0.508); // moves the starting coordinats to new location
  strokeWeight(8);
  stroke(240,1,40); // red color
  pixsDistance = iDistance*((height-height*0.1666)*0.013/3); // covers the distance from the sensor from cm to pixels
  // limiting the range to 40 cms
  if(iDistance<40*3){
    // draws the object according to the angle and the distance
  line(pixsDistance*cos(radians(iAngle)),-pixsDistance*sin(radians(iAngle)),(width-width*0.505)*cos(radians(iAngle)),-(width-width*0.505)*sin(radians(iAngle)));
  }
  popMatrix();
}

void drawLine() {
  pushMatrix();
  strokeWeight(8);
  stroke(32,65,174);
  translate(width/2,height-height*0.507); // moves the starting coordinates to new location
  line(0,0,(height-height*0.575)*cos(radians(iAngle)),-(height-height*0.575)*sin(radians(iAngle))); // draws the line according to the angle
  popMatrix();
}

void drawText() { // draws the texts on the screen

    if(iAngle<prevAng)
      angFlag = 1;
    else
    {
      if(angFlag==1)
      {
        Count_now=0;
      angFlag = 0;
      }
    }
  pushMatrix();
  if(iDistance>40*0.3) {
    if(countFlag==1)
    {
      
       countFlag = 0; 
    }
  noObject = "No object within Range";
  }
  //For counting objects
  else {
    if(countFlag==0)
    {
      objCount++;
      Count_now++;
      countFlag = 1;
    }
    
     prevAng = iAngle; 
  noObject = "Object in Range";
  
  }
  fill(0,0,0);
  noStroke();
  rect(0, height-height*0.0521, width, height);
  fill(251,255,249);
  textSize(15);
  
  text("30cm",width-width*0.4041,height-height*0.4793);
  text("60cm",width-width*0.281,height-height*0.4792);
  text("90cm",width-width*0.177,height-height*0.4792);
  text("120cm",width-width*0.0729,height-height*0.4792);
  textSize(16);
  text(noObject, width-width*0.634, height-height*0.0218);
  text("Angle: " + iAngle +" °", width-width*0.97, height-height*0.0232);
  text("Distance: ", width-width*0.26, height-height*0.0235);
  textSize(16);
  text(" No. of objects: "+ objCount +"", width-width*0.986, height-height*0.0714);
  text(" No. of objects now: "+ Count_now +"", width-width*0.986, height-height*0.1714);
  if(iDistance<40*0.3) {
  text("        " + iDistance + " cm", width-width*0.185, height-height*0.0237);
  }
  textSize(19);
  fill(7,7,6);
  translate((width-width*0.5020)+width/2*cos(radians(30)),(height-height*0.5283)-width/2*sin(radians(30)));
  rotate(-radians(-60));
  text("30°",0,0);
  resetMatrix();
  translate((width-width*0.507)+width/2*cos(radians(60)),(height-height*0.5139)-width/2*sin(radians(60)));
  rotate(-radians(-29));
  text("60°",-4,-5);
  resetMatrix();
  translate((width-width*0.507)+width/2*cos(radians(90)),(height-height*0.5149)-width/2*sin(radians(90)));
  rotate(radians(0));
  text("90°",-5,-2);
  resetMatrix();
  translate(width-width*0.513+width/2*cos(radians(120)),(height-height*0.51286)-width/2*sin(radians(120)));
  rotate(radians(-30));
  text("120°",0,0);
  resetMatrix();
  translate((width-width*0.5298)+width/2*cos(radians(150)),(height-height*0.4803)-width/2*sin(radians(150)));
  rotate(radians(-60));
  text("150°",0,0);
  resetMatrix();
  translate(width-width*0.475+width/2*cos(radians(180)),(height-height*0.47791)-width/2*sin(radians(180)));
  rotate(radians(-90));
  text("180°",0,0);
  resetMatrix();
  translate(width-width*0.494+width/2*cos(radians(210)),(height-height*0.4797)-width/2*sin(radians(210)));
  rotate(radians(-120));
  text("210°",0,0);
  resetMatrix();
  translate(width-width*0.484+width/2*cos(radians(240)),(height-height*0.4867)-width/2*sin(radians(240)));
  rotate(radians(-150));
  text("240°",0,0);
  resetMatrix();
  translate(width-width*0.474+width/2*cos(radians(270)),(height-height*0.50151)-width/2*sin(radians(270)));
  rotate(radians(-180));
  text("270°",0,0);
  resetMatrix();
  translate(width-width*0.470+width/2*cos(radians(300)),(height-height*0.51167)-width/2*sin(radians(300)));
  rotate(radians(-210));
  text("300°",0,0);
  resetMatrix();
  translate(width-width*0.478+width/2*cos(radians(330)),(height-height*0.5174)-width/2*sin(radians(330)));
  rotate(radians(-240));
  text("330°",0,0);
  resetMatrix();
  translate(width-width*0.523+width/2*cos(radians(360)),(height-height*0.52132)-width/2*sin(radians(360)));
  rotate(radians(-270));
    text("0°",0,0);
  popMatrix(); 
}