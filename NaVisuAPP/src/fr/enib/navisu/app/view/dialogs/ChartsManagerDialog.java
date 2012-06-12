package fr.enib.navisu.app.view.dialogs;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.actions.UpdateDatabaseAction;
import fr.enib.navisu.app.model.ChartsFoldersModel;
import fr.enib.navisu.app.model.SortedListModel;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.common.utils.GUIUtils;
import fr.enib.navisu.common.xml.XMLReader;
import fr.enib.navisu.common.xml.XMLWriter;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.xml.bind.JAXBException;
import org.openide.util.Exceptions;
import org.openide.windows.InputOutput;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 25/05/2012
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ChartsManagerDialog extends javax.swing.JDialog {

    private static ChartsManagerDialog _instance = null;
    
    private final InputOutput IO = AppTopComponent.io();
    
    private SortedListModel foldersListModel;
    private ChartsFoldersModel model;
    private final Path chartsFoldersPersistenceFilename = Paths.get(ChartsFoldersModel.XML_FILE_PATH);
    private boolean hasChanged;
    
    public static ChartsManagerDialog getInstance() {
        if(_instance == null) {
            _instance = new ChartsManagerDialog();
        }
        
        return _instance;
    }
    
    private ChartsManagerDialog() {
        super((Frame)null, true);
        initComponents();
        
        initialize();
    }

    private void initialize() {
        
        GUIUtils.centerWindowInScreen(this);
        foldersListModel = new SortedListModel();
        
        if(Files.exists(chartsFoldersPersistenceFilename)) {
            try {
                model = (ChartsFoldersModel) XMLReader.read(ChartsFoldersModel.class, chartsFoldersPersistenceFilename.toString());
            } catch (IOException | JAXBException ex) {
                IO.getErr().println(ex.getMessage());
            }
            
            for(String f : model.getChartsFolders()) {
                foldersListModel.add(f);
            }
        } else {
            model = new ChartsFoldersModel();
        }
        
        foldersList.setModel(foldersListModel);
        foldersListModel.addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {}
            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {
                model.clear();
                Iterator<String> it = foldersListModel.iterator();
                while(it.hasNext()) {
                    model.addChartsFolder(it.next());
                }
                hasChanged = true;
            }
        });
        
        hasChanged = false;
    }
    
    private void print(String s) {
        IO.getOut().println("[ChartsManagerDialog] " + s);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        foldersList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        forceCheckBox = new javax.swing.JCheckBox();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(foldersList);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        addButton.setText(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel2.add(addButton, new java.awt.GridBagConstraints());

        removeButton.setText(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.removeButton.text")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jPanel2.add(removeButton, new java.awt.GridBagConstraints());

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.jPanel3.border.title"))); // NOI18N

        forceCheckBox.setText(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.forceCheckBox.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(forceCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(forceCheckBox))
        );

        cancelButton.setText(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText(org.openide.util.NbBundle.getMessage(ChartsManagerDialog.class, "ChartsManagerDialog.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        
        File[] newFolders = GUIUtils.showDirectoriesChooser();
        if(newFolders != null) {
            for(int i=0; i<newFolders.length; i++) {
                foldersListModel.add(newFolders[i].getAbsolutePath());
                model.addChartsFolder(newFolders[i].getAbsolutePath());
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed

        int[] sel = foldersList.getSelectedIndices();
        int length = sel.length;

        for (int index = length - 1; index >= 0; index--) {
            model.removeChartsFolder((String)foldersListModel.getElementAt(sel[index]));
            foldersListModel.removeElement((String)foldersListModel.getElementAt(sel[index]));
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
        // Mise à jour (ou création) du fichier de persistence XML
        try {
            XMLWriter.write(model, chartsFoldersPersistenceFilename.toString());
        } catch (JAXBException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        // Si besoin, reconstruction de la base de données
        if(hasChanged || forceCheckBox.isSelected()) {
            print("Update database...");
            new UpdateDatabaseAction().actionPerformed(null);
        }
        
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JList foldersList;
    private javax.swing.JCheckBox forceCheckBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables

}
