/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.common.utils.WWUtils;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Sector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 15/05/2012
 */
public class GotoChartNodeAction extends AbstractAction {

    private Chart chart;
    private static WorldWindowGLCanvas wwd = (WorldWindowGLCanvas) AppTopComponent.wwd();

    public GotoChartNodeAction(Chart kapChart) {
        this.chart = kapChart;
        initialize();
    }

    private void initialize() {
        putValue(NAME, "Goto...");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Sector sector = Sector.boundingSector(WWUtils.point2DToLatLon(chart.getPolygon().getCoordinates()));
        WWUtils.goTo(wwd, sector);
    }
}
