package fr.enib.navisu.common.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * 
 * @author Thibault Pensec and Jordan Mens
 * @date 12 mars 2012
 */

public class Catalog<T> {
    
    private static final Logger LOGGER = Logger.getLogger(Catalog.class.getName()); 
    
    private List<T> elements;    
    
    /**
     * 
     */
    public Catalog() {
        elements = new ArrayList<>();
    }
    
    /**
     * @param elements 
     */
    public Catalog(List<T> elements) {
        this.elements = elements;
    }
    
    /**
     * @param element
     * @return 
     */
    public void add(T element) {
        elements.add(element);
    }
    
    public void addAll(List<T> elmts) {
        elements.addAll(elmts);
    }
    
    /**
     * @param element
     * @return 
     */
    public void remove(T element) {
        elements.remove(element);
    }
    
    /**
     * 
     * @param elmts
     * @return Current instance of CatalogDAO class
     */
    public void removeAll(List<T> elmts) {
        elements.removeAll(elmts);
    }
    
    public void clear() {
        elements.clear();
    }

    /**
     * @return the charts
     */
    public List<T> list() {
        return elements;
    }
    
    /**
     * 
     * @param index
     * @return 
     */
    public T get(int index) {
        return elements.get(index);
    }
    
    /**
     * 
     * @param index
     * @param element
     * @return 
     */
    public void set(int index, T element) {
        elements.set(index, element);
    }
    
    /**
     * 
     * @param elmt
     * @return The first index of T elmt
     */
    public int indexOf(T elmt) {
        return elements.indexOf(elmt);
    }
    
    /**
     * 
     * @return Number of elements
     */
    public int size() {
        return elements.size();
    }
    
    public void display() {
        for(T e : elements) {
            LOGGER.log(Level.INFO, e.toString());
        }
    }
}
