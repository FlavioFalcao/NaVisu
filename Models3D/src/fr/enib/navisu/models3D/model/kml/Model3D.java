/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.models3D.model.kml;

import fr.enib.navisu.models3D.controller.Behavior;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Serge
 */
public class Model3D {

    private KMLRoot kmlRoot;
    private KMLController kmlController;
    private String name;
    private Behavior behavior;

    

    public Model3D(String name) {
        this.name = name;
        try {
            kmlRoot = KMLRoot.createAndParse(new File(name));
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(Model3D.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        kmlController = new KMLController(kmlRoot);
    }

    /**
     * Get the value of kmlController
     *
     * @return the value of kmlController
     */
    public KMLController getKmlController() {
        return kmlController;
    }

    /**
     * Set the value of kmlController
     *
     * @param kmlController new value of kmlController
     */
    public void setKmlController(KMLController kmlController) {
        this.kmlController = kmlController;
    }

    /**
     * Get the value of behavior
     *
     * @return the value of behavior
     */
    public Behavior getBehavior() {
        return behavior;
    }

    /**
     * Set the value of behavior
     *
     * @param behavior new value of behavior
     */
    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of kmlRoot
     *
     * @return the value of kmlRoot
     */
    public KMLRoot getKmlRoot() {
        return kmlRoot;
    }

    /**
     * Set the value of kmlRoot
     *
     * @param kmlRoot new value of kmlRoot
     */
    public void setKmlRoot(KMLRoot kmlRoot) {
        this.kmlRoot = kmlRoot;
    }
}

