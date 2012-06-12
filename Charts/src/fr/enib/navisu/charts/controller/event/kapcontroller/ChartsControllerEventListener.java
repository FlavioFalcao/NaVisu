/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.charts.controller.event.kapcontroller;

import fr.enib.navisu.charts.view.WWChart;
import gov.nasa.worldwind.event.SelectEvent;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 23/04/2012
 */
public interface ChartsControllerEventListener {

    void selected(SelectEvent se, WWChart wwChart);
}
