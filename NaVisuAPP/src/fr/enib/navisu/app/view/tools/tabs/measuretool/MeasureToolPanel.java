package fr.enib.navisu.app.view.tools.tabs.measuretool;

import fr.enib.navisu.common.utils.WWUtils;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.TerrainProfileLayer;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author ThibaultPENSEC & Jordan MENS
 * @date 01/06/2012
 */
public class MeasureToolPanel extends JPanel {

    private final WorldWindow wwd;
    private int lastTabIndex = -1;
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private TerrainProfileLayer profile = new TerrainProfileLayer();
    private PropertyChangeListener measureToolListener = new MeasureToolListener();

    public MeasureToolPanel(WorldWindow wwd) {
        this.wwd = wwd;
        setBackground(Color.WHITE);
        tabbedPane.setBackground(Color.WHITE);
        initTool();
    }

    private void initTool() {
        // Add terrain profile layer
        profile.setEventSource(wwd);
        profile.setFollow(TerrainProfileLayer.FOLLOW_PATH);
        profile.setShowProfileLine(false);
        WWUtils.insertBeforePlacenames(wwd, profile);

        // Add + tab
        tabbedPane.setBackground(Color.white);
        tabbedPane.add(new JPanel());
        tabbedPane.setTitleAt(0, "+");
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    // Add new measure tool in a tab when '+' selected
                    MeasureTool measureTool = new MeasureTool(wwd);
                    measureTool.setController(new MeasureToolController());
                    tabbedPane.add(new MeasureToolSinglePanel(wwd, measureTool));
                    tabbedPane.setTitleAt(tabbedPane.getTabCount() - 1, "" + (tabbedPane.getTabCount() - 1));
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                    switchMeasureTool();
                } else {
                    switchMeasureTool();
                }
            }
        });

        // Add measure tool control panel to tabbed pane
        MeasureTool measureTool = new MeasureTool(wwd);
        measureTool.setController(new MeasureToolController());
        tabbedPane.add(new MeasureToolSinglePanel(wwd, measureTool));
        tabbedPane.setTitleAt(1, "1");
        tabbedPane.setSelectedIndex(1);
        switchMeasureTool();

        add(tabbedPane, BorderLayout.CENTER);
    }

    private class MeasureToolListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // Measure shape position list changed - update terrain profile
            if (event.getPropertyName().equals(MeasureTool.EVENT_POSITION_ADD)
                    || event.getPropertyName().equals(MeasureTool.EVENT_POSITION_REMOVE)
                    || event.getPropertyName().equals(MeasureTool.EVENT_POSITION_REPLACE)) {
                updateProfile(((MeasureTool) event.getSource()));
            }
        }
    }

    private void switchMeasureTool() {
        // Disarm last measure tool when changing tab and switching tool
        if (lastTabIndex != -1) {
            MeasureTool mt = ((MeasureToolSinglePanel) tabbedPane.getComponentAt(lastTabIndex)).getMeasureTool();
            mt.setArmed(false);
            mt.removePropertyChangeListener(measureToolListener);
        }
        // Update terrain profile from current measure tool
        lastTabIndex = tabbedPane.getSelectedIndex();
        MeasureTool mt = ((MeasureToolSinglePanel) tabbedPane.getComponentAt(lastTabIndex)).getMeasureTool();
        mt.addPropertyChangeListener(measureToolListener);
        updateProfile(mt);
    }

    private void updateProfile(MeasureTool mt) {
        ArrayList<? extends LatLon> positions = mt.getPositions();
        if (positions != null && positions.size() > 1) {
            profile.setPathPositions(positions);
            profile.setEnabled(true);
        } else {
            profile.setEnabled(false);
        }

        wwd.redraw();
    }
}
