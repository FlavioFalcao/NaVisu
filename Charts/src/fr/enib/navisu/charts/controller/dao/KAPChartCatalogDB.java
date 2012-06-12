package fr.enib.navisu.charts.controller.dao;

import fr.enib.navisu.charts.model.kap.KAPChart;
import fr.enib.navisu.common.EntityManagerProxy;
import fr.enib.navisu.common.catalog.Catalog;
import fr.enib.navisu.common.catalog.CatalogDAO;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec & Jordan Mens
 * @date 29 mars 2012
 */
public final class KAPChartCatalogDB extends CatalogDAO<String, KAPChart> {
    
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(KAPChartCatalogDB.class.getName());
    /** The entity proxy manager util class */
    private static final EntityManagerProxy EMP = EntityManagerProxy.getInstance();
    
    /**
     * Default constructor
     */
    public KAPChartCatalogDB() {
        super();
    }
    
    /**
     * Initialize database
     * 
     * @param chartsPath 
     */
    public KAPChartCatalogDB(String chartsPath) {
        
        Catalog<KAPChart> charts = new KAPChartCatalog(chartsPath);
        
        for(KAPChart c : charts.list()) {
            create(c);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="KAPChartCatalogDAO">
    @Override
    public KAPChart create(KAPChart t) {
        assert t != null;
        add(t);
        EMP.persist(t);
        return t;
    }
    
    @Override
    public void createAll(Collection<KAPChart> elmts) {
        for(KAPChart c : elmts) {
            create(c);
        }
    }
    
    @Override
    public KAPChart read(String i) {
        assert i != null;
        KAPChart elmt = EMP.find(KAPChart.class, i);
        add(elmt);
        return elmt;
    }
    
    @Override
    public Collection<KAPChart> readAll() {
        List<KAPChart> all = EMP.findAll(KAPChart.class);
        addAll(all);
        return all;
    }
    
    @Override
    public KAPChart update(KAPChart t) {
        assert t != null;
        //set(indexOf(t), t);
        return EMP.update(t);
    }
    
    @Override
    public void updateAll(Collection<KAPChart> elmts) {
        for(KAPChart c : elmts) {
            update(c);
        }
    }
    
    @Override
    public boolean delete(KAPChart t) {
        assert t!= null;
        remove(t);
        EMP.remove(t);
        return !EMP.contains(t);
    }
    
    @Override
    public void deleteAll(Collection<KAPChart> elmts) {
        for(KAPChart c : elmts) {
            delete(c);
        }
    }
    //</editor-fold>
    public void clearAll() {
        EMP.clearAll(KAPChart.class);
        clear();
    }
    
    public boolean contains(KAPChart elmt) {
        for(KAPChart c : list()) {
            if(c.getId().equals(elmt.getId()))
                return true;
        }
        return false;
    }
    
    @Override
    public void display() {
        for(KAPChart c : list()) {
            LOGGER.log(Level.INFO, c.toString());
        }
    }
}
