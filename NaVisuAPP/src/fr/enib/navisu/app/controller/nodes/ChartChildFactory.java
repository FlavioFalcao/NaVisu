package fr.enib.navisu.app.controller.nodes;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.actions.DisplayAndGotoChartNodeAction;
import fr.enib.navisu.app.controller.actions.DisplayChartNodeAction;
import fr.enib.navisu.app.controller.actions.GotoChartNodeAction;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.controller.event.kapcontroller.ChartsControllerEventListener;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.model.kap.KAPChart;
import fr.enib.navisu.charts.view.WWChart;
import gov.nasa.worldwind.event.SelectEvent;
import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.*;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.InputOutput;

/**
 * @author Jordan MENS & Thibault PENSEC @date 10/05/2012
 */
public class ChartChildFactory extends ChildFactory<Chart> {

    private List<Chart> charts;
    private Method[] methods;
    public static final InputOutput IO = AppTopComponent.io();
    private final ChartsController controller = AppTopComponent.controller();
    
    @SuppressWarnings("cast")
    public ChartChildFactory(List<Chart> charts) {
        this.charts = charts;

        Class<?> c = KAPChart.class;
        methods = c.getMethods();
        List<Method> l = new ArrayList<>();
        List<String> disablePropertiesMethod = new ArrayList<>();
        disablePropertiesMethod.add("getClass");
        disablePropertiesMethod.add("getNtmDate");
        disablePropertiesMethod.add("getPolygon");
        disablePropertiesMethod.add("getImageWidthHeight");
        disablePropertiesMethod.add("getImageHeight");
        disablePropertiesMethod.add("getImageWidth");
        disablePropertiesMethod.add("getReferencePoints");
        
        for (Method m : methods) {
            if (m.getName().startsWith("get") && !disablePropertiesMethod.contains(m.getName())) {
                l.add(m);
            }
        }

        methods = (Method[]) l.toArray(new Method[l.size()]);
    }

    @Override
    protected boolean createKeys(List<Chart> list) {
        for (Chart chart : charts) {    
            list.add(chart);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(final Chart chart) {
        ChartNode node = new ChartNode(chart, this, Children.LEAF, Lookups.singleton(chart));
        controller.addEventListener(node);
        return node;
    }

    public class ChartNode extends AbstractNode implements ChartsControllerEventListener {

        private final WWChart wwChart;
        private final Chart chart;
        private final ChartChildFactory parent;

        public ChartNode(final Chart chart, final ChartChildFactory parent, Children children, Lookup lookup) {
            super(children, lookup);
            this.chart = chart;
            this.wwChart = AppTopComponent.controller().findWWChart(chart);
            this.parent = parent;
        }

        @Override
        public boolean canDestroy() {
            KAPChart kapChart = this.getLookup().lookup(KAPChart.class);
            return kapChart != null;
        }

        @Override
        public String getHtmlDisplayName() {
            return chart.getId() + "<font color='#aaaaaa'><i> (" + chart.getName() + ")</i></font>";
        }

        @Override
        public Image getIcon(int type) {

            String imageURL = "fr/enib/navisu/app/view/images/emblem-important-4.png";

            if (wwChart.isTiled()) {

                if (wwChart.isVisible()) {
                    imageURL = "fr/enib/navisu/app/view/images/emblem-default.png";
                } else {
                    imageURL = "fr/enib/navisu/app/view/images/emblem-generic.png";
                }
            }
            return ImageUtilities.loadImage(imageURL);
        }
        
        @Override
        public Action[] getActions(boolean context) {

            Action[] actions = super.getActions(context);

            List<Action> newActionsList = new ArrayList<>(3);
            newActionsList.add(new DisplayChartNodeAction(this, chart));
            newActionsList.add(new GotoChartNodeAction(chart));
            newActionsList.add(new DisplayAndGotoChartNodeAction(this, chart));

            Action[] newActions = new Action[newActionsList.size()];
            for (int i = 0; i < (wwChart.isVisible() ? 2 : 3); i++) {
                newActions[i] = newActionsList.get(i);
            }

            Action[] finalActions = new Action[actions.length + newActions.length];
            System.arraycopy(newActions, 0, finalActions, 0, newActions.length);
            System.arraycopy(actions, 0, finalActions, newActions.length, actions.length);
            return finalActions;
        }

        @Override
        protected Sheet createSheet() {

            Sheet sheet = Sheet.createDefault();
            final KAPChart kapChart = getLookup().lookup(KAPChart.class);
            Sheet.Set set = Sheet.createPropertiesSet();
            set.setDisplayName("Information");
            sheet.put(set);

            Property<String> prop;

            for (final Method m : methods) {

                String propertyName = m.getName().substring(3, m.getName().length());
                prop = new PropertySupport.ReadOnly<String>(m.getName(), String.class, propertyName, "") {

                    @Override
                    public String getValue() {
                        try {
                            return (null != kapChart) ? m.invoke(kapChart, (Object[]) null).toString() : "*********";
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                        return "fail";
                    }
                };
                set.put(prop);
            }

            return sheet;
        }
        
        public void refresh() {
            fireIconChange();
        }

        @Override
        public void selected(SelectEvent se, WWChart wwChart) {
            if(wwChart.getModel().getId().equals(chart.getId())) {
                refresh();
            }
        }
    }
    
    private void print(Object str) {
        IO.getOut().println("[ChartChildFactory] " + str);
    }
}
