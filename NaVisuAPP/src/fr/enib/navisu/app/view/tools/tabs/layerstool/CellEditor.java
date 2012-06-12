/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.view.tools.tabs.layerstool;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

/**
 *
 * @author jordan
 */
public class CellEditor implements TreeCellEditor {
    
    private JPanel panel;
    private Icon earthIcon;
    private JCheckBox checkBox;
    private Icon folderIcon;
    
    public CellEditor() {
        earthIcon = new ImageIcon(getClass().getResource("/fr/enib/navisu/app/view/tools/tabs/layerstool/earth-icon.png"));
        folderIcon = new ImageIcon(getClass().getResource("/fr/enib/navisu/app/view/tools/tabs/layerstool/folder.gif"));
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if(node.getUserObject() instanceof CheckBoxNode) {
            panel = new JPanel(new BorderLayout());
            final CheckBoxNode checkBoxNode = (CheckBoxNode) node.getUserObject();
            final JLabel label = new JLabel(earthIcon);
            checkBox = checkBoxNode.getCheckbox();
            panel.add(label, BorderLayout.WEST);
            panel.add(checkBox, BorderLayout.EAST);

            return panel;
        } else {
            
            String label = (String) node.getUserObject();
            return new JLabel(label, folderIcon, JLabel.CENTER);
        }
    }

    @Override
    public Object getCellEditorValue() {
        return this;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) evt;
            if (event.getClickCount() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return false;
    }
    @Override
    public void cancelCellEditing() {}
    @Override
    public void addCellEditorListener(CellEditorListener l) {}
    @Override
    public void removeCellEditorListener(CellEditorListener l) {}
}
