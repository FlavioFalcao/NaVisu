package fr.enib.navisu.common.event;

/**
 *
 * @author Thibault
 */
public interface EventProvider<T> {
    
    /**
     * 
     * @param listener The event listener to add
     */
    public void addEventListener(T listener);
    
    /**
     * 
     * @param listener The event listener to remove
     */
    public void removeEventListener(T listener);
    
    /**
     * 
     */
    public void fireEventListeners();
}
