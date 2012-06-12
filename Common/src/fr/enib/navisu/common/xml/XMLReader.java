package fr.enib.navisu.common.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Serge Morvan
 */
public class XMLReader<T> {

    private Class<T> claz;
    private Object object;

    public XMLReader(Class<T> claz) {
        this.claz = claz;
    }

    public Object read(String fileName) throws IOException, JAXBException {
        FileInputStream inputFile;
        JAXBContext jAXBContext;
        Unmarshaller unmarshaller;

        inputFile = new FileInputStream(new File(fileName));

        jAXBContext = JAXBContext.newInstance(claz);
        unmarshaller = jAXBContext.createUnmarshaller();
        object = unmarshaller.unmarshal(inputFile);
        inputFile.close();
        return object;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object read(Class<?> claz, String fileName) throws IOException, JAXBException {
        return new XMLReader(claz).read(fileName);
    }
}