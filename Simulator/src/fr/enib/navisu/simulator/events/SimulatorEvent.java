/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.simulator.events;

import gov.nasa.worldwind.geom.Position;
import java.util.EventObject;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 22/05/2012
 */
public class SimulatorEvent extends EventObject {
    
    private Position oldPosition;
    private Position newPosition;
    private boolean isLastPosition;

    public SimulatorEvent(Object source) {
        this(source, Position.ZERO, Position.ZERO, false);
    }

    public SimulatorEvent(Object source, Position oldPosition, Position newPosition, boolean isLastPosition) {
        super(source);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.isLastPosition = isLastPosition;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Position getNewPosition() {
        return newPosition;
    }
    
    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }
    
    public Position getOldPosition() {
        return oldPosition;
    }
    
    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public boolean isLastPosition() {
        return isLastPosition;
    }

    public void setIsLastPosition(boolean isLastPosition) {
        this.isLastPosition = isLastPosition;
    }
    //</editor-fold>
}
