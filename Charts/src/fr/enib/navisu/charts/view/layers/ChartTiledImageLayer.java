package fr.enib.navisu.charts.view.layers;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.BasicTiledImageLayer;
import gov.nasa.worldwind.util.LevelSet;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec & Jordan Mens
 * @date 28 mars 2012
 */
public class ChartTiledImageLayer extends BasicTiledImageLayer {

    private static final Logger LOGGER = Logger.getLogger(ChartTiledImageLayer.class.getName());

    // Defaults params
    private static final int TILE_WIDTH = 512;
    private static final int TILE_HEIGHT = 512;
    private static final double LAT_DEG = 90.0;
    private static final double LON_DEG = 90.0;
    
    public static final String DEFAULT_DATA_CACHE_NAME = "Earth/ChartsLayer/";
    
    /**
     * @param tileName The name of the tile layer name in data cache
     * @param numEmptyLevels Number of levels before first tilde layer displays
     * @param numLevels Number of levels
     * @param formatSuffix Tiled images format. Ex: "png" or "dds"
     */
    public ChartTiledImageLayer(String tileName, int numEmptyLevels, int numLevels, String formatSuffix) {
        super(makeLevels(
                tileName, 
                numEmptyLevels, 
                numLevels, 
                formatSuffix, 
                DEFAULT_DATA_CACHE_NAME, 
                LAT_DEG, LON_DEG, 
                TILE_WIDTH, TILE_HEIGHT));
        
        setName(tileName);
        setUseTransparentTextures(true);
    }
    
    /**
     * Return a new <code>LevelSet</code> from params
     * 
     * @param tileName The name of the tile layer name in data cache
     * @param numEmptyLevels Number of levels before display first layer
     * @param numLevels Number of levels
     * @param formatSuffix Tiled images format. Ex: "png" or "dds"
     * @param dataCacheName Ex: Earth/KapLayer/
     * @param latDegrees
     * @param lonDegrees
     * @param tileWidth
     * @param tileHeight
     * 
     * @return a <code>LevelSet</code>
     */
    private static LevelSet makeLevels(
            String tileName,
            int numEmptyLevels, 
            int numLevels, 
            String formatSuffix,
            String dataCacheName,
            double latDegrees, double lonDegrees, 
            int tileWidth, int tileHeight) {
        
        AVList params = new AVListImpl();
        
        // Constants params
        params.setValue(AVKey.TILE_WIDTH, tileWidth);
        params.setValue(AVKey.TILE_HEIGHT, tileHeight);
        params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(
                Angle.fromDegrees(latDegrees), 
                Angle.fromDegrees(lonDegrees)));
        params.setValue(AVKey.SECTOR, Sector.FULL_SPHERE);
        // Dynamics params
        params.setValue(AVKey.DATA_CACHE_NAME, dataCacheName + tileName);
        params.setValue(AVKey.DATASET_NAME, tileName);
        params.setValue(AVKey.FORMAT_SUFFIX, formatSuffix);
        params.setValue(AVKey.NUM_LEVELS, numLevels);
        params.setValue(AVKey.NUM_EMPTY_LEVELS, numEmptyLevels);
        
        return new LevelSet(params);
    }
}
