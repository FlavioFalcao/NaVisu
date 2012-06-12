package fr.enib.navisu.app;

import fr.enib.navisu.app.controller.services.CatalogService;
import fr.enib.navisu.app.view.layers.BMOLayer;
import fr.enib.navisu.app.view.layers.openstreetmap.OpenStreetMapLayer;
import fr.enib.navisu.app.view.layers.openstreetmap.OpenStreetMapTransparentLayer;
import fr.enib.navisu.app.view.settings.charts.ChartsPanel;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.controller.dao.KAPChartCatalogDB;
import fr.enib.navisu.charts.controller.event.kapcontroller.ChartsControllerEventListener;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.view.WWChart;
import fr.enib.navisu.common.catalog.Catalog;
import fr.enib.navisu.common.utils.WWUtils;
import fr.enib.navisu.simulator.Simulator;
import fr.enib.navisu.simulator.events.SimulatorEvent;
import fr.enib.navisu.simulator.events.SimulatorEventListener;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.HotSpotController;
import gov.nasa.worldwindx.examples.util.ToolTipController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;

@ConvertAsProperties(dtd = "-//fr.enib.navisu.app//App//EN", autostore = false)
@TopComponent.Description(
        preferredID = "AppTopComponent",
        iconBase = "fr/enib/navisu/app/earth-16x16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "fr.enib.navisu.app.AppTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_AppAction",
    preferredID = "AppTopComponent"
)
@Messages({
    "CTL_AppAction=App",
    "CTL_AppTopComponent=App Window",
    "HINT_AppTopComponent=This is a App window"
})
@ServiceProvider(service = CatalogService.class)
public final class AppTopComponent extends TopComponent implements
        CatalogService,
        SimulatorEventListener,
        ChartsControllerEventListener {

    private static final KAPChartCatalogDB DB = new KAPChartCatalogDB();
    private static final Logger LOGGER = Logger.getLogger(AppTopComponent.class.getName());
    private static final InputOutput IO = IOProvider.getDefault().getIO("NaVisu", true);
    private static final Preferences PREFS = NbPreferences.forModule(AppTopComponent.class);
    
    // WorldWind
    private static final WorldWindowGLCanvas WWD = new WorldWindowGLCanvas();
    static {
        Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        WWD.setModel(model);
        //System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
    }
    private static final ChartsController CONTROLLER = new ChartsController(WWD);
    private JPanel wwjPanel;
    private JPanel statusPanel;
    private StatusBar statusBar;
    private ToolTipController toolTipController;
    private HighlightController highlightController;
    private HotSpotController hotSpotController;
    
    private static Catalog<? extends Chart> CATALOG = null;
    private static boolean firstTime = true;
    // Simulator
    private static final Simulator SIMULATOR = new Simulator();
    private PointPlacemark placemarkSimu;
    
    public AppTopComponent() {
        initComponents();
        setName(Bundle.CTL_AppTopComponent());
        setToolTipText(Bundle.HINT_AppTopComponent());
        
        if (firstTime) {
            
            firstTime = !firstTime;
            
            initWWJ();
            initWWJLayers();
            initController();
            loadDatabase();
            initSimulator();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Init. Methods">
    private void initWWJ() {
        print("Initialize WorldWind's components.");
        
        // Initialiaze eye position (not animated)
        double lat = PREFS.getDouble(ChartsPanel.KEY_DOUBLE_LAT, 0.0);
        double lon = PREFS.getDouble(ChartsPanel.KEY_DOUBLE_LON, 0.0);
        int ele = PREFS.getInt(ChartsPanel.KEY_INT_ELE, 1500000);
        WWUtils.setEyePosition(WWD, Position.fromDegrees(lat, lon, ele));
        
        // Add controllers to manage highlighting and tool tips.
        toolTipController = new ToolTipController(WWD, AVKey.DISPLAY_NAME, null);
        highlightController = new HighlightController(WWD, SelectEvent.ROLLOVER);
        hotSpotController = new HotSpotController(WWD);

        wwjPanel = new JPanel(new BorderLayout());
        wwjPanel.add(WWD, BorderLayout.CENTER);

        statusPanel = new JPanel(new BorderLayout());

        statusBar = new StatusBar();
        statusBar.setEventSource(WWD);
        statusPanel.add(statusBar, BorderLayout.CENTER);

        JCheckBox setLocalDataOnlyChechBox = new JCheckBox("Local data only");
        setLocalDataOnlyChechBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                boolean state = ie.getStateChange() == ItemEvent.SELECTED;
                statusBar.setShowNetworkStatus(state ? false : true);
                WorldWind.getNetworkStatus().setOfflineMode(state ? true : false);
            }
        });

        statusPanel.add(setLocalDataOnlyChechBox, BorderLayout.EAST);
        wwjPanel.add(statusPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(wwjPanel, BorderLayout.CENTER);
    }

    private void initWWJLayers() {
        print("Initialize WorldWind's layers.");

        // LatLon
        LatLonGraticuleLayer graticuleLayer = new LatLonGraticuleLayer();
        graticuleLayer.setEnabled(false);
        WWUtils.insertBeforeCompass(WWD, graticuleLayer);
        
        // Couche BMOLayer
        BMOLayer bmoLayer = new BMOLayer();
        WWUtils.insertBeforeCompass(WWD, bmoLayer);

        // Open street map layer
        OpenStreetMapLayer openStreetMapLayer = new OpenStreetMapLayer();
        openStreetMapLayer.setEnabled(false);
        WWUtils.insertBeforeCompass(WWD, openStreetMapLayer);

        // Open street map layer (transparent)
        OpenStreetMapTransparentLayer openStreetMapTransparentLayer = new OpenStreetMapTransparentLayer();
        openStreetMapTransparentLayer.setEnabled(false);
        WWUtils.insertBeforeCompass(WWD, openStreetMapTransparentLayer);
    }

    private void initController() {
        print("Initialize controller.");

        CONTROLLER.setDisplayNotTiled(false);
        CONTROLLER.addEventListener(this);
    }

    private void loadDatabase() {
        
        List<Chart> charts = new ArrayList<>(DB.size());
        for(Chart c : DB.readAll()) {
            charts.add(c);
        }
        CATALOG = new Catalog<>(charts);
        updateCatalog(CATALOG);
    }

    private void initSimulator() {

        // PlacemarkLayer
        RenderableLayer placemarkSimuLayer = new RenderableLayer();
        placemarkSimuLayer.setName("[App] Placemark Simulation");
        placemarkSimu = new PointPlacemark(Position.ZERO);
        placemarkSimu.setVisible(false);
        placemarkSimu.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        
        PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
        attrs.setImageAddress("images/boat-64x64.png");
        attrs.setImageColor(new Color(1f, 1f, 1f, 0.6f));
        attrs.setScale(0.6);
        
        placemarkSimuLayer.addRenderable(placemarkSimu);
        WWUtils.insertBeforeCompass(WWD, placemarkSimuLayer);

        SIMULATOR.setInterpolationType(Simulator.INTERPOLATION_RHUMB);
        SIMULATOR.addEventListener(this);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Events">
    @Override
    public void updatePosition(SimulatorEvent event) {
        LatLon ll = event.getNewPosition();

        CONTROLLER.setPointOfInterest(ll);
        
        if (!placemarkSimu.isVisible()) {
            placemarkSimu.setVisible(true);
        }
        
        placemarkSimu.moveTo(Position.fromDegrees(ll.latitude.degrees, ll.longitude.degrees));
        
        if(event.isLastPosition()) {
            placemarkSimu.setVisible(false);
        }
        
        WWD.redraw();
    }

    @Override
    public void updateCatalog(Catalog<? extends Chart> catalog) {
        print("updateCatalog(" + catalog.size() + " charts)");

        CATALOG = catalog;
        CONTROLLER.clear();
        CONTROLLER.addAll(catalog);
    }

    @Override // ChartsControllerEventListener
    public void selected(SelectEvent se, WWChart wwChart) {
        
        if (se.isRightClick()) {
            
            if (wwChart.isTiled() && !wwChart.isVisible()) {
                try {
                    wwChart.setVisible(true);
                    return;
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (wwChart.isVisible()) {
                try {
                    wwChart.setVisible(false);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    public static Catalog<? extends Chart> catalog() {
        return CATALOG;
    }

    public static WorldWindow wwd() {
        return WWD;
    }
    
    public WorldWindow getWwd() {
        return WWD;
    }

    public static ChartsController controller() {
        return CONTROLLER;
    }
    
    public ChartsController getController() {
        return CONTROLLER;
    }
    
    public static InputOutput io() {
        return IO;
    }
    
    public static KAPChartCatalogDB db() {
        return DB;
    }
    
    public static Simulator simulator() {
        return SIMULATOR;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {}
    void readProperties(java.util.Properties p) {}
    
    private void print(Object s) {
        IO.getOut().println("[AppTopComponent] " + s.toString());
    }
}
