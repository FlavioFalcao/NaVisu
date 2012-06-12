package fr.enib.navisu.charts.controller.xml.adapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * 
 * @author Jordan Mens & Thibault Pensec
 * @date 29 mars 2012
 */
@XmlRootElement
public class LatLonType {
    
    private double lat, lon;

    public LatLonType() {
    }

    public LatLonType(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @XmlAttribute(name="lat")
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    @XmlAttribute(name="lon")
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
    
    
}
