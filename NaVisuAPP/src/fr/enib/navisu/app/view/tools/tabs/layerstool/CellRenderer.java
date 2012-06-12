/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */
package fr.enib.navisu.app.view.tools.tabs.layerstool;


import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.common.utils.WWUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 24/05/2012
 */
public class CellRenderer extends DefaultTreeCellRenderer {
    
    private Icon folderIcon;
    private Icon earthIcon;
    private JPanel panel;
    private JCheckBox checkBox;
    
    public CellRenderer() {
        folderIcon = new ImageIcon(getClass().getResource("/fr/enib/navisu/app/view/tools/tabs/layerstool/folder.gif"));
        earthIcon = new ImageIcon(getClass().getResource("/fr/enib/navisu/app/view/tools/tabs/layerstool/earth-icon.png"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree jtree, Object o, boolean bln, boolean bln1, boolean bln2, int i, boolean bln3) {
        super.getTreeCellRendererComponent(jtree, o, bln, bln1, bln2, i, bln3);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)o;
        if(node.getUserObject() instanceof CheckBoxNode) {
         
            panel = new JPanel(new BorderLayout());
            final CheckBoxNode checkBoxNode = (CheckBoxNode) node.getUserObject();
  
            JLabel label = new JLabel(earthIcon);
            checkBox = checkBoxNode.getCheckbox();
            
            panel.add(label, BorderLayout.WEST);
            panel.add(checkBox, BorderLayout.EAST);
            
            return panel;
            
        } else {
            
            String label = (String) node.getUserObject();
            return new JLabel(label, folderIcon, JLabel.CENTER);
        }
    }
    
}
