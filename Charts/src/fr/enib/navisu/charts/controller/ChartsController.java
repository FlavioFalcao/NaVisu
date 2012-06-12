package fr.enib.navisu.charts.controller;

import fr.enib.navisu.charts.controller.event.kapcontroller.ChartsControllerEventListener;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.view.WWChart;
import fr.enib.navisu.charts.view.renderables.WWPolygon;
import fr.enib.navisu.common.catalog.Catalog;
import fr.enib.navisu.common.event.EventProvider;
import fr.enib.navisu.common.utils.WWUtils;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.RenderableLayer;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 07/06/2012
 */
public class ChartsController implements SelectListener, EventProvider<ChartsControllerEventListener> {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(ChartsController.class.getName());
    /** WorldWindow */
    private final WorldWindow wwd;
    /** Charts */
    private List<WWChart> charts;
    /** Compare 2 charts in terms of there scale */
    public static final Comparator<WWChart> WWCHART_SCALE_COMPARATOR = new Comparator<WWChart>() {

        @Override
        public int compare(WWChart c1, WWChart c2) {
            return c2.getModel().getScale() - c1.getModel().getScale();
        }
    };
    /** Charts' polygons layer */
    private RenderableLayer polygonsLayer;
    /** Event listeners */
    private Collection<ChartsControllerEventListener> listeners;
    /** Display not tiled ? */
    private boolean isDisplayNotTiled;
    
    /**
     * 
     * @param wwd the wwd
     */
    public ChartsController(final WorldWindow wwd) {
        this.wwd = wwd;
        initialize();
    }
    
    private void initialize() {
        LOGGER.info("initialize()");
        wwd.addSelectListener(this);
        charts = new ArrayList<>();
        listeners = new ArrayList<>();
        polygonsLayer = new RenderableLayer();
        polygonsLayer.setName("[Charts] Polygons Layer");
        WWUtils.insertBeforeCompass(wwd, polygonsLayer);
    }

    //<editor-fold defaultstate="collapsed" desc="Events">
    @Override
    public void selected(SelectEvent se) {
        if(se == null) return;
        Object source = se.getTopObject();
        if(source == null) return;
        
        if(source instanceof WWPolygon) {
            for(ChartsControllerEventListener listener : listeners) {
                listener.selected(se, ((WWPolygon)source).getOwner());
            }
        }
    }
    
    @Override
    public void addEventListener(ChartsControllerEventListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeEventListener(ChartsControllerEventListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public void fireEventListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //</editor-fold>
    
    public void add(final Chart chart) {
        LOGGER.info("add(" + chart.getId() + ")");
        charts.add(new WWChart(wwd, chart));
        update();
    }
    
    public void addAll(List<? extends Chart> charts) {
        LOGGER.info("addAll(" + charts.size() + " charts)");
        for(Chart chart : charts) {
            this.charts.add(new WWChart(wwd, chart));
        }
        update();
    }
    
    public void addAll(Catalog<? extends Chart> charts) {
        LOGGER.info("addAll(" + charts.size() + " charts)");
        for(Chart chart : charts.list()) {
            this.charts.add(new WWChart(wwd, chart));
        }
        update();
    }
    
    public void remove(final Chart chart) {
        LOGGER.info("remove(" + chart.getId() + ")");
        for(int i=0; i<charts.size(); i++) {
            if(charts.get(i).getModel().getId().equals(chart.getId())) {
                charts.remove(i);
                break;
            }
        }
        update();
    }
    
    public void clear() {
        LOGGER.info("clear()");
        charts.clear();
        update();
    }
    
    public void update() {
        polygonsLayer.removeAllRenderables();
        
        if(!WWUtils.containsLayer(wwd, polygonsLayer)) {
            LOGGER.info("update() -> (re)add polygonsLayer : " + polygonsLayer.getName());
            WWUtils.insertBeforeCompass(wwd, polygonsLayer);
        }
        
        Collections.sort(charts, WWCHART_SCALE_COMPARATOR);
        
        int count = 0;
        for(WWChart chart : charts) {
            if(isDisplayNotTiled || chart.isTiled()) {
                polygonsLayer.addRenderable(chart.getPolygonView());
                count++;
            }
        }
        
        LOGGER.info("update() -> " + count + " polygons added");
        
        wwd.redraw();
    }

    public void setPointOfInterest(LatLon ll) {
        
    }
    
    /**
     * @return the isDisplayNotTiled
     */
    public boolean isDisplayNotTiled() {
        return isDisplayNotTiled;
    }

    /**
     * @param isDisplayNotTiled the isDisplayNotTiled to set
     */
    public void setDisplayNotTiled(boolean isDisplayNotTiled) {
        this.isDisplayNotTiled = isDisplayNotTiled;
    }
    
    /**
     * 
     * @param chart the Chart to find
     * @return the WWChart found
     */
    public WWChart findWWChart(final Chart chart) {
        
        for(WWChart wwChart : charts) {
            if(wwChart.getModel().getId().equals(chart.getId())) {
                return wwChart;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @return the charts catalog
     */
    public Catalog<Chart> getCharts() {
       Catalog<Chart> catalog = new Catalog<>();
       for(WWChart chart : charts)
           catalog.add(chart.getModel());
       return catalog;
    }
    
    /**
     * 
     * @param opacity 
     */
    public void setPolygonsOpacity(double opacity) {
        for(WWChart chart : charts) {
            chart.setPolygonOpacity(opacity);
        }
        wwd.redraw();
    }
    
    public double getPolygonsOpacity() {
        double opacity = WWChart.DEFAULT_OPACITY;
        if(charts.size() > 0) {
            opacity = charts.get(0).getPolygonOpacity();
        }
        return opacity;
    }
}
