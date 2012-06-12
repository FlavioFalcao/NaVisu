
package fr.enib.navisu.charts.controller.xml.adapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 04/04/2012
 */
public class RaAdapter extends XmlAdapter<RaAdapter.RAType, Integer[]> {

    @Override
    public RAType marshal(Integer[] v) throws Exception {
        if(v.length != 2)
            throw new ArrayIndexOutOfBoundsException("Array size must equals 2");
        return new RAType(v[0], v[1]);
    }
    
    @Override
    public Integer[] unmarshal(RAType v) throws Exception {
        return new Integer[]{v.width, v.height};
    }
    
    public static class RAType {
        
        @XmlAttribute public int width;
        @XmlAttribute public int height;

        public RAType() {}
        
        public RAType(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
