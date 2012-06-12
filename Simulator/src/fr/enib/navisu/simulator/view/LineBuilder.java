/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.simulator.view;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 01/06/2012
 */
public class LineBuilder extends AVListImpl {

    private final WorldWindow wwd;
    private boolean armed = false;
    private ArrayList<Position> positions = new ArrayList<>();
    private RenderableLayer layer;
    private Polyline line;
    private boolean active = false;

    public LineBuilder(final WorldWindow wwd) {
        this.wwd = wwd;
        line = new Polyline();
        line.setFollowTerrain(true);
        
        
        layer = new RenderableLayer();
        layer.setName("[LineBuilder] Polyline");
        layer.addRenderable(line);
        
        this.wwd.getModel().getLayers().add(this.layer);
        this.wwd.getInputHandler().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                checkIfLayerExists();
                if (armed && mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    if (armed && (mouseEvent.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        if (!mouseEvent.isControlDown()) {
                            active = true;
                            addPosition();
                        }
                    }
                    mouseEvent.consume();
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if (armed && mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    if (positions.size() == 1) {
                        removePosition();
                    }
                    active = false;
                    mouseEvent.consume();
                }
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (armed && mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    if (mouseEvent.isControlDown()) {
                        removePosition();
                    }
                    mouseEvent.consume();
                }
            }
        });
        this.wwd.getInputHandler().addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if (armed && (mouseEvent.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                    if (active) {
                        mouseEvent.consume();
                    }
                }
            }
        });
        this.wwd.addPositionListener(new PositionListener() {

            @Override
            public void moved(PositionEvent event) {
                if (!active) {
                    return;
                }

                if (positions.size() == 1) {
                    addPosition();
                } else {
                    replacePosition();
                }
            }
        });
        
        checkIfLayerExists();
    }

    public RenderableLayer getLayer() {
        return this.layer;
    }

    public Polyline getLine() {
        return this.line;
    }

    public void clear() {
        /*while (this.positions.size() > 0) {
            this.removePosition();
        }*/
        if(!positions.isEmpty()) { // Optimisation perso
            positions.clear();
            line.setPositions(positions);
            wwd.redraw();
        }
    }

    public boolean isArmed() {
        return this.armed;
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }

    public void setPositions(List<Position> positions) {
        
        checkIfLayerExists();
        
        clear();
        this.positions.addAll(positions);
        this.line.setPositions(this.positions);
        this.firePropertyChange("LineBuilder.AddPosition", null, positions);
        this.wwd.redraw();
    }
    
    private void addPosition() {
        Position curPos = this.wwd.getCurrentPosition();
        if (curPos == null) {
            return;
        }

        this.positions.add(curPos);
        this.line.setPositions(this.positions);
        this.firePropertyChange("LineBuilder.AddPosition", null, curPos);
        this.wwd.redraw();
    }

    private void replacePosition() {
        Position curPos = this.wwd.getCurrentPosition();
        if (curPos == null) {
            return;
        }

        int index = this.positions.size() - 1;
        if (index < 0) {
            index = 0;
        }

        Position currentLastPosition = this.positions.get(index);
        this.positions.set(index, curPos);
        this.line.setPositions(this.positions);
        this.firePropertyChange("LineBuilder.ReplacePosition", currentLastPosition, curPos);
        this.wwd.redraw();
    }

    public void removePosition() {
        if (this.positions.isEmpty()) {
            return;
        }

        Position currentLastPosition = this.positions.get(this.positions.size() - 1);
        this.positions.remove(this.positions.size() - 1);
        this.line.setPositions(this.positions);
        this.firePropertyChange("LineBuilder.RemovePosition", currentLastPosition, null);
        this.wwd.redraw();
    }
    
    private void checkIfLayerExists() {
        if(!wwd.getModel().getLayers().contains(layer)) {
            wwd.getModel().getLayers().add(layer);
            wwd.redraw();
        }
    }
    
    public int size() {
        return positions.size();
    }
}
