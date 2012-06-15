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
import fr.enib.navisu.models3D.model.kml.Model3D;
import fr.enib.navisu.models3D.model.obj3ds.Ship;
import fr.enib.navisu.models3D.model.obj3ds.Ship3D;
import fr.enib.navisu.simulator.Simulator;
import fr.enib.navisu.simulator.events.SimulatorEvent;
import fr.enib.navisu.simulator.events.SimulatorEventListener;
import fr.enib.navisu.util.FilesReader;
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
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;
import gov.nasa.worldwindx.examples.util.ToolTipController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import net.java.joglutils.model.Movable3DModel;
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
@TopComponent.Description(preferredID = "AppTopComponent",
iconBase = "fr/enib/navisu/app/earth-16x16.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "fr.enib.navisu.app.AppTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_AppAction",
preferredID = "AppTopComponent")
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

    // ======================================
    // =             Attributes             =
    // ======================================
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
    private static boolean firstTime = true;
    // Simulator
    private static final Simulator SIMULATOR = new Simulator();
    private PointPlacemark placemarkSimu;

    // ======================================
    // =             Constructor            =
    // ======================================
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
            System.out.println("Hello NaVisu");
        }
    }

    // =================================================
    // =             Initialization methods            =
    // =================================================
    private void initWWJ() {
        print("Initialize WorldWind's components");

        // Initialiaze eye position (not animated)
        double lat = PREFS.getDouble(ChartsPanel.KEY_DOUBLE_LAT, 48.0561);
        double lon = PREFS.getDouble(ChartsPanel.KEY_DOUBLE_LON, -3.5333);
        int ele = PREFS.getInt(ChartsPanel.KEY_INT_ELE, 796986);
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
        print("Initialize WorldWind's layers");

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

        // Chargement d'un navire mobile à partir d'un fichier 3ds
        Movable3DModel movable3DModel;
        Ship3D ship3D;
        double lat = 48.36;
        double lon = -4.48;
        movable3DModel = new Movable3DModel("data/ships/lithops.3ds", Position.fromDegrees(lat, lon, 2));
        movable3DModel.setSize(100);
        ship3D = new Ship3D(new Ship(), movable3DModel);
        ship3D.getMovable3DModel().getModel().setUseLighting(true);
        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(ship3D.getMovable3DModel());
        layer.setName("Lithops");
        WWUtils.insertBeforeCompass(WWD, layer);

        // Chargement des objets en KML en arriere plan
        new LoaderSW().execute();
    }

    class LoaderSW
            extends SwingWorker<Integer, Object> {

        public LoaderSW() {
        }

        @Override
        public Integer doInBackground() {
            // load3DLayer("data/buildings", "kmz", "Buildings");
            load3DLayer("data/lightHouses", "kmz", "LightHouses");
            load3DLayer("data/ships", "kmz", "Ships");
            return 1;
        }

        private void load3DLayer(String dir, String suffix, String name) {
            RenderableLayer layer;
            List<String> kmzFiles;

            layer = new RenderableLayer();
            kmzFiles = new FilesReader().search(dir, suffix);
            for (String s : kmzFiles) {
                layer.addRenderable(new Model3D(s).getKmlController());
            }
            layer.setName(name);
            WWUtils.insertBeforeCompass(WWD, layer);
        }
    }

    private void initController() {
        print("Initialize controller");

        CONTROLLER.setDisplayNotTiled(false);
        CONTROLLER.addEventListener(this);
    }

    private void loadDatabase() {
        print("Load database");
        List<Chart> charts = new ArrayList<>(DB.size());
        for (Chart c : DB.readAll()) {
            charts.add(c);
        }
        updateCatalog(new Catalog<>(charts));
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

    // ======================================
    // =             Events                 =
    // ======================================
    @Override
    public void updatePosition(SimulatorEvent event) {
        LatLon ll = event.getNewPosition();

        CONTROLLER.setPointOfInterest(ll);

        if (!placemarkSimu.isVisible()) {
            placemarkSimu.setVisible(true);
        }

        placemarkSimu.moveTo(Position.fromDegrees(ll.latitude.degrees, ll.longitude.degrees));

        if (event.isLastPosition()) {
            placemarkSimu.setVisible(false);
        }

        WWD.redraw();
    }

    @Override
    public void updateCatalog(Catalog<? extends Chart> catalog) {
        print("updateCatalog(" + catalog.size() + " charts)");

        CONTROLLER.clear();
        CONTROLLER.addAll(catalog);
    }

    @Override
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

    // ======================================
    // =             Getters                =
    // ======================================
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
    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

    @Override
    protected void componentOpened() {
        // If the cache does not exists, we create it
        String wwjcache = WWUtils.WWJ_DEFAULT_CACHE;
        String kapCache = "Earth/ChartsLayer";
        Path cache = Paths.get(wwjcache, kapCache);
        if (!Files.exists(cache)) {
            try {
                Files.createDirectory(cache);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void print(Object s) {
        IO.getOut().println("[AppTopComponent] " + s.toString());
    }
}
