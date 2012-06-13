/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.charts.view;

import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.model.kap.worldwind.WWKAPLayerInfo;
import fr.enib.navisu.charts.view.layers.ChartTiledImageLayer;
import fr.enib.navisu.charts.view.renderables.WWPolygon;
import fr.enib.navisu.common.utils.WWUtils;
import fr.enib.navisu.common.xml.XMLReader;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 07/06/2012
 */
public class WWChart {

    private static final Logger LOGGER = Logger.getLogger(WWChart.class.getName());

    private final WorldWindow wwd;
    
    private boolean isTiled, isVisible;
    private final Chart model;
    // TiledImageLayer
    private ChartTiledImageLayer tiledImageLayerView;
    // Polygon
    private WWPolygon polygonView;
    private ShapeAttributes normalAttributes;
    private ShapeAttributes highlightAttributes;
    
    public static final double MIN_OPACITY = 0.00001;
    public static final double MAX_OPACITY = 1d;
    public static final double DEFAULT_OPACITY = 0.1;
    public static final double DIFF_OPACITY = 0.02999;
    
    private double opacity = DEFAULT_OPACITY;
    private double oldOpacity;
    
    private static final String XML_FILE_NAME = "info.xml";
    
    public WWChart(final WorldWindow wwd, final Chart orig) {
        this.wwd = wwd;
        this.model = orig;
        initialize();
    }
    
    private void initialize() {
        
        String layerCache = findCacheLocation();
        isTiled = isVisible = false;
        
        if(layerCache != null) {
            WWKAPLayerInfo info = loadLayerInfo(layerCache);
            if(info != null) {
                isTiled = true;
                
                tiledImageLayerView = new ChartTiledImageLayer(
                        info.getChartID(),  
                        info.getNumEmptyLevels(), 
                        info.getNumLevels(), 
                        info.getFormatSuffix()
                );
            }
        }
        
        makePolygon();
    }
    
    private String findCacheLocation() {
        Path cache = Paths.get(WWUtils.WWJ_DEFAULT_CACHE, "Earth/ChartsLayer", getModel().getId());
        return Files.exists(cache) ? cache.toString() : null;
    }
    
    private WWKAPLayerInfo loadLayerInfo(String cache) {
        WWKAPLayerInfo info = null;
        Path xmlInfo = Paths.get(cache, XML_FILE_NAME);
        if (Files.exists(xmlInfo)) {
            try {
                info = (WWKAPLayerInfo) XMLReader.read(WWKAPLayerInfo.class, xmlInfo.toString());
                info.setChartID(getModel().getId());
            } catch (JAXBException | IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return info;
    }
    
    private void makePolygon() {
        List<LatLon> coordinates = WWUtils.point2DToLatLon(getModel().getPolygon().getCoordinates());
        polygonView = new WWPolygon(this, coordinates);
        polygonView.setValue(AVKey.DISPLAY_NAME, model.getId() + "\n" + model.getName());
        // Normal attributes
        normalAttributes = new BasicShapeAttributes();
        normalAttributes.setOutlineWidth(1.0);
        normalAttributes.setInteriorMaterial(isTiled() ? Material.GREEN : Material.RED);
        normalAttributes.setOutlineMaterial(isTiled() ? Material.GREEN : Material.RED);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setInteriorOpacity(DEFAULT_OPACITY);
        // Highlight attributes
        highlightAttributes = new BasicShapeAttributes(normalAttributes);
        highlightAttributes.setInteriorOpacity(DEFAULT_OPACITY - DIFF_OPACITY);
        polygonView.setAttributes(normalAttributes);
        polygonView.setHighlightAttributes(highlightAttributes);
    }
    
    public void setVisible(boolean visible) {
        if (isTiled) {
            if (visible) {
                WWUtils.insertBeforeCompass(wwd, tiledImageLayerView);
                oldOpacity = opacity;
                setPolygonOpacity(0);
            } else {
                WWUtils.removeLayer(wwd, tiledImageLayerView);
                setPolygonOpacity(oldOpacity, true);
            }
            
            isVisible = visible;
        }
    }
    
    public void setPolygonOpacity(double opacity) {
        setPolygonOpacity(opacity, false);
    }
    
    private void setPolygonOpacity(double opacity, boolean force) {

        if(!isVisible || force) {
            if (opacity < 0) {
                throw new IllegalArgumentException("Opacity must be greater or equal than 0. (Example : minOpacity = 0.000001).");
            }
            if (opacity == 0d) {
                opacity = MIN_OPACITY;
            }

            if (opacity > MIN_OPACITY) {

                normalAttributes.setOutlineOpacity(MAX_OPACITY);
                highlightAttributes.setOutlineOpacity(MAX_OPACITY);
                normalAttributes.setInteriorOpacity(opacity);
                if (opacity > DIFF_OPACITY) {
                    highlightAttributes.setInteriorOpacity(opacity - DIFF_OPACITY);
                } else if (opacity <= DIFF_OPACITY) {
                    highlightAttributes.setInteriorOpacity(MIN_OPACITY);
                }
            } else if (opacity == MIN_OPACITY) {

                normalAttributes.setOutlineOpacity(MIN_OPACITY);
                highlightAttributes.setOutlineOpacity(MIN_OPACITY);
                normalAttributes.setInteriorOpacity(MIN_OPACITY);
                highlightAttributes.setInteriorOpacity(MIN_OPACITY);
            }

            this.opacity = opacity;
        }
    }

    public double getPolygonOpacity() {
        return opacity;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * @return the isTiled
     */
    public boolean isTiled() {
        return isTiled;
    }
    
    /**
     * @return the isVisible
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    /**
     * @return the model
     */
    public Chart getModel() {
        return model;
    }
    
    /**
     * @return the tiledImageLayerView
     */
    public ChartTiledImageLayer getTiledImageLayerView() {
        return tiledImageLayerView;
    }
    
    /**
     * 
     * @return the polygonView
     */
    public SurfacePolygon getPolygonView() {
        return polygonView;
    }
    //</editor-fold>
}
