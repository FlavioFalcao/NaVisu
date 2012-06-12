/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.simulator.events;

import java.util.EventListener;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 22/05/2012
 */
public interface SimulatorEventListener extends EventListener {
    
    void updatePosition(SimulatorEvent event);
}
