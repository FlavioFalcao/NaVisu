/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.utils;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 24/04/2012
 */
public class NbUtils {
    
    /**
     * 
     * @param message
     * @param messageType 
     * @see NotifyDescriptor
     */
    public static void notifyMessage(String message, int messageType) {
        NotifyDescriptor.Message nd = new NotifyDescriptor.Message(message, messageType);
        DialogDisplayer.getDefault().notify(nd);
    }
    
    /**
     * 
     * @param message
     * @return 
     */
    public static boolean showConfirmationMessage(String message) {
        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(message, NotifyDescriptor.QUESTION_MESSAGE);
        Object returnedValue = DialogDisplayer.getDefault().notify(nd);
        
        if (returnedValue == NotifyDescriptor.OK_OPTION) {
            return true;
        }
        
        return false;
    }

    private NbUtils() {}
}
