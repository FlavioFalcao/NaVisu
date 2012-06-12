/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.common.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Serge Morvan
 */
public class XMLWriter<T> {

    FileOutputStream outputFile = null;
    T t = null;

    public XMLWriter(T t) {
        this.t = t;
    }

    public void write(String fileName) throws FileNotFoundException, JAXBException, IOException {
        assert fileName != null;
        
        JAXBContext jAXBContext;
        Marshaller marshaller;

        outputFile = new FileOutputStream(new File(fileName));

        jAXBContext = JAXBContext.newInstance(t.getClass()); assert jAXBContext != null;
        marshaller = jAXBContext.createMarshaller(); assert marshaller != null;
        marshaller.marshal(t, outputFile);
        outputFile.close();
    }
    
    public static <T> void write(T t, String fileName) throws FileNotFoundException, JAXBException, IOException {
        new XMLWriter<>(t).write(fileName);
    }
}
