/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.charts.view.renderables;

import fr.enib.navisu.charts.view.WWChart;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.SurfacePolygon;
import java.util.List;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 07/06/2012
 */
public class WWPolygon extends SurfacePolygon {

    private final WWChart owner;
    
    public WWPolygon(final WWChart owner, List<LatLon> coordinates) {
        super(coordinates);
        this.owner = owner;
    }

    /**
     * @return the owner
     */
    public WWChart getOwner() {
        return owner;
    }
}
