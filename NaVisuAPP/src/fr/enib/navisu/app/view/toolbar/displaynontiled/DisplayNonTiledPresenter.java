/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.view.toolbar.displaynontiled;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.view.explorer.ChartViewTopComponent;
import fr.enib.navisu.charts.controller.ChartsController;
import org.openide.util.Lookup;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 26/05/2012
 */
public class DisplayNonTiledPresenter extends javax.swing.JPanel {
    
    private static DisplayNonTiledPresenter _instance = null;
    
    private final ChartsController controller = AppTopComponent.controller();
    
    public static DisplayNonTiledPresenter getInstance() {
        if(_instance == null) {
            _instance = new DisplayNonTiledPresenter();
        }
        return _instance;
    }
    
    private DisplayNonTiledPresenter() {
        initComponents();

        choiceCheckBox.setSelected(controller.isDisplayNotTiled());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        choiceCheckBox = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.BorderLayout());

        choiceCheckBox.setText(org.openide.util.NbBundle.getMessage(DisplayNonTiledPresenter.class, "DisplayNonTiledPresenter.choiceCheckBox.text")); // NOI18N
        choiceCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choiceCheckBoxActionPerformed(evt);
            }
        });
        add(choiceCheckBox, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void choiceCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choiceCheckBoxActionPerformed

        controller.setDisplayNotTiled(choiceCheckBox.isSelected());
        ChartViewTopComponent chartView = Lookup.getDefault().lookup(ChartViewTopComponent.class);
        chartView.displayNonTiled(choiceCheckBox.isSelected());
    }//GEN-LAST:event_choiceCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox choiceCheckBox;
    // End of variables declaration//GEN-END:variables
}
