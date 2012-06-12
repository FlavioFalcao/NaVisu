package fr.enib.navisu.app.controller.nodes;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.actions.GoToChartNodeAction;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.model.kap.KAPChart;
import fr.enib.navisu.charts.view.WWChart;
import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.*;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;
import org.openide.windows.InputOutput;

/**
 * @author Jordan MENS & Thibault PENSEC @date 10/05/2012
 */
public class ChartChildFactory extends ChildFactory<Chart> {

    private List<Chart> kapCharts;
    private Method[] methods;
    public static final InputOutput IO = AppTopComponent.io();

    @SuppressWarnings("cast")
    public ChartChildFactory(List<Chart> kapCharts) {
        this.kapCharts = kapCharts;

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
        //methods = (Method[]) Arrays.copyOf(l.toArray(), l.size());
    }

    @Override
    protected boolean createKeys(List<Chart> list) {
        for (Chart k : kapCharts) {
            list.add(k);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(final Chart kapChart) {
        
        Node node = new AbstractNode(Children.LEAF, Lookups.singleton(kapChart)) {
            
            @Override
            public boolean canDestroy() {
                KAPChart kapChart = this.getLookup().lookup(KAPChart.class);
                return kapChart != null;
            }

            @Override
            public String getHtmlDisplayName() {
                return kapChart.getId() + "<font color='#aaaaaa'><i> (" + kapChart.getName() + ")</i></font>";
            }
            
            @Override
            public Image getIcon(int type) {
                
                WWChart wwKAPChart = AppTopComponent.controller().findWWChart(kapChart);
                String imageURL;
                
                if(wwKAPChart.isTiled()) {
                    imageURL = "fr/enib/navisu/app/view/explorer/valid.gif";
                } else {
                    imageURL = "fr/enib/navisu/app/view/explorer/invalid.gif";
                }
                return ImageUtilities.loadImage(imageURL);
            }

            @Override
            public Action[] getActions(boolean context) {

                Action[] actions = super.getActions(context);
                Action[] myAction = new Action[]{
                    new GoToChartNodeAction(kapChart)
                };

                Action[] finalActions = new Action[actions.length + myAction.length];
                System.arraycopy(myAction, 0, finalActions, 0, myAction.length);
                System.arraycopy(actions, 0, finalActions, myAction.length, actions.length);
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
        };
        
        return node;
    }
}
