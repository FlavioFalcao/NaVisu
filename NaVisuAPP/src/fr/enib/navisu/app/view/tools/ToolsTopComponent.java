package fr.enib.navisu.app.view.tools;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.view.tools.tabs.layerstool.LayersPanel;
import fr.enib.navisu.app.view.tools.tabs.measuretool.MeasureToolPanel;
import fr.enib.navisu.app.view.tools.tabs.miscellaneous.MiscellaneousPanel;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.common.utils.GUIUtils;
import fr.enib.navisu.simulator.view.SimulatorPanel;
import gov.nasa.worldwind.WorldWindow;
import java.util.prefs.Preferences;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//fr.enib.navisu.app.view.tools//Tools//EN", autostore = false)
@TopComponent.Description(
    preferredID = "ToolsTopComponent",
    iconBase = "fr/enib/navisu/app/view/tools/picto_tool.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "leftSlidingSide", openAtStartup = true)
@ActionID(category = "Window", id = "fr.enib.navisu.app.view.tools.ToolsTopComponent")
@ActionReference(path = "Menu/Tools" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_ToolsAction",
    preferredID = "ToolsTopComponent")
@Messages({
    "CTL_ToolsAction=Tools",
    "CTL_ToolsTopComponent=Tools",
    "HINT_ToolsTopComponent=This window contains all NaVisu's tools"
})
public final class ToolsTopComponent extends TopComponent {

    private static final Preferences PREFS = NbPreferences.forModule(ToolsTopComponent.class);
    
    private final AppTopComponent app;
    private final WorldWindow wwd;
    private final ChartsController controller;
    private LayersPanel layersPanel;
    
    public ToolsTopComponent() {
        initComponents();
        setName(Bundle.CTL_ToolsTopComponent());
        setToolTipText(Bundle.HINT_ToolsTopComponent());
        
        app = (AppTopComponent) WindowManager.getDefault().findTopComponent("AppTopComponent");
        wwd = app.getWwd();
        controller = AppTopComponent.controller();
        
        layersPanel = new LayersPanel(wwd);
        initTabbedPane();
        mainTabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                int selectedTab = mainTabbedPane.getSelectedIndex();
                PREFS.putInt("selected.Tab", selectedTab);
            }
        });
    }
    
    private void initTabbedPane() {
        
        if(wwd == null)
            throw new IllegalStateException("wwd is null.");
        
        mainTabbedPane.addTab("Layer Tree", layersPanel);
        mainTabbedPane.addTab("Measure", new JScrollPane(new MeasureToolPanel(wwd)));
        mainTabbedPane.addTab("Simulator", new JScrollPane(new SimulatorPanel(wwd, AppTopComponent.simulator())));
        mainTabbedPane.addTab("Miscellaneous", new JScrollPane(new MiscellaneousPanel(wwd, controller)));
    }

    @Override
    protected void componentOpened() {

        int selectedTab = PREFS.getInt("selected.Tab", 0);
        mainTabbedPane.setSelectedIndex(selectedTab);
        layersPanel.loadTreeState();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());
        add(mainTabbedPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane mainTabbedPane;
    // End of variables declaration//GEN-END:variables
    void writeProperties(java.util.Properties p) {}
    void readProperties(java.util.Properties p) {}
}