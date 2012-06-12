/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.view.toolbar.displaynontiled.DisplayNonTiledPresenter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools", id = "fr.enib.navisu.app.controller.actions.DisplayNonTiledAction")
@ActionRegistration(iconBase = "fr/enib/navisu/app/controller/actions/regle.png",
displayName = "#CTL_DisplayNonTiledAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navisu", position = 3333)
})
@Messages("CTL_DisplayNonTiledAction=Afficher les polygones non tuilées")
public final class DisplayNonTiledAction extends AbstractAction implements Presenter.Toolbar {

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public Component getToolbarPresenter() {
        return DisplayNonTiledPresenter.getInstance();
    }
}
