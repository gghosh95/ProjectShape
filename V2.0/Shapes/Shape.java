package Shapes;

import com.jme3.scene.Geometry;
import javax.swing.JTextArea;

public abstract class Shape {
    
    public abstract double Area(); // Compute area
    
    public abstract double Volume(); // Compute volume
     
    public abstract String Name(); // return name of shape
    
    public abstract int Dimension(); // return dimension of geometry
    
    public abstract Geometry generateGraphics(); //For displaying graphics
   
    public void printProperties(JTextArea jta) // print properties of existing geometric objects
    {
        jta.append("Name: "+Name()+"\n"+
                   "Dimension: "+Dimension()+"\n"+
                   "Area: "+Area()+"\n"+
                   "Volume: "+Volume()+"\n"+""+"\n");
    }
    
}
