package net.java.joglutils.model;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Quaternion;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.Logging;
import javax.media.opengl.GL;

import net.java.joglutils.model.examples.DisplayListRenderer;
import net.java.joglutils.model.geometry.Model;

/**
 * @author R.Wathelet, most of the code is from RodgersGB Model3DLayer class
 * see https://joglutils.dev.java.net/
 * modified by eterna2
 * modified by R.Wathelet adding the Adjustable
 * modified by Cride5, allow setting of initial roll/pitch/yaw, and distance-based clipping
 */
public class Movable3DModel implements Renderable, Movable {

    private Position position;
    private Model model;
    private double defyaw = 0.0;
    private double defroll = 0.0;
    private double defpitch = 0.0;
    private double yaw = 0.0;
    private double roll = 0.0;
    private double pitch = 0.0;
    private boolean keepConstantSize = true;
    private Vec4 referenceCenterPoint;
    private Globe globe;
    private double size = 1;
    private double clipDist = Double.MAX_VALUE;

    public Movable3DModel(Model model, Position pos) {
        this.model = model;
        this.setPosition(pos);
        this.model.setUseLighting(false);
        this.model.setUseTexture(false);
    }

    public Movable3DModel(String path, Position pos) {
        try {
            this.model = ModelFactory.createModel(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setPosition(pos);
        this.model.setUseLighting(false);
        this.model.setUseTexture(true);
    }

    public Movable3DModel(String path, Position pos, double size, double clipDist, double defroll, double defpitch, double defyaw) {
        this(path, pos);
        this.setSize(size);
        this.defroll = defroll;
        this.defpitch = defpitch;
        this.defyaw = defyaw;
        this.clipDist = clipDist;
        setYaw(0);
        setRoll(0);
        setPitch(0);
    }

    @Override
    public void render(DrawContext dc) {
        this.computeReferenceCenter(dc);
        if (dc == null) {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalStateException(message);
        }
        try {
            beginDraw(dc);
            dc.setDeepPickingEnabled(true);
            if (dc.isPickingMode()) {
                this.model.setRenderPicker(true);
            } else {
                this.model.setRenderPicker(false);
            }


            draw(dc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endDraw(dc);
        }
    }

    protected void draw(DrawContext dc) {
        this.globe = dc.getGlobe();
        Vec4 loc = dc.getGlobe().computePointFromPosition(this.getPosition());
        // Only render the object if it is within the clip distance and viewing fustrum
        if (loc.distanceTo3(dc.getView().getEyePoint()) > clipDist
                || !dc.getView().getFrustumInModelCoordinates().contains(loc)) {
//System.out.println("dist:" + loc.distanceTo3(dc.getView().getEyePoint()) + " clipDist:" + clipDist + " infust:" + dc.getView().getFrustumInModelCoordinates().contains(loc));
            return;
        }

        GL gl = dc.getGL();
        double localSize = this.computeSize(dc, loc);
        this.computeReferenceCenter(dc);

        dc.getView().pushReferenceCenter(dc, loc);
        gl.glRotated(position.getLongitude().degrees, 0, 1, 0);
        gl.glRotated(-position.getLatitude().degrees, 1, 0, 0);
        gl.glRotated(yaw, 0, 0, 1);
        gl.glRotated(pitch, 1, 0, 0);
        gl.glRotated(roll, 0, 1, 0);
        gl.glScaled(localSize, localSize, localSize);
        DisplayListRenderer.getInstance().render(gl, this.getModel());
        dc.getView().popReferenceCenter(dc);
    }

    // puts opengl in the correct state for this layer
    protected void beginDraw(DrawContext dc) {
        GL gl = dc.getGL();
        Vec4 cameraPosition = dc.getView().getEyePoint();
        gl.glPushAttrib(
                GL.GL_TEXTURE_BIT
                | GL.GL_COLOR_BUFFER_BIT
                | GL.GL_DEPTH_BUFFER_BIT
                | GL.GL_HINT_BIT
                | GL.GL_POLYGON_BIT
                | GL.GL_ENABLE_BIT
                | GL.GL_CURRENT_BIT
                | GL.GL_LIGHTING_BIT
                | GL.GL_TRANSFORM_BIT);
        //float[] lightPosition = {0F, 100000000f, 0f, 0f};

        float[] lightPosition = {(float) (cameraPosition.x + 1000), (float) (cameraPosition.y + 1000), (float) (cameraPosition.z + 1000), 1.0f};
        /** Ambient light array */
        float[] lightAmbient = {0.4f, 0.4f, 0.4f, 0.4f};
        /** Diffuse light array */
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        /** Specular light array */
        float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] model_ambient = {0.5f, 0.5f, 0.5f, 1.0f};

        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, model_ambient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightSpecular, 0);
        gl.glDisable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_NORMALIZE);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
    }

    // resets opengl state
    protected void endDraw(DrawContext dc) {
        GL gl = dc.getGL();
        gl.glMatrixMode(javax.media.opengl.GL.GL_MODELVIEW);
        gl.glPopMatrix();
        gl.glPopAttrib();
    }

    @Override
    public Position getReferencePosition() {
        return this.getPosition();
    }

    @Override
    public void move(Position delta) {
        if (delta == null) {
            String msg = Logging.getMessage("nullValue.PositionDeltaIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.moveTo(this.getReferencePosition().add(delta));
    }

    @Override
    public void moveTo(Position position) {
        if (position == null) {
            String msg = Logging.getMessage("nullValue.PositionIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (globe != null) {
            Vec4 newRef = this.globe.computePointFromPosition(position);
            Angle distance = LatLon.greatCircleDistance(this.getPosition(), position);
            Vec4 axis = this.referenceCenterPoint.cross3(newRef).normalize3();
            Vec4 p = this.globe.computePointFromPosition(this.getPosition());
            p = p.transformBy3(Quaternion.fromAxisAngle(distance, axis));
            this.position = this.globe.computePositionFromPoint(p);
        }
    }

    private void computeReferenceCenter(DrawContext dc) {
        this.referenceCenterPoint = this.computeTerrainPoint(dc,
                this.getPosition().getLatitude(), this.getPosition().getLongitude());
    }

    private Vec4 computeTerrainPoint(DrawContext dc, Angle lat, Angle lon) {
        Vec4 p = dc.getSurfaceGeometry().getSurfacePoint(lat, lon);
        if (p == null) {
            p = dc.getGlobe().computePointFromPosition(lat, lon,
                    dc.getGlobe().getElevation(lat, lon) * dc.getVerticalExaggeration());
        }
        return p;
    }

    private double computeSize(DrawContext dc, Vec4 loc) {
        if (this.keepConstantSize) {
            return size;
        }
        if (loc == null) {
            System.err.println("Null location when computing size of model");
            return 1;
        }
        double d = loc.distanceTo3(dc.getView().getEyePoint());
        double newSize = 60 * dc.getView().computePixelSizeAtDistance(d);
        if (newSize < 2) {
            newSize = 2;
        }
        return newSize;
    }

    public boolean isConstantSize() {
        return keepConstantSize;
    }

    public void setKeepConstantSize(boolean val) {
        this.keepConstantSize = val;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Model getModel() {
        return model;
    }

    public void setYaw(double val) {
        this.yaw = val + defyaw;
    }

    public void setRoll(double val) {
        this.roll = val + defroll;
    }

    public void setPitch(double val) {
        this.pitch = val + defpitch;
    }
}
