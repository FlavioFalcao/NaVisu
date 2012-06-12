/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */
package fr.enib.navisu.common.utils;

import com.vividsolutions.jts.geom.Coordinate;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Thibault PENSEC & Jordan MENS @date 15/04/2012
 */
public class WWUtils {

    public static final String WWJ_DEFAULT_CACHE = WorldWind.getDataFileStore().getWriteLocation().getAbsolutePath();
    
    public static List<LatLon> point2DToLatLon(List<? extends Point2D> in) {
        List<LatLon> lalons = new ArrayList<>(in.size());
        for(Point2D c : in) {
            lalons.add(LatLon.fromDegrees(c.getX(), c.getY()));
        }
        return lalons;
    }

    public static List<LatLon> coordinatesToLatLon(List<? extends Coordinate> in) {
        List<LatLon> lalons = new ArrayList<>(in.size());
        for (Coordinate c : in) {
            lalons.add(LatLon.fromDegrees(c.x, c.y));
        }
        return lalons;
    }

    public static void insertBeforeCompass(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition, layer);
    }

    public static void insertBeforePlacenames(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition, layer);
    }

    public static void insertAfterPlacenames(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just after the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition + 1, layer);
    }

    public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName) {
        // Insert the layer into the layer list just before the target layer.
        int targetPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l.getName().indexOf(targetName) != -1) {
                targetPosition = layers.indexOf(l);
                break;
            }
        }
        layers.add(targetPosition, layer);
    }

    public static void removeLayer(WorldWindow wwd, Layer layer) {
        wwd.getModel().getLayers().remove(layer);
    }
    
    public static boolean containsLayer(WorldWindow wwd, Layer layer) {

        if (layer != null) {
            return wwd.getModel().getLayers().contains(layer);
        }

        return false;
    }
    
    public static void goTo(WorldWindow wwd, Sector sector) {
        if (wwd == null) {
            String message = "WorldWindow can't be null";
            throw new IllegalArgumentException(message);
        }

        if (sector == null) {
            String message = "Sector can't be null";
            throw new IllegalArgumentException(message);
        }

        Box extent = Sector.computeBoundingBox(wwd.getModel().getGlobe(),
                wwd.getSceneController().getVerticalExaggeration(), sector);
        Angle fov = wwd.getView().getFieldOfView();
        double zoom = extent.getRadius() / fov.cosHalfAngle() / fov.tanHalfAngle();

        wwd.getView().goTo(new Position(sector.getCentroid(), 0d), zoom);
    }
    
    public static void setEyePosition(WorldWindow wwd, Position pos) {
        setEyePosition(wwd, pos, false);
    }   
    
    public static void setEyePosition(WorldWindow wwd, Position pos, boolean animated) {
        if(animated) {
            wwd.getView().goTo(pos, pos.elevation);
        } else {
            wwd.getView().setEyePosition(pos);
        }
    }

    private WWUtils(){}
}
