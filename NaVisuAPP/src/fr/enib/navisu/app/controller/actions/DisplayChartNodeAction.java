package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.nodes.ChartChildFactory.ChartNode;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.view.WWChart;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 15/05/2012
 */
public class DisplayChartNodeAction extends AbstractAction {

    private final Chart chart;
    private WWChart wwChart;
    private final ChartNode node;
    private final ChartsController appController = AppTopComponent.controller();

    public DisplayChartNodeAction(final ChartNode node, final Chart chart) {
        this.chart = chart;
        this.node = node;
        initialize();
    }

    private void initialize() {
        
        wwChart = appController.findWWChart(chart);
        
        if(wwChart.isVisible()) {
            putValue(NAME, "Hide");
        } else {
            putValue(NAME, "Display");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (!wwChart.isVisible()) {
            wwChart.setVisible(true);
        } else {
            wwChart.setVisible(false);
        }
        
        node.refresh();
    }
}
