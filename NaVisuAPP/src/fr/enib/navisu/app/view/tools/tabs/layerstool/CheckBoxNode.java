/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */
package fr.enib.navisu.app.view.tools.tabs.layerstool;

import gov.nasa.worldwind.layers.Layer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 24/05/2012
 */
public class CheckBoxNode implements ItemListener {

    private String value;
    private Layer layer;
    private boolean isEnable;
    private JCheckBox checkbox;

    public CheckBoxNode(Layer l) {

        isEnable = l.isEnabled();
        this.value = l.getName();
        layer = l;
        checkbox = new JCheckBox(value, isEnable);
        checkbox.addItemListener(this);
    }

    public String getValue() {
        return value;
    }

    public Layer getLayer() {
        return layer;
    }

    public boolean isEnabled() {
        return isEnable;
    }

    public void setIsEnable(boolean b) {
        layer.setEnabled(b);
        isEnable = b;
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(JCheckBox checkbox) {
        this.checkbox = checkbox;
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        setIsEnable(!isEnable);
    }
}
