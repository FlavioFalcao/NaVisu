package fr.enib.navisu.common;

import fr.enib.navisu.common.utils.Utils;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author Morvan
 */
public final class EntityManagerProxy {

    private static final Logger LOGGER = Logger.getLogger(EntityManagerProxy.class.getName());
    
    public static final String PERSISTENCE_UNIT_NAME = "NaVisuDB_PU";
    static {
        System.getProperties().setProperty(
                "derby.system.home", 
                Utils.getDataCacheFolder()
                + ".db");
    }
    
    private static EntityManagerFactory entityManagerFactory = null;
    private static EntityManager entityManager = null;
    private static EntityManagerProxy instance = null;

    private EntityManagerProxy() {}

    /**
     *
     * @return
     */
    public static EntityManagerProxy getInstance() {
        if (instance == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            entityManager = entityManagerFactory.createEntityManager();
            instance = new EntityManagerProxy();
        }
        return instance;
    }

    /**
     *
     * @param object
     */
    public void persist(Object object) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            if(!tx.isActive()) 
                tx.begin();
            
            entityManager.persist(object);
            tx.commit();
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            tx.rollback();
        }
    }
    
    public void persist(List<? extends Serializable> list) {
        for(Object object : list) {
            persist(object);
        }
    }

    /**
     *
     * @param <T>
     * @param entity
     * @return
     */
    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

    /**
     *
     * @param <T>
     * @param object
     * @return
     */
    public <T> T update(T object) {
        T res = null;
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            res = entityManager.merge(object);
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while updating object ", ex);
            tx.rollback();
        }
        return res;
    }

    /**
     *
     * @param object
     */
    public void remove(Object object) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.remove(entityManager.merge(object));
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while deleting object ", ex);
            tx.rollback();
        }
    }

    /**
     *
     * @param object
     */
    public void refresh(Object object) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.refresh(object);
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while refreshing object ", ex);
            tx.rollback();
        }
    }

    /**
     *
     * @param <T>
     * @param c
     * @param id
     * @return
     */
    public <T> T find(Class<T> c, Object id) {
        T res = null;
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            res = entityManager.find(c, id);
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while searching object - class : " + c.getName() + " id : " + id + " - " + ex, ex);
            tx.rollback();
        }
        return res;
    }

    /**
     *
     * @param <T>
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> c) {
        List<T> res;
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            res = entityManager.createQuery("SELECT t FROM " + c.getSimpleName() + " t").getResultList();
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while searching all objects - class : " + c.getName() + " - " + ex, ex);
            tx.rollback();
            return null;
        }

        return res;
    }

    /**
     *
     * @param c
     * @return
     */
    public <T> int clearAll(Class<T> c) {
        int nbRes = 0;
        EntityTransaction tx = entityManager.getTransaction();
        if(!tx.isActive()) 
            tx.begin();
        try {
            nbRes = entityManager.createQuery("DELETE FROM " + c.getSimpleName()).executeUpdate();
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while clearing table " + c.getName() + " - " + ex, ex);
            tx.rollback();
        }
        return nbRes;

    }

    /**
     *
     * @return
     */
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    /**
     *
     * @param persistenceUnitName
     */
    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    /**
     *
     * @return
     */
    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    /**
     *
     * @return
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     *
     */
    public void close() {
        entityManager.close();
    }

    /**
     *
     * @return
     */
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    /**
     *
     */
    public void flush() {
        entityManager.flush();
    }

    /**
     *
     * @param flushMode
     */
    public void setFlushMode(FlushModeType flushMode) {
        entityManager.setFlushMode(flushMode);
    }

    /**
     *
     * @return
     */
    public FlushModeType getFlushMode() {
        return entityManager.getFlushMode();
    }

    /**
     *
     * @param <T>
     * @param entityClass
     * @param primaryKey
     * @return
     */
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return entityManager.getReference(entityClass, primaryKey);
    }

    /**
     *
     */
    public void clear() {
        entityManager.clear();
    }

    /**
     *
     * @param entity
     * @return
     */
    public boolean contains(Object entity) {
        return entityManager.contains(entity);
    }

    /**
     *
     * @param query
     * @return
     */
    public Query createQuery(String query) {
        return entityManager.createQuery(query);
    }

    /**
     *
     * @param namedQuery
     * @return
     */
    public Query createNamedQuery(String namedQuery) {
        return entityManager.createNamedQuery(namedQuery);
    }

    /**
     *
     * @param sqlString
     * @return
     */
    public Query createNativeQuery(String sqlString) {
        return entityManager.createNativeQuery(sqlString);
    }

    /**
     *
     * @param sqlString
     * @param resultClass
     * @return
     */
    public <T> Query createNativeQuery(String sqlString, Class<T> resultClass) {
        return entityManager.createNativeQuery(sqlString, resultClass);
    }
    private String persistenceUnitName;
}
