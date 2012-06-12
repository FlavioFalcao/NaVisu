/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.view.toolbar.gazetteer.GazetteerPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools", id = "fr.enib.navisu.app.controller.actions.GazetteerAction")
@ActionRegistration(
    iconBase = "fr/enib/navisu/app/view/statusbar/magnify_glass.gif",
displayName = "#CTL_GazetteerAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navisu", position = 200)
})
@Messages("CTL_GazetteerAction=Search...")
public final class GazetteerAction extends AbstractAction implements Presenter.Toolbar {
    
    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public Component getToolbarPresenter() {
        JPanel gazetteerPanel = null;
        try {
            gazetteerPanel = new GazetteerPanel(AppTopComponent.wwd(), null);
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return gazetteerPanel;
    }
}
