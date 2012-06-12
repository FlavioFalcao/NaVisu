/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.common.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 12/04/2012
 */
public class GUIUtils {
    
    private GUIUtils() {}
    
    public static class JSliderMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent evt) {
            if(evt.getSource() instanceof JSlider) {
                JSlider slider = (JSlider)evt.getSource();
                slider.setValue(slider.getValue() + evt.getWheelRotation());
            }
        }
    }
    
    public static void centerWindowInScreen(Window window) {
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension w = window.getSize();
        Point newLoc = new Point(s.width / 2 - w.width / 2, s.height / 2 - w.height / 2);
        window.setLocation(newLoc);
    }
    
    public static void showSimpleMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    
    public static void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showWarningDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
    }
    
    //<editor-fold defaultstate="collapsed" desc="JFileChooser">
    private static final JFileChooser JFC = new JFileChooser();
    
    public static File showDirectoryChooser(String currentDirectoryPath) {
        
        if(currentDirectoryPath != null)
            JFC.setCurrentDirectory(Paths.get(currentDirectoryPath).toFile());
        
        JFC.setMultiSelectionEnabled(false);
        JFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(JFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return JFC.getSelectedFile();
        }
        
        return null;
    }
    
    public static File showFileChooser(String currentDirectoryPath) {
        
        if(currentDirectoryPath != null)
            JFC.setCurrentDirectory(Paths.get(currentDirectoryPath).toFile());
        
        JFC.setMultiSelectionEnabled(false);
        JFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(JFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return JFC.getSelectedFile();
        }
        
        return null;
    }
    
    public static File[] showFilesChooser(String currentDirectoryPath, boolean acceptDirectories) {
        
        if(currentDirectoryPath != null)
            JFC.setCurrentDirectory(Paths.get(currentDirectoryPath).toFile());
        
        JFC.setMultiSelectionEnabled(true);
        JFC.setFileSelectionMode(acceptDirectories
                ? JFileChooser.FILES_AND_DIRECTORIES
                : JFileChooser.FILES_ONLY);
        
        if(JFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return JFC.getSelectedFiles();
        }
        
        return null;
    }
    
    public static File[] showDirectoriesChooser(String currentDirectoryPath) {
        
        if(currentDirectoryPath != null)
            JFC.setCurrentDirectory(Paths.get(currentDirectoryPath).toFile());
        
        JFC.setMultiSelectionEnabled(true);
        JFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(JFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return JFC.getSelectedFiles();
        }
        
        return null;
    }
    
    public static File[] showDirectoriesChooser() {
        return showDirectoriesChooser(null);
    }
    //</editor-fold>
}
