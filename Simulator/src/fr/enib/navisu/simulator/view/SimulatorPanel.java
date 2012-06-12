package fr.enib.navisu.simulator.view;

import fr.enib.navisu.common.utils.GUIUtils;
import fr.enib.navisu.common.utils.Utils;
import fr.enib.navisu.simulator.Simulator;
import fr.enib.navisu.simulator.events.SimulatorEvent;
import fr.enib.navisu.simulator.events.SimulatorEventListener;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.formats.gpx.GpxReader;
import gov.nasa.worldwind.formats.gpx.GpxWriter;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.util.measure.LengthMeasurer;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.openide.util.Exceptions;
import org.openide.windows.InputOutput;
import org.xml.sax.SAXException;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 01/06/2012
 */
public class SimulatorPanel extends javax.swing.JPanel implements SimulatorEventListener {

    private InputOutput IO;
    private final WorldWindow wwd;
    private final Simulator simulator;
    private LineBuilder lineBuilder;
    private LengthMeasurer lengthMeasurer;
    private JFileChooser gpxFileChooser;

    public SimulatorPanel(final WorldWindow wwd, final Simulator simulator) {
        initComponents();
        this.wwd = wwd;
        this.simulator = simulator;
        this.simulator.addEventListener(this);
        initControlsPanelComponents();
        initTrajectoryPanelComponents();
        initGpxPanelComponents();
    }

    /**
     * 
     */
    private void initControlsPanelComponents() {

        // START BUTTON
        startBtn.setSelected(false);
        startBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (startBtn.isSelected()) {
                    if(lineBuilder.size() > 1) {
                        simulator.start();
                        stopBtn.setSelected(false);
                        pauseBtn.setSelected(false);
                        pauseBtn.setEnabled(true);
                        trajectoryPanel.setEnabled(false);
                        activeProgressBar(true);
                        setTrajectoryPanelEnabled(false);
                        setGpxPanelEnabled(false);
                    } else {
                        startBtn.setSelected(false);
                    }
                }
            }
        });

        // STOP BUTTON
        stopBtn.setSelected(false);
        stopBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (stopBtn.isSelected()) {
                    if (simulator.isRunning()) {
                        simulator.stop();
                    }
                    startBtn.setSelected(false);
                    pauseBtn.setSelected(false);
                    pauseBtn.setEnabled(false);
                    activeProgressBar(false);
                    setTrajectoryPanelEnabled(true);
                    setGpxPanelEnabled(true);
                }
            }
        });

        // PAUSE BUTTON
        pauseBtn.setSelected(false);
        pauseBtn.setEnabled(false);
        pauseBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (pauseBtn.isSelected()) {
                    simulator.pause();
                    startBtn.setSelected(false);
                    stopBtn.setSelected(false);
                    trajectoryPanel.setEnabled(true);
                    activeProgressBar(false);
                }
            }
        });

        // SPEED TEXTFIELD
        speedTF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int oldPeriod = simulator.getPeriod();
                try {
                    
                    int newPeriod = Integer.valueOf(speedTF.getText());
                    
                    if (newPeriod <= 0) {
                        if (newPeriod < 0) {
                            newPeriod *= -1;
                        }
                        if (newPeriod == 0) {
                            newPeriod = 1;
                        }
                        speedTF.setText(Integer.toString(newPeriod));
                    }

                    simulator.setPeriod(newPeriod);
                    
                } catch (NumberFormatException ex) {
                    speedTF.setText(Integer.toString(oldPeriod));
                }
            }
        });
        
        activeProgressBar(false);
    }

    /**
     * 
     */
    private void initTrajectoryPanelComponents() {
        lengthMeasurer = new LengthMeasurer();
        lineBuilder = new LineBuilder(wwd);
        lineBuilder.addPropertyChangeListener(new PropertyChangeListener() { @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePositions();
            }
        });
        
        // NEW TRAJECTORY BUTTON
        newTrajBtn.setEnabled(true);
        newTrajBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                lineBuilder.clear();
                lineBuilder.setArmed(true);
                pauseTrajBtn.setText("pause");
                pauseTrajBtn.setEnabled(true);
                endTrajBtn.setEnabled(true);
                newTrajBtn.setEnabled(false);
                ((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                setControlsPanelEnabled(false);
                setGpxPanelEnabled(false);
                simulator.setAllowInterpolation(true);
            }
        });
       
        // PAUSE TRAJECTORY BUTTON
        pauseTrajBtn.setEnabled(false);
        pauseTrajBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                lineBuilder.setArmed(!lineBuilder.isArmed());
                pauseTrajBtn.setText(!lineBuilder.isArmed() ? "Resume" : "Pause");
                ((Component) wwd).setCursor(Cursor.getDefaultCursor());
            }
        });
        
        // END TRAJECTORY BUTTON
        endTrajBtn.setEnabled(false);
        endTrajBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                lineBuilder.setArmed(false);
                newTrajBtn.setEnabled(true);
                pauseTrajBtn.setEnabled(false);
                pauseTrajBtn.setText("Pause");
                endTrajBtn.setEnabled(false);
                ((Component) wwd).setCursor(Cursor.getDefaultCursor());
                updatePositions();
                setControlsPanelEnabled(true);
                setGpxPanelEnabled(true);
            }
        });
        
        clearTrajBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                lineBuilder.clear();
                simulator.clearPositions();
                updatePositions();
            }
        });

        setControlsPanelEnabled(false);
    }

    /**
     * 
     */
    private void initGpxPanelComponents() {
        
        gpxFileChooser = new JFileChooser();
        gpxFileChooser.setFileFilter(new FileNameExtensionFilter("GPX track file", "gpx", "GPX"));
        
        gpxExportBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (simulator.getPositions().size() > 2) {
                    gpxFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (gpxFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String path = gpxFileChooser.getSelectedFile().getAbsolutePath();
                        try {
                            path += path.endsWith(".gpx") ? "" : ".gpx";
                            getIO().getOut().println("Export : " + path);
                            GpxWriter writer = new GpxWriter(path);
                            writer.writeTrack(simulator.getTrack());
                        } catch (IOException | ParserConfigurationException | TransformerException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                } else {
                    GUIUtils.showWarningDialog("Gpx Export", "No positions to export !");
                }
            }
        });
        
        gpxImportBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (gpxFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        GpxReader reader = new GpxReader();
                        reader.readFile(gpxFileChooser.getSelectedFile().getAbsolutePath());
                        Iterator<Position> positions = reader.getTrackPositionIterator();
                        ArrayList<Position> pos = new ArrayList<>();
                        simulator.clearPositions();
                        Position tmp;
                        while(positions.hasNext()) {
                            tmp = positions.next();
                            simulator.addPosition(tmp);
                            pos.add(tmp);
                        }
                        setControlsPanelEnabled(true);
                        lineBuilder.setPositions(pos);
                        simulator.setAllowInterpolation(false);
                    } catch (ParserConfigurationException | SAXException | IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });
    }
    
    private void updatePositions() {
        
        ArrayList<Position> positions = new ArrayList<>();
        for(Position pos : lineBuilder.getLine().getPositions()) {
            //String las = String.format("Lat %7.4f\u00B0", pos.getLatitude().getDegrees());
            //String los = String.format("Lon %7.4f\u00B0", pos.getLongitude().getDegrees());
            positions.add(pos);
        }
        
        simulator.setPositions(positions);
        lengthMeasurer.setPositions(positions);
        double length = lengthMeasurer.getLength(wwd.getModel().getGlobe());
        length = length < 0.0 ? 0.0 : length;
        lengthLbl.setText(Utils.formatDouble(length, 2) + " meters");
        nbPointsLbl.setText(Integer.toString(positions.size()));
    }

    private void activeProgressBar(boolean active) {
        if (active) {
            progessBar.setIndeterminate(true);
        } else {
            progessBar.setIndeterminate(false);
            progessBar.setValue(0);
        }
    }
    
    @Override
    public void updatePosition(SimulatorEvent event) {
        if(event.isLastPosition()) {
            stopBtn.setSelected(true);
            pauseBtn.setSelected(false);
            startBtn.setSelected(false);
            activeProgressBar(false);
        }
    }
    
    public void setControlsPanelEnabled(boolean enabled) {
        speedTF.setEnabled(enabled);
        stopBtn.setEnabled(enabled);
        pauseBtn.setEnabled(enabled);
        startBtn.setEnabled(enabled);
        clearTrajBtn.setEnabled(enabled);
        controlsPanel.setEnabled(enabled);
        if (enabled) {
            pauseBtn.setEnabled(!enabled);
        }
    }
    
    public void setTrajectoryPanelEnabled(boolean enabled) {
        newTrajBtn.setEnabled(enabled);
        pauseTrajBtn.setEnabled(enabled);
        endTrajBtn.setEnabled(enabled);
        clearTrajBtn.setEnabled(enabled);
        trajectoryPanel.setEnabled(enabled);
        if (enabled) {
            pauseTrajBtn.setEnabled(!enabled);
            endTrajBtn.setEnabled(!enabled);
        }
    }

    public void setGpxPanelEnabled(boolean enabled) {
        gpxExportBtn.setEnabled(enabled);
        gpxImportBtn.setEnabled(enabled);
        gpxPanel.setEnabled(enabled);
    }
    
    public InputOutput getIO() {
        return Simulator.getIO();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        trajectoryPanel = new javax.swing.JPanel();
        trajControlPanel = new javax.swing.JPanel();
        newTrajBtn = new javax.swing.JButton();
        pauseTrajBtn = new javax.swing.JButton();
        endTrajBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lengthLbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nbPointsLbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        clearTrajBtn = new javax.swing.JButton();
        controlsPanel = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        stopBtn = new javax.swing.JToggleButton();
        pauseBtn = new javax.swing.JToggleButton();
        startBtn = new javax.swing.JToggleButton();
        speedPanel = new javax.swing.JPanel();
        speedLbl = new javax.swing.JLabel();
        speedTF = new javax.swing.JTextField();
        progressPanel = new javax.swing.JPanel();
        progessBar = new javax.swing.JProgressBar();
        gpxPanel = new javax.swing.JPanel();
        gpxExportBtn = new javax.swing.JButton();
        gpxImportBtn = new javax.swing.JButton();
        helpBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        trajectoryPanel.setBackground(new java.awt.Color(255, 255, 255));
        trajectoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.trajectoryPanel.border.title"))); // NOI18N
        trajectoryPanel.setPreferredSize(new java.awt.Dimension(275, 282));

        trajControlPanel.setBackground(new java.awt.Color(255, 255, 255));

        newTrajBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.newTrajBtn.text")); // NOI18N
        trajControlPanel.add(newTrajBtn);

        pauseTrajBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.pauseTrajBtn.text")); // NOI18N
        trajControlPanel.add(pauseTrajBtn);

        endTrajBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.endTrajBtn.text")); // NOI18N
        trajControlPanel.add(endTrajBtn);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.jLabel2.text")); // NOI18N

        lengthLbl.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.lengthLbl.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.jLabel3.text")); // NOI18N

        nbPointsLbl.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.nbPointsLbl.text")); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        clearTrajBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.clearTrajBtn.text")); // NOI18N
        jPanel1.add(clearTrajBtn);

        javax.swing.GroupLayout trajectoryPanelLayout = new javax.swing.GroupLayout(trajectoryPanel);
        trajectoryPanel.setLayout(trajectoryPanelLayout);
        trajectoryPanelLayout.setHorizontalGroup(
            trajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(trajControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
            .addGroup(trajectoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(trajectoryPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lengthLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(trajectoryPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nbPointsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        trajectoryPanelLayout.setVerticalGroup(
            trajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trajectoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(trajControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(trajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lengthLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trajectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nbPointsLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        controlsPanel.setBackground(new java.awt.Color(255, 255, 255));
        controlsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.controlsPanel.border.title"))); // NOI18N
        controlsPanel.setPreferredSize(new java.awt.Dimension(175, 209));

        buttonsPanel.setBackground(new java.awt.Color(255, 255, 255));

        stopBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fr/enib/navisu/simulator/view/images/stop-48.png"))); // NOI18N
        stopBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.stopBtn.text")); // NOI18N
        stopBtn.setPreferredSize(new java.awt.Dimension(65, 55));
        buttonsPanel.add(stopBtn);

        pauseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fr/enib/navisu/simulator/view/images/pause-48.png"))); // NOI18N
        pauseBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.pauseBtn.text")); // NOI18N
        pauseBtn.setPreferredSize(new java.awt.Dimension(65, 55));
        buttonsPanel.add(pauseBtn);

        startBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fr/enib/navisu/simulator/view/images/start-48.png"))); // NOI18N
        startBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.startBtn.text")); // NOI18N
        startBtn.setPreferredSize(new java.awt.Dimension(65, 55));
        buttonsPanel.add(startBtn);

        speedPanel.setBackground(new java.awt.Color(255, 255, 255));

        speedLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        speedLbl.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.speedLbl.text")); // NOI18N
        speedPanel.add(speedLbl);

        speedTF.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        speedTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        speedTF.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.speedTF.text")); // NOI18N
        speedTF.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        speedTF.setPreferredSize(new java.awt.Dimension(50, 21));
        speedPanel.add(speedTF);

        progressPanel.setBackground(new java.awt.Color(255, 255, 255));

        progessBar.setIndeterminate(true);
        progressPanel.add(progessBar);

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(speedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
            .addComponent(progressPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addComponent(speedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gpxPanel.setBackground(new java.awt.Color(255, 255, 255));
        gpxPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.gpxPanel.border.title"))); // NOI18N

        gpxExportBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.gpxExportBtn.text")); // NOI18N
        gpxPanel.add(gpxExportBtn);

        gpxImportBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.gpxImportBtn.text")); // NOI18N
        gpxPanel.add(gpxImportBtn);

        helpBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fr/enib/navisu/simulator/view/images/help-24.png"))); // NOI18N
        helpBtn.setText(org.openide.util.NbBundle.getMessage(SimulatorPanel.class, "SimulatorPanel.helpBtn.text")); // NOI18N
        helpBtn.setPreferredSize(new java.awt.Dimension(35, 35));
        helpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
            .addComponent(trajectoryPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(gpxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(helpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trajectoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gpxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void helpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
        
        new SimulatorHelpDialog().setVisible(true);
        
    }//GEN-LAST:event_helpBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton clearTrajBtn;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JButton endTrajBtn;
    private javax.swing.JButton gpxExportBtn;
    private javax.swing.JButton gpxImportBtn;
    private javax.swing.JPanel gpxPanel;
    private javax.swing.JButton helpBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lengthLbl;
    private javax.swing.JLabel nbPointsLbl;
    private javax.swing.JButton newTrajBtn;
    private javax.swing.JToggleButton pauseBtn;
    private javax.swing.JButton pauseTrajBtn;
    private javax.swing.JProgressBar progessBar;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JLabel speedLbl;
    private javax.swing.JPanel speedPanel;
    private javax.swing.JTextField speedTF;
    private javax.swing.JToggleButton startBtn;
    private javax.swing.JToggleButton stopBtn;
    private javax.swing.JPanel trajControlPanel;
    private javax.swing.JPanel trajectoryPanel;
    // End of variables declaration//GEN-END:variables

}
