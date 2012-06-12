/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.view.tools.tabs.miscellaneous;

import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.common.utils.GUIUtils;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.terrain.BathymetryFilterElevationModel;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.JLabel;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 11/06/2012
 */
public class MiscellaneousPanel extends javax.swing.JPanel {
    
    private final WorldWindow wwd;
    private final ChartsController controller;
    // Type of world
    private Globe roundGlobe;
    private FlatGlobe flatGlobe;
    private final int ROUND = 0;
    private final int FLAT = 1;
    // Bathy
    private ElevationModel defaultElevationModel;
    private BathymetryFilterElevationModel noDepthModel;
    private boolean isUseBathyFilter;

    public MiscellaneousPanel(final WorldWindow wwd, final ChartsController controller) {
        initComponents();
        this.wwd = wwd;
        this.controller = controller;
        
        initialize();
    }
    
    
    private void initialize() {
        if (isFlatGlobe()) {
            this.flatGlobe = (FlatGlobe) wwd.getModel().getGlobe();
            this.roundGlobe = new Earth();
        } else {
            this.flatGlobe = new EarthFlat();
            this.roundGlobe = wwd.getModel().getGlobe();
        }
        
        defaultElevationModel = wwd.getModel().getGlobe().getElevationModel();
        noDepthModel = new BathymetryFilterElevationModel(defaultElevationModel);
        isUseBathyFilter = false;
        
        verticalExaggerationSlider.setValue((int)wwd.getSceneController().getVerticalExaggeration());
    }
    
    public final boolean isFlatGlobe() {
        return wwd.getModel().getGlobe() instanceof FlatGlobe;
    }

    public void enableFlatGlobe(boolean flat) {
        if (isFlatGlobe() == flat) {
            return;
        }

        if (!flat) {
            // Switch to round globe
            wwd.getModel().setGlobe(roundGlobe);
            // Switch to orbit view and update with current position
            FlatOrbitView flatOrbitView = (FlatOrbitView) wwd.getView();
            BasicOrbitView orbitView = new BasicOrbitView();
            orbitView.setCenterPosition(flatOrbitView.getCenterPosition());
            orbitView.setZoom(flatOrbitView.getZoom());
            orbitView.setHeading(flatOrbitView.getHeading());
            orbitView.setPitch(flatOrbitView.getPitch());
            wwd.setView(orbitView);
        } else {
            // Switch to flat globe
            wwd.getModel().setGlobe(flatGlobe);
            flatGlobe.setProjection(FlatGlobe.PROJECTION_MERCATOR);
            // Switch to flat view and update with current position
            BasicOrbitView orbitView = (BasicOrbitView) wwd.getView();
            FlatOrbitView flatOrbitView = new FlatOrbitView();
            flatOrbitView.setCenterPosition(orbitView.getCenterPosition());
            flatOrbitView.setZoom(orbitView.getZoom());
            flatOrbitView.setHeading(orbitView.getHeading());
            flatOrbitView.setPitch(orbitView.getPitch());
            wwd.setView(flatOrbitView);
        }

        wwd.redraw();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        displayNotTiledCheck = new javax.swing.JCheckBox();
        opacitySlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        opacityLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        roundOrFlatWorldCombo = new javax.swing.JComboBox();
        removeBathyCheck = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        verticalExaggerationSlider = new javax.swing.JSlider();
        verticalExaggerationLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.jPanel1.border.title"))); // NOI18N

        displayNotTiledCheck.setBackground(new java.awt.Color(255, 255, 255));
        displayNotTiledCheck.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.displayNotTiledCheck.text")); // NOI18N
        displayNotTiledCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayNotTiledCheckActionPerformed(evt);
            }
        });

        opacitySlider.setBackground(new java.awt.Color(255, 255, 255));
        opacitySlider.setMaximum(25);
        opacitySlider.setValue(10);
        opacitySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                opacitySliderStateChanged(evt);
            }
        });
        opacitySlider.addMouseWheelListener(new GUIUtils.JSliderMouseWheelListener());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.jLabel1.text")); // NOI18N

        opacityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        opacityLabel.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.opacityLabel.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(displayNotTiledCheck, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opacitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opacityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(displayNotTiledCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opacitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opacityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.jPanel2.border.title"))); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.jLabel2.text")); // NOI18N

        roundOrFlatWorldCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Round", "Flat" }));
        roundOrFlatWorldCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                roundOrFlatWorldComboItemStateChanged(evt);
            }
        });

        removeBathyCheck.setBackground(new java.awt.Color(255, 255, 255));
        removeBathyCheck.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.removeBathyCheck.text")); // NOI18N
        removeBathyCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBathyCheckActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.jLabel3.text")); // NOI18N

        verticalExaggerationSlider.setBackground(new java.awt.Color(255, 255, 255));
        verticalExaggerationSlider.setMaximum(20);
        verticalExaggerationSlider.setMinimum(1);
        verticalExaggerationSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                verticalExaggerationSliderStateChanged(evt);
            }
        });
        verticalExaggerationSlider.addMouseWheelListener(new GUIUtils.JSliderMouseWheelListener());

        verticalExaggerationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        verticalExaggerationLabel.setText(org.openide.util.NbBundle.getMessage(MiscellaneousPanel.class, "MiscellaneousPanel.verticalExaggerationLabel.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verticalExaggerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verticalExaggerationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roundOrFlatWorldCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(removeBathyCheck))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addComponent(roundOrFlatWorldCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(removeBathyCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verticalExaggerationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verticalExaggerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void opacitySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_opacitySliderStateChanged
        controller.setPolygonsOpacity(opacitySlider.getValue() / 100d);
        opacityLabel.setText(Double.toString(opacitySlider.getValue() / 100d));
    }//GEN-LAST:event_opacitySliderStateChanged

    private void displayNotTiledCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayNotTiledCheckActionPerformed
        controller.setDisplayNotTiled(displayNotTiledCheck.isSelected());
    }//GEN-LAST:event_displayNotTiledCheckActionPerformed

    private void roundOrFlatWorldComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_roundOrFlatWorldComboItemStateChanged
        int selectedIndex = roundOrFlatWorldCombo.getSelectedIndex();
        enableFlatGlobe(selectedIndex == ROUND ? false : true);
    }//GEN-LAST:event_roundOrFlatWorldComboItemStateChanged

    private void removeBathyCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBathyCheckActionPerformed
        if(isUseBathyFilter) {
            wwd.getModel().getGlobe().setElevationModel(defaultElevationModel);
        } else {
            wwd.getModel().getGlobe().setElevationModel(noDepthModel);
        }
        
        isUseBathyFilter = !isUseBathyFilter;
        wwd.redraw();
    }//GEN-LAST:event_removeBathyCheckActionPerformed

    private void verticalExaggerationSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_verticalExaggerationSliderStateChanged
        wwd.getSceneController().setVerticalExaggeration(verticalExaggerationSlider.getValue()*1d);
        verticalExaggerationLabel.setText("x" + Integer.toString(verticalExaggerationSlider.getValue()));
    }//GEN-LAST:event_verticalExaggerationSliderStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox displayNotTiledCheck;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel opacityLabel;
    private javax.swing.JSlider opacitySlider;
    private javax.swing.JCheckBox removeBathyCheck;
    private javax.swing.JComboBox roundOrFlatWorldCombo;
    private javax.swing.JLabel verticalExaggerationLabel;
    private javax.swing.JSlider verticalExaggerationSlider;
    // End of variables declaration//GEN-END:variables
}
