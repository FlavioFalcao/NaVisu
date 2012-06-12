/*
 * XMLDateAdapter.java
 *
 * Created on 10 avril 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.enib.navisu.common.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Serge Morvan
 */
public class XMLDateAdapter extends XmlAdapter<String, Date> {

    /**
     * Creates a new instance of XMLDateAdapter
     */
    public XMLDateAdapter() {
    }

    @Override
    public Date unmarshal(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    @Override
    public String marshal(Date date)  {
        return dateFormat.format(date);
    }
    
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
}
