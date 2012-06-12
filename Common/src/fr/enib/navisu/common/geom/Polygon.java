/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.common.geom;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 02/05/2012
 */
public interface Polygon {

    /**
     * 
     * @param location
     */
    void addCoordinate(Point2D location);
    /**
     * 
     * @param locations
     */
    void setCoordinates(Point2D[] locations);
    /**
     * 
     * @param locations
     */
    void setCoordinates(List<? extends Point2D> locations);
    /**
     * 
     * @param index
     * @return
     */
    Point2D getCoordinate(int index);
    /**
     * 
     * @return
     */
    List<Point2D> getCoordinates();
    
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    boolean contains(double x, double y);
    /**
     * 
     * @param other
     * @return
     */
    boolean contains(Polygon other);
    /**
     * 
     * @param other
     * @return
     */
    boolean intersects(Polygon other);
    /**
     * 
     * @param other
     * @return
     */
    Polygon intersection(Polygon other);
    /**
     * 
     * @param other
     * @return
     */
    Polygon difference(Polygon other);
    /**
     * 
     * @param other
     * @return
     */
    Polygon union(Polygon other);
}
