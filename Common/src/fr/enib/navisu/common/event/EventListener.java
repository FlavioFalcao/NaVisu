/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.common.event;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 02/04/2012
 */
public interface EventListener<T> extends java.util.EventListener {

    public void notify(T evt);
}
