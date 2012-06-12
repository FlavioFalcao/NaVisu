package fr.enib.navisu.app.view.settings.charts;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.common.utils.GUIUtils;
import fr.enib.navisu.common.utils.Utils;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;
import org.openide.windows.InputOutput;

public final class ChartsPanel extends javax.swing.JPanel {

    public static final InputOutput IO = AppTopComponent.io();
    public static final Preferences PREFS = NbPreferences.forModule(AppTopComponent.class);
    private final ChartsOptionsPanelController controller;
    // Data
    public static final String KEY_STRING_CACHE = "keyStringCache";
    // View
    public static final String KEY_DOUBLE_LAT = "keyDoubleLat";
    public static final String KEY_DOUBLE_LON = "keyDoubleLon";
    public static final String KEY_INT_ELE = "keyDoubleEle";
    
    ChartsPanel(ChartsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        initListeners();
    }

    private void initListeners() {
        // Listen to changes in form fields and call CONTROLLER.changed()
        chooseCacheBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent ae) {
                File newCache = GUIUtils.showDirectoryChooser(cacheTF.getText());
                if(newCache != null) {
                    cacheTF.setText(newCache.getAbsolutePath());
                    //controller.changed();
                }
            }
        });
        
        currentEyePosBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent ae) {
                Position current = AppTopComponent.wwd().getView().getCurrentEyePosition();
                latTF.setText(Utils.formatDouble(current.latitude.degrees, 4));
                lonTF.setText(Utils.formatDouble(current.longitude.degrees, 4));
                eleTF.setText(Integer.toString((int)current.elevation));
                //controller.changed();
            }
        });
    }
    
    void load() { // Read settings and initialize GUI
        // DATA
        cacheTF.setText(PREFS.get(KEY_STRING_CACHE, WorldWind.getDataFileStore().getWriteLocation().getAbsolutePath()));
        // VIEW
        latTF.setText(Utils.formatDouble(PREFS.getDouble(KEY_DOUBLE_LAT, 0.0), 4));
        lonTF.setText(Utils.formatDouble(PREFS.getDouble(KEY_DOUBLE_LON, 0.0), 4));
        eleTF.setText(Integer.toString(PREFS.getInt(KEY_INT_ELE, 1500000)));
    }

    void store() { // Store modified settings
        // DATA
        PREFS.put(KEY_STRING_CACHE, cacheTF.getText());
        // VIEW
        PREFS.putDouble(KEY_DOUBLE_LAT, Double.parseDouble(latTF.getText().replaceAll(",", ".")));
        PREFS.putDouble(KEY_DOUBLE_LON, Double.parseDouble(lonTF.getText().replaceAll(",", ".")));
        PREFS.putInt(KEY_INT_ELE, Integer.valueOf(eleTF.getText()));
    }

    boolean valid() {
        boolean rep = true;
        // DATA
        rep &= Files.exists(Paths.get(cacheTF.getText()));
        // VIEW
        rep &= Utils.isDouble(latTF.getText().replaceAll(",", "."));
        rep &= Utils.isDouble(lonTF.getText().replaceAll(",", "."));
        rep &= Utils.isInteger(eleTF.getText());
        
        return rep;
    }
    
    private void print(Object s) {
        IO.getOut().println("[ChartsPanel] " + s.toString());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cacheTF = new javax.swing.JTextField();
        chooseCacheBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        currentEyePosBtn = new javax.swing.JButton();
        latTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lonTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        eleTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 1, 12))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jLabel1.text")); // NOI18N

        cacheTF.setText(org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.cacheTF.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chooseCacheBtn, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.chooseCacheBtn.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cacheTF, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chooseCacheBtn))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(cacheTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(chooseCacheBtn))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 1, 12))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(currentEyePosBtn, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.currentEyePosBtn.text")); // NOI18N

        latTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        latTF.setText(org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.latTF.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jLabel5.text")); // NOI18N

        lonTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lonTF.setText(org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.lonTF.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jLabel6.text")); // NOI18N

        eleTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        eleTF.setText(org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.eleTF.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(ChartsPanel.class, "ChartsPanel.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(latTF, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lonTF, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eleTF, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentEyePosBtn)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(currentEyePosBtn)
                .addComponent(latTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5)
                .addComponent(lonTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6)
                .addComponent(eleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cacheTF;
    private javax.swing.JButton chooseCacheBtn;
    private javax.swing.JButton currentEyePosBtn;
    private javax.swing.JTextField eleTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField latTF;
    private javax.swing.JTextField lonTF;
    // End of variables declaration//GEN-END:variables
}
