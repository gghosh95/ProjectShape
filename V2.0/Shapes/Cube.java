package Shapes;


import java.util.ArrayList;
import com.jme3.scene.Geometry;
import com.jme3.util.TangentBinormalGenerator;


public class Cube extends ThreeDimension{
    
    private double sidelength;   // sidelength of cube
    private double X;        // X coordinate of centroid
    private double Y;        // Y coordinate of centroid
    private double Z;        // Z coordinate of centroid
    
    public Cube(ArrayList<Double> arguments)
    {
        this.X = arguments.get(0);
        this.Y = arguments.get(1);
        this.Z = arguments.get(2);
        this.sidelength = arguments.get(3);
    }
    
    @Override
    public double Area(){return 6*Math.pow(sidelength, 2);} // Compute area of circle
    
    @Override
    public double Volume(){return Math.pow(sidelength, 3);} // Compute volume of circle
    
    @Override
    public String Name(){return this.getClass().getSimpleName();} // return class name
    
    @Override
    public int Dimension(){return dimension;} // return dimension of this geometry
    
    @Override
    public Geometry generateGraphics() // for displaying graphics
    {
        com.jme3.scene.shape.Box boxMesh = new com.jme3.scene.shape.Box((float)sidelength/2, (float)sidelength/2, (float)sidelength/2);
        Geometry boxGeo = new Geometry("Shiny rock", boxMesh);
        boxGeo.setLocalTranslation((float) X, (float) Y, (float) Z);
        TangentBinormalGenerator.generate(boxMesh);
        return boxGeo;
    }
    
    // Helper stuff
    public double getX(){return this.X;}
    
    public double getY(){return this.Y;}
    
    public double getSideLength(){return this.sidelength;}
    
    public void setX(double X){this.X = X;}
    
    public void setY(double Y){this.Y = Y;}
    
    public void setSideLength(double sidelength){this.sidelength=sidelength;}
    
}
