package fr.enib.navisu.app.view.explorer;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.nodes.ChartChildFactory;
import fr.enib.navisu.app.controller.nodes.ChartsRootNode;
import fr.enib.navisu.app.controller.services.CatalogService;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.view.WWChart;
import fr.enib.navisu.common.catalog.Catalog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//fr.enib.wwbsbapp.view.explorer//ChartView//EN",
autostore = false)
@TopComponent.Description(preferredID = "ChartViewTopComponent",
iconBase = "fr/enib/wwbsbapp/view/explorer/map_magnify.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "fr.enib.wwbsbapp.view.explorer.ChartViewTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ChartViewAction",
preferredID = "ChartViewTopComponent")
@Messages({
    "CTL_ChartViewAction=ChartView",
    "CTL_ChartViewTopComponent=ChartView Window",
    "HINT_ChartViewTopComponent=This is a ChartView window"
})

@ServiceProvider(service=CatalogService.class)
public final class ChartViewTopComponent extends TopComponent implements ExplorerManager.Provider, 
                                                                         CatalogService {

    // ======================================
    // =             Attributes             =
    // ======================================
    public static final InputOutput IO = AppTopComponent.io();
    
    private static ExplorerManager manager = new ExplorerManager();;
    private List<Chart> charts;
    private ChartsController controller;
    private boolean displayNonTiled;
    
    private static boolean firstTime = true;
    
    // ======================================
    // =             Constructor            =
    // ======================================
    public ChartViewTopComponent() {
        
        initComponents();
        setName(Bundle.CTL_ChartViewTopComponent());
        setToolTipText(Bundle.HINT_ChartViewTopComponent());
        
        if (firstTime) {
            
            firstTime = !firstTime;
            
            associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
            loadController();
            displayNonTiled = controller.isDisplayNotTiled();
            charts = new ArrayList<>();
            if (AppTopComponent.controller().getCharts() != null) {
                updateCatalog(AppTopComponent.controller().getCharts());
            } else {
                updateManager();
            }
        }
    }

    // ======================================
    // =             Methods                =
    // ======================================
    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    
    @Override
    public void updateCatalog(Catalog<? extends Chart> catalog) {
        
        if(charts == null) {
            charts = new ArrayList<>(catalog.size());
        }
        
        print("updateCatalog(" + catalog.size() + " charts)");
        charts.clear();

        WWChart wwc;
        for (Chart c : catalog.list()) {
            wwc = controller.findWWChart(c);

            if (wwc != null && wwc.isTiled() && !displayNonTiled) {
                charts.add(c);
            }

            if (displayNonTiled) {
                charts.add(c);
            }
        }

        sortCharts();
        updateManager();
    }
    
    private void loadController() {
        AppTopComponent app = (AppTopComponent) WindowManager.getDefault().findTopComponent("AppTopComponent");
        controller = app.getController();
    }
    
    public void updateManager() {
        manager.setRootContext(new ChartsRootNode<>(Children.create(new ChartChildFactory(charts), true)));
    }

    public void displayNonTiled(boolean choice) {
        displayNonTiled = choice;
        if (controller == null) {
            loadController();
        }
        updateCatalog(controller.getCharts());
    }
    
    private void sortCharts() {
        Collections.sort(charts, new Comparator<Chart>() {

            @Override
            public int compare(Chart o1, Chart o2) {
                
                
                String[] sIds = new String[]{o1.getId(), o2.getId()};
                int[] iIds = new int[sIds.length];
                int[] iIdsBis = new int[sIds.length];
                
                try {
                    for(int i=0; i<sIds.length; i++) {
                        if(sIds[i].contains("_")) {
                            try {
                                iIds[i] = Integer.parseInt(sIds[i].substring(0, sIds[i].indexOf("_")));
                                iIdsBis[i] = Integer.parseInt(sIds[i].substring(sIds[i].indexOf("_") + 1, sIds[i].length()));
                            } catch(NumberFormatException ex) {
                                IO.getErr().println(ex.getMessage());
                                return 0;
                            }
                        } else {    
                            iIds[i] = Integer.parseInt(sIds[i]);
                            iIdsBis[i] = -1;
                        }
                    }

                    if(iIds[0] < iIds[1]) {
                        return iIds[0] - iIds[1];
                    }
                    else if(iIds[0] == iIds[1]) {
                        if(iIdsBis[0] != -1 && iIdsBis[1] != -1) {
                            if(iIdsBis[0] < iIdsBis[1]) {
                                return iIdsBis[0] - iIdsBis[1];
                            }
                        }
                    }
                } catch(NumberFormatException ex) {
                    return o1.toString().compareTo(o2.toString());
                }
                
                return 0;
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new BeanTreeView();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jScrollPane.setBackground(null);
        add(jScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
 
    void writeProperties(java.util.Properties p) {}
    void readProperties(java.util.Properties p) {}
    
    private void print(Object str) {
        IO.getOut().println("[ChartViewTopComponent] " + str);
    }
}
