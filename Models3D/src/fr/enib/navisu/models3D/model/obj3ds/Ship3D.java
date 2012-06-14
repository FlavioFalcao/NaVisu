/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.models3D.model.obj3ds;


import net.java.joglutils.model.Movable3DModel;

/**
 *
 * @author Serge Morvan
 */
public class Ship3D {


    private Movable3DModel movable3DModel;
    private Ship ship;

    /**
     * Get the value of ship
     *
     * @return the value of ship
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Set the value of ship
     *
     * @param ship new value of ship
     */
    public void setShip(Ship ship) {
        this.ship = ship;
    }



    public Ship3D(Ship ship, Movable3DModel movable3DModel) {
        this.ship = ship;
        this.movable3DModel = movable3DModel;
    }

    /**
     * Get the value of movable3DModel
     *
     * @return the value of movable3DModel
     */
    public Movable3DModel getMovable3DModel() {
        return movable3DModel;
    }

    /**
     * Set the value of movable3DModel
     *
     * @param movable3DModel new value of movable3DModel
     */
    public void setMovable3DModel(Movable3DModel movable3DModel) {
        this.movable3DModel = movable3DModel;
    }
}
