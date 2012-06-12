package fr.enib.navisu.common.catalog;

import fr.enib.navisu.common.dao.CRUD;
import java.util.List;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * 
 * @author Thibault Pensec and Jordan Mens
 * @date 12 mars 2012
 */

public abstract class CatalogDAO<I, T> extends Catalog<T> implements CRUD<I, T> {
    
    public CatalogDAO() {
        super();
    }
    
    /**
     * @param elements 
     */
    public CatalogDAO(List<T> elements) {
        super(elements);
    }
}
