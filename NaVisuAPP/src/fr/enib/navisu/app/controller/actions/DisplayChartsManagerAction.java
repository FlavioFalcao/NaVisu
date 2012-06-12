/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.view.dialogs.ChartsManagerDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
id = "fr.enib.navisu.app.controller.actions.DisplayChartsManagerAction")
@ActionRegistration(iconBase = "fr/enib/navisu/app/controller/actions/update-db.png",
displayName = "#CTL_DisplayChartsManagerAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 0),
    @ActionReference(path = "Shortcuts", name = "D-U")
})
@Messages("CTL_DisplayChartsManagerAction=Charts manager")
public final class DisplayChartsManagerAction implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(DisplayChartsManagerAction.class.getName());
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ChartsManagerDialog.getInstance().setVisible(true);
    }
}
