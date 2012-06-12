/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.model;

import java.text.Collator;
import java.util.*;
import javax.swing.AbstractListModel;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SortedListModel extends AbstractListModel {

    private SortedSet model;

    public SortedListModel() {
        model = new TreeSet(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                String str1 = o1.toString();
                String str2 = o2.toString();
                Collator collator = Collator.getInstance();
                return collator.compare(str1, str2);
            }
        });
    }
    
    public SortedListModel(Comparator comparator) {
        model = new TreeSet(comparator);
    }

    //<editor-fold defaultstate="collapsed" desc="ListModel methods">
    @Override
    public int getSize() {
        return model.size();
    }
    
    @Override
    public Object getElementAt(int index) {
        return model.toArray()[index];
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Other methods">
    public void add(Object element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }
    
    public void addAll(Object elements[]) {
        Collection c = Arrays.asList(elements);
        model.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }
    
    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }
    
    public boolean contains(Object element) {
        return model.contains(element);
    }
    
    public Object firstElement() {
        return model.first();
    }
    
    public Iterator iterator() {
        return model.iterator();
    }
    
    public Object lastElement() {
        return model.last();
    }
    
    public boolean removeElement(Object element) {
        boolean removed = model.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }
    //</editor-fold>
}
