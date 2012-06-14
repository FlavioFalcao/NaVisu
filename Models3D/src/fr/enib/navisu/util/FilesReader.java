/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Serge
 */
public class FilesReader {

    private String postfix;

    /**
     * Get the value of postfix
     *
     * @return the value of postfix
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * Set the value of postfix
     *
     * @param postfix new value of postfix
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public FilesReader() {
    }

    public FilesReader(String postfix) {
        this.postfix = postfix;
    }

    public static void main(String[] args) {
        //FilesReader filesReader = new FilesReader("csv");
        // filesReader.testCsv("../data/tide");
        List<String> kmzFiles = new FilesReader().search("D:/models3D", "kmz");
        System.out.println(kmzFiles);

    }

    public void read(File path, List<String> allFiles) {
        if (path.isDirectory()) {
            File[] list = path.listFiles();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    read(list[i], allFiles);
                }
            } else {
                System.err.println(path + " : Erreur de lecture.");
            }
        } else {
            String currentFilePath = null;
            try {
                currentFilePath = path.getCanonicalPath();
            } catch (IOException ex) {
                Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            allFiles.add(currentFilePath);
        }
    }

    private void testCsv(String repository) {
        List<String> allFiles = new ArrayList<>();

        read(new File(repository), allFiles);

        CSVReader csvReader;
        List<String[]> list = null;
        for (String s : allFiles) {
            if (s.substring(s.length() - 3).equalsIgnoreCase(postfix)) {
                try {
                    csvReader = new CSVReader(new InputStreamReader(new FileInputStream(s), "ISO-8859-1"), ';', '$', 5);
                    list = csvReader.readAll();
                } catch (IOException ex) {
                    Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (String[] tab : list) {
                    for (int i = 0; i < tab.length; i++) {
                        System.out.print(tab[i] + "  ");
                    }
                    System.out.println("");
                }
            }
        }
    }

    public List<String> search(String repository, String postfix) {
        List<String> allFiles = new ArrayList<>();
        List<String> searchFiles = new ArrayList<>();
        String postFix = "." + postfix;
        read(new File(repository), allFiles);

        for (String s : allFiles) {
            if (s.substring(s.length() - 4).equalsIgnoreCase(postFix)) {
                searchFiles.add(s);
            }
        }
        return searchFiles;
    }
}
