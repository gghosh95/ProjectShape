package GraphicalUserInterface;

import Shapes.Shape;
import com.jme3.app.SimpleApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.util.TangentBinormalGenerator;

public class DisplayGraphics extends SimpleApplication {

    protected final Canvas cnvs;
    static GraphicalUserInterface.CustomCam customCam = null;
    private final AmbientLight ambient = new AmbientLight();
    private final DirectionalLight light1 = new DirectionalLight();
    private boolean firstload = true;

    //==========================================================================
    public DisplayGraphics() {
        Logger.getLogger("").setLevel(Level.SEVERE);
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(30);
        setSettings(newSetting);
        createCanvas(); // create canvas!
        startCanvas();
        JmeCanvasContext ctx = (JmeCanvasContext) getContext();
        ctx.setSystemListener(this);
        cnvs = ctx.getCanvas();
        cnvs.setLocation(0, 0);
        setPauseOnLostFocus(false);
    } // end of Constructor

    //==========================================================================
    @Override
    public void simpleInitApp() {

        customCam = new GraphicalUserInterface.CustomCam(cam);
        if (customCam != null) {
            flyCam.setEnabled(false);
            customCam.registerWithInput(inputManager);
            customCam.setZoomSpeed(10f);
            customCam.setRotationSpeed(10f);
            customCam.setMoveSpeed(3f);
            customCam.setDistance(50.0f);
        }
        setDisplayStatView(false); // to hide the statistics
        setDisplayFps(false);
        viewPort.setBackgroundColor(ColorRGBA.Black);
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.8f, 1.0f));
        rootNode.addLight(ambient);
        // Set up the directional light
        light1.setColor(ColorRGBA.White);

        rootNode.addLight(light1);

        createSceneGraph();

    }

    //==========================================================================
    /* Use the main event loop to trigger repeating actions. */
//    private final Quaternion camQ = new Quaternion();
    @Override
    public void simpleUpdate(float tpf) {
//        if (firstload == true) {
//            MainGUI.Splash.setVisible(false);
//            MainGUI.mjf.setVisible(true);
//        }
        light1.setDirection(
                new Vector3f(
                        cam.getDirection().x,
                        cam.getDirection().y,
                        cam.getDirection().z));

        if (GUI.shapeList.isEmpty()) {
            rootNode.detachAllChildren();
            drawAxis();
        }
        if(GUI.ShapeStack.getSelectedValue() == null && !GUI.shapeList.isEmpty())
        {
            rootNode.detachAllChildren();
            drawAxis();
            for(int ii = 0 ; ii < GUI.shapeList.size(); ii++)
            {
                Shape s = GUI.shapeList.get(ii);
                Geometry g = s.generateGraphics();
                Material m = new Material(assetManager,
                        "Common/MatDefs/Light/Lighting.j3md");

                m.setBoolean("UseMaterialColors", true);
                m.setColor("Diffuse", ColorRGBA.LightGray);
                m.setColor("Specular", ColorRGBA.LightGray);
                m.setFloat("Shininess", 128);  // [0,128]

                g.setMaterial(m);
                rootNode.attachChild(g);   
            }
        }
//        com.jme3.scene.shape.Box boxMesh = new com.jme3.scene.shape.Box((float) 10 / 2, (float) 10 / 2, (float) 10 / 2);
//        Geometry boxGeo = new Geometry("Shiny rock", boxMesh);
//        boxGeo.setLocalTranslation((float) 0, (float) 0, (float) 0);
//        TangentBinormalGenerator.generate(boxMesh);
//        Geometry g = boxGeo;
//                Material m = new Material(assetManager,
//                        "Common/MatDefs/Light/Lighting.j3md");
//
//                m.setBoolean("UseMaterialColors", true);
//                m.setColor("Diffuse", ColorRGBA.LightGray);
//                m.setColor("Specular", ColorRGBA.LightGray);
//                m.setFloat("Shininess", 128);  // [0,128]
//
//                g.setMaterial(m);
//                rootNode.attachChild(g);

//        if (MainGUI.ShapeJList.getSelectedValue() != null) {
//            rootNode.detachAllChildren();
//            drawAxis();
//            for (int i = 0; i < MainGUI.allShapes.size(); i++) {
//                Shape s = MainGUI.allShapes.get(i);
//                Geometry g = s.draw();
//                Material m = new Material(assetManager,
//                        "Common/MatDefs/Light/Lighting.j3md");
//
//                m.setBoolean("UseMaterialColors", true);
//                m.setColor("Diffuse", ColorRGBA.LightGray);
//                m.setColor("Specular", ColorRGBA.LightGray);
//                m.setFloat("Shininess", 128f);  // [0,128]
//
//                if (i == MainGUI.ShapeJList.getSelectedIndex()) {
//                    m = new Material(assetManager,
//                            "Common/MatDefs/Light/Lighting.j3md");
//
//                    m.setBoolean("UseMaterialColors", true);
//                    m.setColor("Diffuse", ColorRGBA.Orange);
//                    m.setColor("Specular", ColorRGBA.Orange);
//                    m.setFloat("Shininess", 128f);  // [0,128]
//
//                }
//                g.setMaterial(m);
//                rootNode.attachChild(g);
//
//            }
//        }
//        if (!MainGUI.allShapes.isEmpty() && MainGUI.ShapeJList.getSelectedValue() == null) {
//            rootNode.detachAllChildren();
//            drawAxis();
//            for (int i = 0; i < MainGUI.allShapes.size(); i++) {
//                Shape s = MainGUI.allShapes.get(i);
//                Geometry g = s.draw();
//                Material m = new Material(assetManager,
//                        "Common/MatDefs/Light/Lighting.j3md");
//
//                m.setBoolean("UseMaterialColors", true);
//                m.setColor("Diffuse", ColorRGBA.LightGray);
//                m.setColor("Specular", ColorRGBA.LightGray);
//                m.setFloat("Shininess", 128);  // [0,128]
//
//                g.setMaterial(m);
//                rootNode.attachChild(g);
//            }
//        }
        firstload = false;
    }

    private void createSceneGraph() {
        com.jme3.math.Vector3f lookAt = new com.jme3.math.Vector3f(0, 0, 0);
        customCam.setLookAt(lookAt);
        customCam.setXYView(true);

    }

    private void drawAxis() {
        com.jme3.scene.shape.Line x = new com.jme3.scene.shape.Line(new com.jme3.math.Vector3f(0, 0, 0), new com.jme3.math.Vector3f(2, 0, 0));
        com.jme3.scene.shape.Line y = new com.jme3.scene.shape.Line(new com.jme3.math.Vector3f(0, 0, 0), new com.jme3.math.Vector3f(0, 2, 0));
        com.jme3.scene.shape.Line z = new com.jme3.scene.shape.Line(new com.jme3.math.Vector3f(0, 0, 0), new com.jme3.math.Vector3f(0, 0, 2));
        x.setLineWidth(4);
        y.setLineWidth(4);
        z.setLineWidth(4);
        Geometry gx = new Geometry("Shiny Rock", x);
        Geometry gy = new Geometry("Shiny Rock", y);
        Geometry gz = new Geometry("Shiny Rock", z);
        Material m = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");

        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.Red);
        m.setColor("Specular", ColorRGBA.Red);
        m.setFloat("Shininess", 0);  // [0,128]

        gx.setMaterial(m);
        gy.setMaterial(m);
        gz.setMaterial(m);
        rootNode.attachChild(gx);
        rootNode.attachChild(gy);
        rootNode.attachChild(gz);
    }

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
