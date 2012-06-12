package fr.enib.navisu.charts.controller.xml.adapter;

import gov.nasa.worldwind.geom.LatLon;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec & Jordan Mens
 * @date 29 mars 2012
 */
public class LatLonAdapter extends XmlAdapter<LatLonType, LatLon> {

    @Override
    public LatLonType marshal(LatLon v) {
        return new LatLonType(v.asDegreesArray()[0], v.asDegreesArray()[1]);
    }
    
    @Override
    public LatLon unmarshal(LatLonType v) {
        return LatLon.fromDegrees(v.getLat(), v.getLon());
    }
}
