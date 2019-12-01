package Shapes;


import com.jme3.scene.Geometry;
import com.jme3.util.TangentBinormalGenerator;
import java.util.ArrayList;

public class Circle extends TwoDimension{
    
    private double radius;   // Radius of circle
    private double X;        // X coordinate of centroid
    private double Y;        // Y coordinate of centroid
    
    public Circle(ArrayList<Double> arguments)
    {
        this.X = arguments.get(0);
        this.Y = arguments.get(1);
        this.radius = arguments.get(2);
    }
    
    @Override
    public double Area(){return radius*radius;} // Compute area of circle
    
    @Override
    public String Name(){return this.getClass().getSimpleName();} // return class name
    
    @Override
    public int Dimension(){return dimension;} // return dimension of shape
    
    @Override
    public Geometry generateGraphics() // for displaying graphics 
    {
        com.jme3.scene.shape.Cylinder circleMesh = new com.jme3.scene.shape.Cylinder(32, 32, (float)radius,(float)0.05);
        Geometry circleGeo = new Geometry("Shiny rock", circleMesh);
        circleGeo.setLocalTranslation((float) X, (float) Y, (float) 0);
        TangentBinormalGenerator.generate(circleMesh);
        return circleGeo;
    }
    
    // Helper stuff
    public double getX(){return this.X;}
    
    public double getY(){return this.Y;}
    
    public double getRadius(){return this.radius;}
    
    public void setX(double X){this.X = X;}
    
    public void setY(double Y){this.Y = Y;}
    
    public void setRadius(double radius){this.radius=radius;}
    
}
