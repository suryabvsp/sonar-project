import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import java.awt.event.KeyEvent; 
import java.io.IOException; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class radar_processing1_1 extends PApplet {

/*   Arduino Radar Project
 *    1.1
 */

 // imports library for serial communication
 // imports library for reading the data from the serial port


Serial myPort; // defines Object Serial
// defines variables
String angle="";
String distance="";
String data="";
String noObject;
float pixsDistance;
int iAngle;
float iDistance;
int index1=0;
int index2=0;
int objCount=0,obctr=0;
PFont orcFont;
float angFlag=1.0f;
float prevAng=0,deltaAng=0;
float maxDIST=12; //max distance of object in cms
int wctr=0,objWidth=0;
public void setup() {
  
  // ***CHANGE THIS TO YOUR SCREEN RESOLUTION***
 
 myPort = new Serial(this,"COM3", 9600); // Enter the COM Port address as COM4 or COM 22.starts the serial communication
 myPort.bufferUntil('.'); // reads the data from the serial port up to the character '.'. So actually it reads this: angle,distance.
 orcFont = loadFont("CenturySchL-Ital-20.vlw");

}

public void draw() {
  
  fill(237,13,245);
  textFont(orcFont);
  // simulating motion blur and slow fade of the moving line
  noStroke();
  fill(184,13); 
  rect(0, 0, width, height-height*0.065f); 
  
  fill(211,27,228); //  color
  // calls the functions for drawing the radar
  drawRadar(); 
  drawLine();
  drawObject();
  drawText();
}

public void serialEvent (Serial myPort) { // starts reading data from the Serial Port
  // reads the data from the Serial Port up to the character '.' and puts it into the String variable "data".
  try{
    data = myPort.readStringUntil(';');
    data = data.substring(0,data.length()-1);
    
    index1 = data.indexOf(","); // find the character ',' and puts it into the variable "index1"
    angle= data.substring(0, index1); // read the data from position "0" to position of the variable index1 or thats the value of the angle the Arduino Board sent into the Serial Port
    distance= data.substring(index1+1, data.length()); // read the data from position "index1" to the end of the data pr thats the value of the distance
    
    // converts the String variables into Integer
    iAngle = PApplet.parseInt(angle);
    iDistance = PApplet.parseFloat(distance);
    deltaAng=iAngle-prevAng;

    if(deltaAng*angFlag<0.0f)
      objCount=0;
    
    //anticlockwise--deltaAng>0
    //clockwise--deltaAng<0    
    if(deltaAng>0.0f)
      angFlag=1.0f;
    else
      angFlag=-1.0f;
  }
  catch(Exception e){
        println("Error parsing:");
        e.printStackTrace();
    }
}

public void drawRadar() {
  pushMatrix();
  translate(width/2,height-height*0.507f); // moves the starting coordinates to new location
  noFill();
  strokeWeight(2);
  stroke(407,409,305);
  // draws the arc lines
  arc(0,0,(width-width*0.0385f),(width-width*0.0385f),PI,TWO_PI);
  arc(0,0,(width-width*0.26f),(width-width*0.26f),PI,TWO_PI);
  arc(0,0,(width-width*0.498f),(width-width*0.498f),PI,TWO_PI);
  arc(0,0,(width-width*0.754f),(width-width*0.754f),PI,TWO_PI);
  arc(0,0,(width-width*0.0385f),(width-width*0.0385f),0,PI);
  arc(0,0,(width-width*0.26f),(width-width*0.26f),0,PI);
  arc(0,0,(width-width*0.498f),(width-width*0.498f),0,PI);
  arc(0,0,(width-width*0.754f),(width-width*0.754f),0,PI);
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

public void drawLine() {
  pushMatrix();
  strokeWeight(8);
  stroke(32,65,174); //color for blue line
  translate(width/2,height-height*0.507f); // moves the starting coordinates to new location
  line(0,0,(height-height*0.575f)*cos(radians(iAngle)),-(height-height*0.575f)*sin(radians(iAngle))); // draws the line according to the angle
  popMatrix();
}

public void drawObject() {
  pushMatrix();
  translate(width/2,height-height*0.508f); // moves the starting coordinats to new location
  strokeWeight(8);
  stroke(240,1,40); // red color
  pixsDistance = iDistance*((height-height*0.1666f)*0.013f/3); // covers the distance from the sensor from cm to pixels
  if(iDistance<=maxDIST){
    if(wctr>2){    //Assuming that an object will be thick enough to be detected for 2 degrees of rotation.
      objWidth=0;
      // draws the object according to the angle and the distance
      line(pixsDistance*cos(radians(iAngle)),-pixsDistance*sin(radians(iAngle)),(width-width*0.505f)*cos(radians(iAngle)),-(width-width*0.505f)*sin(radians(iAngle)));
      obctr++;
    }
  }
  popMatrix();
}

public void drawText() { // draws the texts on the screen
  pushMatrix();
  if(iDistance>maxDIST) {
    noObject = "No object within Range";
    objWidth=wctr;
    wctr=0;
    if(obctr>0){
      objCount++;
      obctr=0;
    }
  }
  else {
    wctr++;
    if(wctr>2)  //Assuming that an object will be thick enough to be detected for 2 degrees of rotation.
    {
      noObject = "Object in Range";
    
    }
  } 

  fill(0,0,0);  //black background of bottom text
  noStroke();
  rect(0, height-height*0.0521f, width, height);
  fill(251,255,249);
  textSize(15);
  
  text("30cm",width-width*0.4041f,height-height*0.4793f);
  text("60cm",width-width*0.281f,height-height*0.4792f);
  text("90cm",width-width*0.177f,height-height*0.4792f);
  text("120cm",width-width*0.0729f,height-height*0.4792f);
  textSize(16);
  text(noObject, width-width*0.634f, height-height*0.0218f);
  text("Angle: " + iAngle +" \u00b0", width-width*0.97f, height-height*0.0232f);
  text("Distance: ", width-width*0.26f, height-height*0.0235f);
  textSize(16);
  text(" No. of objects: "+ objCount +"", width-width*0.986f, height-height*0.0714f);
  if(iDistance<=maxDIST) {
    if(wctr>2)
      text("        " + iDistance + " cm", width-width*0.185f, height-height*0.0237f);
  }
  textSize(19);
  fill(7,7,6); //color for degrees text
  translate((width-width*0.5020f)+width/2*cos(radians(30)),(height-height*0.5283f)-width/2*sin(radians(30)));
  rotate(-radians(-60));
  text("30\u00b0",0,0);
  resetMatrix();
  translate((width-width*0.507f)+width/2*cos(radians(60)),(height-height*0.5139f)-width/2*sin(radians(60)));
  rotate(-radians(-29));
  text("60\u00b0",-4,-5);
  resetMatrix();
  translate((width-width*0.507f)+width/2*cos(radians(90)),(height-height*0.5149f)-width/2*sin(radians(90)));
  rotate(radians(0));
  text("90\u00b0",-5,-2);
  resetMatrix();
  translate(width-width*0.513f+width/2*cos(radians(120)),(height-height*0.51286f)-width/2*sin(radians(120)));
  rotate(radians(-30));
  text("120\u00b0",0,0);
  resetMatrix();
  translate((width-width*0.5298f)+width/2*cos(radians(150)),(height-height*0.4803f)-width/2*sin(radians(150)));
  rotate(radians(-60));
  text("150\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.475f+width/2*cos(radians(180)),(height-height*0.47791f)-width/2*sin(radians(180)));
  rotate(radians(-90));
  text("180\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.494f+width/2*cos(radians(210)),(height-height*0.4797f)-width/2*sin(radians(210)));
  rotate(radians(-120));
  text("210\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.484f+width/2*cos(radians(240)),(height-height*0.4867f)-width/2*sin(radians(240)));
  rotate(radians(-150));
  text("240\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.474f+width/2*cos(radians(270)),(height-height*0.50151f)-width/2*sin(radians(270)));
  rotate(radians(-180));
  text("270\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.470f+width/2*cos(radians(300)),(height-height*0.51167f)-width/2*sin(radians(300)));
  rotate(radians(-210));
  text("300\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.478f+width/2*cos(radians(330)),(height-height*0.5174f)-width/2*sin(radians(330)));
  rotate(radians(-240));
  text("330\u00b0",0,0);
  resetMatrix();
  translate(width-width*0.523f+width/2*cos(radians(360)),(height-height*0.52132f)-width/2*sin(radians(360)));
  rotate(radians(-270));
    text("0\u00b0",0,0);
  popMatrix(); 
  prevAng = iAngle;
}
  public void settings() {  size (600, 700);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "radar_processing1_1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
