/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.common.dao;

import java.util.Collection;

/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 02/04/2012
 * 
 * CRUD interface (Create, Read, Update and Delete). 
 * 
 * @param <I> Index key
 * @param <T> Type of data
 */
public interface CRUD<I, T> {
    
    /**
     * @param t The object to create
     * @return The created object
     */
    T create(T t);
    
    /**
     * @param elmts 
     */
    void createAll(Collection<T> elmts);
    
    /**
     * @param i The index of the object to read
     * @return Reading result
     */
    T read(I i);
    
    /**
     * 
     * @return 
     */
    Collection<T> readAll();
    
    /**
     * @param t The object to update
     * @return The updated object
     */
    T update(T t);
    
    /**
     * 
     * @param elmts 
     */
    void updateAll(Collection<T> elmts);

    /**
     * @param t The object to delete
     * @return Result of delete (true or false)
     */
    boolean delete(T t);
    
    /**
     * 
     * @param elmts 
     */
    void deleteAll(Collection<T> elmts);
}
