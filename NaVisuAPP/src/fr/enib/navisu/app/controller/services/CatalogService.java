/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.controller.services;

import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.common.catalog.Catalog;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 10/05/2012
 */
public interface CatalogService {

    void updateCatalog(Catalog<? extends Chart> catalog);
}
