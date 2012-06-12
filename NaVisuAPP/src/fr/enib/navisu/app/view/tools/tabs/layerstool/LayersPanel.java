package fr.enib.navisu.app.view.tools.tabs.layerstool;

import fr.enib.navisu.app.model.LayerTreeListModel;
import fr.enib.navisu.app.view.tools.ToolsTopComponent;
import fr.enib.navisu.charts.view.layers.ChartTiledImageLayer;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.NbPreferences;

/**
 *
 * @author Jordan MENS & Thibault PENSEC
 */
public final class LayersPanel extends javax.swing.JPanel {

    public LayerTreeListModel model;
    private final WorldWindow wwd;
    private DefaultMutableTreeNode rootNode;
    private Icon icon;
    private LayerList layerList;
    private LayerList oldLayerList;
    private DefaultMutableTreeNode effectNode;
    private DefaultMutableTreeNode utilsNode;
    private DefaultMutableTreeNode satelliteNode;
    private DefaultMutableTreeNode cartographicNode;
    private DefaultMutableTreeNode navisuNode;
    private DefaultMutableTreeNode aerialNode;
    private DefaultMutableTreeNode kapNode;
    private DefaultMutableTreeNode topographicNode;
    private DefaultMutableTreeNode othersNode;
    private static final Preferences PREFS = NbPreferences.forModule(ToolsTopComponent.class);

    public LayersPanel(WorldWindow wwd) {

        icon = new ImageIcon(getClass().getResource("/fr/enib/navisu/app/view/tools/tabs/layerstool/earth-icon.png"));

        this.wwd = wwd;
        layerList = this.wwd.getModel().getLayers();
        oldLayerList = new LayerList(layerList);
        
        initComponents();
        initTree();

        treeLayerList.addTreeExpansionListener(new TreeExpansionListener() {

            @Override
            public void treeExpanded(TreeExpansionEvent tee) {
                saveTreeState();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent tee) {

                saveTreeState();
            }
        });
        openRootNode();
    }

    public void saveTreeState() {}
    
    private DefaultMutableTreeNode findNodeByName(String layerName) {

        Enumeration en = ((DefaultMutableTreeNode) model.getModel().getRoot()).breadthFirstEnumeration();
        DefaultMutableTreeNode node;
        while (en.hasMoreElements()) {
            node = (DefaultMutableTreeNode) en.nextElement();
            if (node.getUserObject() instanceof CheckBoxNode) {

                CheckBoxNode c = (CheckBoxNode) node.getUserObject();
                if (c.getLayer().getName().equals(layerName)) {
                    return node;
                } 
            } 
        }

       return (DefaultMutableTreeNode) model.getModel().getRoot();
    }    
    
    
    public void loadTreeState() {}

    public void initTree() {

        treeLayerList.setEditable(true);
        treeLayerList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeLayerList.setShowsRootHandles(true);
        treeLayerList.setCellRenderer(new CellRenderer());
        treeLayerList.setCellEditor(new CellEditor());

        effectNode = new DefaultMutableTreeNode("Effects");
        utilsNode = new DefaultMutableTreeNode("Utils");

        aerialNode = new DefaultMutableTreeNode("Aerials");
        satelliteNode = new DefaultMutableTreeNode("Satellites");
        cartographicNode = new DefaultMutableTreeNode("Cartography");
        navisuNode = new DefaultMutableTreeNode("NaVisu");
        kapNode = new DefaultMutableTreeNode("Kap");
        othersNode = new DefaultMutableTreeNode("Miscellaneous");
        topographicNode = new DefaultMutableTreeNode("Topographic");
        rootNode.add(effectNode);
        rootNode.add(utilsNode);
        rootNode.add(satelliteNode);
        rootNode.add(topographicNode);
        rootNode.add(aerialNode);
        rootNode.add(cartographicNode);
        rootNode.add(navisuNode);
        rootNode.add(kapNode);
        rootNode.add(othersNode);

        wwd.getModel().getLayers().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                update();
            }
        });

        for (Layer l : layerList) {
            addNode(l);
        }
    }

    private void openRootNode() {
        treeLayerList.expandRow(0);
    }

    private void update() {

        CheckBoxNode cbn;
        DefaultMutableTreeNode d;

        for (Layer l : layerList) {
            cbn = new CheckBoxNode(l);
            d = new DefaultMutableTreeNode(cbn);
            if (!oldLayerList.contains(l)) {
                oldLayerList.add(l);
                if (l instanceof ChartTiledImageLayer) {
                    model.getModel().insertNodeInto(d, kapNode, kapNode.getChildCount());
                } else {
                    model.getModel().insertNodeInto(d, othersNode, othersNode.getChildCount());
                }
            }
        }

        for (Layer l : oldLayerList) {

            if (!layerList.contains(l)) {
                oldLayerList.remove(l);
                DefaultMutableTreeNode node = findNodeByName(l.getName());
                try {
                    if (node.getParent() != null && node != null) {
                        model.getModel().removeNodeFromParent(node);
                    }
                } catch (NullPointerException ex) {
                }
            }
        }
    }

    public void expandNode(int i) {
        treeLayerList.expandRow(i);
    }

    private void expandAllNodes() {
        for (int i = 0; i < treeLayerList.getRowCount(); i++) {
            treeLayerList.expandRow(i);
        }
    }



    private void addNode(Layer l) {
        DefaultMutableTreeNode d;
        CheckBoxNode cbn;

        cbn = new CheckBoxNode(l);
        d = new DefaultMutableTreeNode(cbn);
        String layerName = l.getName();

        switch (layerName) {
            case "Stars":
            case "Atmosphère":
                effectNode.add(d);
                break;

            case "NASA Blue Marble Image":
            case "Blue Marble (WMS) 2004":
            case "i-cubed Landsat":
                satelliteNode.add(d);
                break;

            case "Échelle":
            case "Lat-Lon Graticule":
            case "Boussole":
            case "Toponymes":
                utilsNode.add(d);
                break;

            case "OpenStreetMap Mapnik":
            case "OpenStreetMap Mapnik (Transparent)":
            case "USGS Urban Area Ortho":
                cartographicNode.add(d);
                break;


            case "Brest Metropole Oceane":
            case "MS Virtual Earth Aerial":
                aerialNode.add(d);
                break;

            case "USGS Topographic Maps 1:250K":
            case "USGS Topographic Maps 1:100K":
            case "USGS Topographic Maps 1:24K":
                topographicNode.add(d);
                break;

            case "[Charts] Polygons layer":
            case "[LineBuilder] Polyline" :
                navisuNode.add(d);
                break;

            default:
                if (l instanceof ChartTiledImageLayer) {
                    kapNode.add(d);
                } else {
                    othersNode.add(d);
                }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        rootNode = new DefaultMutableTreeNode("Layer List");
        model = new LayerTreeListModel(rootNode);
        treeLayerList = new javax.swing.JTree(model.getModel());

        setLayout(new java.awt.BorderLayout());

        scrollPane.setViewportView(treeLayerList);

        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTree treeLayerList;
    // End of variables declaration//GEN-END:variables
}
