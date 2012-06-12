/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.model;

import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 31/05/2012
 */
public class LayerTreeListModel  {

    private static final Logger LOGGER = Logger.getLogger(LayerTreeListModel.class.getName());
    private DefaultTreeModel model;
    

    public LayerTreeListModel(TreeNode t) {
        model = new DefaultTreeModel(t);
        
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }
}
