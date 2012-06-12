/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */
package fr.enib.navisu.app.view.tools.tabs.layerstool;

import gov.nasa.worldwind.layers.Layer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 24/05/2012
 */
public class ListenerItem implements ItemListener{

    private static final Logger LOGGER = Logger.getLogger(ListenerItem.class.getName());
    private CheckBoxNode checkBoxNode;
    
    public ListenerItem(CheckBoxNode cbn) {
        checkBoxNode = cbn;
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        Layer l = checkBoxNode.getLayer();
        checkBoxNode.setIsEnable(!checkBoxNode.isEnabled());
    }
}
