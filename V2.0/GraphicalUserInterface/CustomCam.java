package GraphicalUserInterface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Controls: - Move the mouse to rotate the camera - Mouse wheel for zooming in
 * or out
 */
public class CustomCam implements AnalogListener, ActionListener {

//    private ViewCanvas vc = null;
    private final Camera cam;
    private Vector3f upVec;
    private Vector3f lookAt;
    private Vector3f centroid;
    private float rotationSpeed = 1f;
    private float moveSpeed = 1f;
    private float zoomSpeed = 1f;
    private InputManager inputManager;
    private float distance = 2.0f;
    private Vector3f pos = new Vector3f();

    //==========================================================================
    /**
     * Creates a new FlyByCamera to control the given Camera object.
     *
     * @param cam
     */
    public CustomCam(Camera cam) {
        this.cam = cam;
        upVec = cam.getUp().clone();
        pos = cam.getLocation();
        resetZoom();
    }

    //==========================================================================
    /**
     * Sets the point to look at for the camera
     *
     * @param lookAt
     */
    public void setLookAt(Vector3f lookAt) {
        this.lookAt = lookAt;
        centroid = lookAt.clone();
    }

    //==========================================================================
    public void setDistance(float dist) {
        distance = dist;
        L = (float) Math.sqrt(distance * distance / 3.0);
    }

    //==========================================================================
    /**
     * Sets the move speed. The speed is given in world units per second.
     *
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    //==========================================================================
    /**
     * Gets the move speed. The speed is given in world units per second.
     *
     * @return moveSpeed
     */
    public float getMoveSpeed() {
        return moveSpeed;
    }

    //==========================================================================
    /**
     * Sets the rotation speed.
     *
     * @param rotationSpeed
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    //==========================================================================
    /**
     * Gets the move speed. The speed is given in world units per second.
     *
     * @return rotationSpeed
     */
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    //==========================================================================
    /**
     * Sets the zoom speed.
     *
     * @param zoomSpeed
     */
    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    //==========================================================================
    /**
     * Gets the zoom speed. The speed is a multiplier to increase/decrease the
     * zoom rate.
     *
     * @return zoomSpeed
     */
    public float getZoomSpeed() {
        return zoomSpeed;
    }



    //==========================================================================
    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     *
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager) {
        this.inputManager = inputManager;
        inputManager.addMapping("Ctrl",
                new KeyTrigger(KeyInput.KEY_LCONTROL), // trigger 1: Left Control Key
                new KeyTrigger(KeyInput.KEY_RCONTROL)); // trigger 2: Right Control Key
        inputManager.addListener(ctrlAltShift, "Ctrl");
        inputManager.addMapping("Shift",
                new KeyTrigger(KeyInput.KEY_LSHIFT), // trigger 1: Left Shift Key
                new KeyTrigger(KeyInput.KEY_RSHIFT)); // trigger 2: Right Shift Key
        inputManager.addListener(ctrlAltShift, "Shift");
        inputManager.addMapping("Alt",
                new KeyTrigger(KeyInput.KEY_LMENU), // trigger 1: Left Alt Key
                new KeyTrigger(KeyInput.KEY_RMENU)); // trigger 2: Right Alt Key
        inputManager.addListener(ctrlAltShift, "Alt");

        // Arrow keys
        inputManager.addMapping("LeftPan",
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addListener(this, "LeftPan");
        inputManager.addMapping("RightPan",
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "RightPan");
        inputManager.addMapping("UpPan",
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addListener(this, "UpPan");
        inputManager.addMapping("DownPan",
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(this, "DownPan");

        // Mouse Buttons
        inputManager.addMapping("LeftMouse",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "LeftMouse");
        inputManager.addMapping("MiddleMouse",
                new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(this, "MiddleMouse");
        inputManager.addMapping("RightMouse",
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "RightMouse");

        // Mouse movement
        inputManager.addMapping("R_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addListener(this, "R_Left");
        inputManager.addMapping("R_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addListener(this, "R_Right");
        inputManager.addMapping("R_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(this, "R_Up");
        inputManager.addMapping("R_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addListener(this, "R_Down");

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(this, "ZoomIn");
        inputManager.addMapping("ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(this, "ZoomOut");
    } // registerWithInput

    //==========================================================================
    @Override
    public void onAnalog(String name, float value, float tpf) {
        float rot = rotationSpeed * value;
        float move = moveSpeed * value;
        if (name.equals("R_Left") && (midMouse || (shift && leftMouse))) {
            rotateRt(rot);
        }
        if (name.equals("R_Right") && (midMouse || (shift && leftMouse))) {
            rotateRt(-rot);
        }
        if (name.equals("R_Up") && (midMouse || (shift && leftMouse))) {
            rotateUp(-rot);
        }
        if (name.equals("R_Down") && (midMouse || (shift && leftMouse))) {
            rotateUp(rot);
        }
        if (name.equals("R_Left") && (rightMouse || (ctrl && leftMouse))) {
            moveCamera(-move);
        }
        if (name.equals("R_Right") && (rightMouse || (ctrl && leftMouse))) {
            moveCamera(move);
        }
        if (name.equals("R_Up") && (rightMouse || (ctrl && leftMouse))) {
            riseCamera(-move);
        }
        if (name.equals("R_Down") && (rightMouse || (ctrl && leftMouse))) {
            riseCamera(move);
        }
        if (name.equals("R_Down") && (alt && leftMouse)) {
            zoomCamera(15 * value);
        }
        if (name.equals("R_Up") && (alt && leftMouse)) {
            zoomCamera(-15 * value);
        }
        if (name.equals("ZoomIn")) {
            zoomCamera(value);
        }
        if (name.equals("ZoomOut")) {
            zoomCamera(-value);
        }
    } // onAnalog

    //==========================================================================
    private boolean midMouse = false;
    private boolean leftMouse = false;
    private boolean rightMouse = false;
    private boolean ctrl = false;
    private boolean shift = false;
    private boolean alt = false;

    //==========================================================================
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        try {
            if (name.equals("LeftMouse") && keyPressed && !shift && !alt) {
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);
//                vc.leftMouseClicked(ctrl, keyPressed, ray);
            }
            if (name.equalsIgnoreCase("LeftPan")) {
                moveCamera(-0.01f);
            }
            if (name.equalsIgnoreCase("RightPan")) {
                moveCamera(0.01f);
            }
            if (name.equalsIgnoreCase("UpPan")) {
                riseCamera(0.01f);
            }
            if (name.equalsIgnoreCase("DownPan")) {
                riseCamera(-0.01f);
            }
            leftMouse = name.equals("LeftMouse") && keyPressed;
            midMouse = name.equals("MiddleMouse") && keyPressed;
            rightMouse = name.equals("RightMouse") && keyPressed;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // onAction

    //==========================================================================
    private final ActionListener ctrlAltShift = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            ctrl = name.equals("Ctrl") && keyPressed;
            shift = name.equals("Shift") && keyPressed;
            alt = name.equals("Alt") && keyPressed;
        }
    };

    //==========================================================================
    protected void zoomCamera(float value) {
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;
        float near = cam.getFrustumNear();
        float fovY = FastMath.atan(h / near)
                / (FastMath.DEG_TO_RAD * .5f);
        float newFovY = fovY + value * 0.1f * zoomSpeed;
        if (newFovY > 0f) {
            // Don't let the FOV go zero or negative.
            fovY = newFovY;
        }
        h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;
        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    private final Vector3f vel = new Vector3f();

    //==========================================================================
    protected void riseCamera(float value) {
        cam.getUp(vel);
        pos = cam.getLocation().clone();
        vel.multLocal(value * moveSpeed);
        pos.addLocal(vel);
        lookAt.addLocal(vel);
        cam.setLocation(pos);
    }

    //==========================================================================
    protected void moveCamera(float value) {
        cam.getLeft(vel);
        pos = cam.getLocation().clone();
        vel.multLocal(value * moveSpeed);
        pos.addLocal(vel);
        lookAt.addLocal(vel);
        cam.setLocation(pos);
    }

    private final Quaternion quat = new Quaternion();
    //==========================================================================
    private void rotateRt(float rot) {
        quat.set(Quaternion.IDENTITY);
        quat.fromAngleAxis(rot, upVec);
        pos = cam.getLocation();
        pos.subtract(lookAt, pos);
        pos = quat.mult(pos);
        pos.add(lookAt, pos);
        cam.setLocation(pos);
        cam.lookAt(lookAt, upVec);
    }

    //==========================================================================
    private void rotateUp(float rot) {
        pos = cam.getLocation();
        pos.subtract(lookAt, pos);
        Vector3f norm = pos.cross(upVec);
        quat.fromAngleAxis(rot, norm);
        pos = cam.getLocation();
        pos = quat.mult(pos);
        upVec = quat.mult(upVec);
        pos.add(lookAt, pos);
        cam.setLocation(pos);
        cam.lookAt(lookAt, upVec);
    }

    //==========================================================================
    public void setXYView(boolean posDir) {
        float dist = distance;
        if (!posDir) {
            dist = -dist;
        }
        lookAt = centroid.clone();
        cam.setLocation(new Vector3f(lookAt.x, lookAt.y, lookAt.z + dist));
        pos = cam.getLocation();
        cam.lookAt(lookAt, Vector3f.UNIT_Y);
        upVec = Vector3f.UNIT_Y;
        resetZoom();
    }

    //==========================================================================
    public void setXZView(boolean posDir) {
        float dist = distance;
        if (!posDir) {
            dist = -dist;
        }
        lookAt = centroid.clone();
        cam.setLocation(new Vector3f(lookAt.x, lookAt.y + dist, lookAt.z));
        pos = cam.getLocation();
        cam.lookAt(lookAt, Vector3f.UNIT_Z);
        upVec = Vector3f.UNIT_Z;
        resetZoom();
    }

    //==========================================================================
    public void setYZView(boolean posDir) {
        float dist = distance;
        if (!posDir) {
            dist = -dist;
        }
        lookAt = centroid.clone();
        cam.setLocation(new Vector3f(lookAt.x + dist, lookAt.y, lookAt.z));
        pos = cam.getLocation();
        cam.lookAt(lookAt, Vector3f.UNIT_Y);
        upVec = Vector3f.UNIT_Y;
        resetZoom();
    }

    //======================================================
    // Set XYZ View / Isometric View
    //======================================================
    private float L = 1.155f;
    protected Vector3f[] CamD = {
        new Vector3f(L, L, L), new Vector3f(L, L, -L),
        new Vector3f(-L, L, -L), new Vector3f(-L, L, L),
        new Vector3f(L, -L, L), new Vector3f(L, -L, -L),
        new Vector3f(-L, -L, -L), new Vector3f(-L, -L, L)
    };

    //==========================================================================
    public void setXYZView(int dir) {
        lookAt = centroid.clone();
        cam.setLocation(new Vector3f(CamD[dir].x + lookAt.x, CamD[dir].y + lookAt.y, CamD[dir].z + lookAt.z));
        pos = cam.getLocation();
        cam.lookAt(lookAt, Vector3f.UNIT_Y);
        upVec = Vector3f.UNIT_Y;
        resetZoom();
    }

    //==========================================================================
    private void resetZoom() {
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;
        cam.setFrustumPerspective(30, aspect, 0.1f, 100f);
    }
} // end CustomCam
