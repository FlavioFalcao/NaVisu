package fr.enib.navisu.common.utils;

import gov.nasa.worldwind.Configuration;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * 
 * @author Jordan Mens & Thibault Pensec
 * @date 28 mars 2012
 */
public final class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
    
    public static final String LINUX_SEPARATOR_PATTERN = "/";
    public static final String WINDOWS_SEPARATOR_PATTERN = "\\";
    
    public static final String DD_MM_YYYY_DATE_PATTERN = "dd/MM/yyyy";
    
    private Utils() {
    }
    
    public static String getDataCacheFolder() {
        
        Path dataFolderPath;
        String commonDataFolder = Configuration.getUserHomeDirectory(), navisuData = ".navisu";
        
        if(commonDataFolder != null) {
            if(Files.notExists(Paths.get(commonDataFolder))) {
                try {
                    Files.createDirectory(Paths.get(commonDataFolder));
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }

        dataFolderPath = Paths.get(commonDataFolder, navisuData);
        if (Files.notExists(dataFolderPath)) {
            try {
                Files.createDirectory(dataFolderPath);
                LOGGER.info("Data folder created...");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        
        return dataFolderPath.toString() + File.separator;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isDouble(String s) {
        try {
            Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isBoolean(String s) {
        try {
            Boolean.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String formatDouble(double value, int fractionDigits) {
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(fractionDigits);
        nf.setMinimumFractionDigits(fractionDigits);
        return nf.format(value);
    }
    
    public static String getFormattedPath(String path) {
        return path
                .replace(WINDOWS_SEPARATOR_PATTERN, File.separator)
                .replace(LINUX_SEPARATOR_PATTERN, File.separator);
    }

    public static Set<String> arrayToSet(String[] objs) {
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(objs));
        return set;
    }

    public static void executeCmd(String cmd) throws IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
    }


    public static boolean exists(String path) {
        return new File(path).exists();
    }

    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    public static boolean deleteDirectory(File path) {
        boolean resultat = true;
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    resultat &= deleteDirectory(f);
                } else {
                    resultat &= f.delete();
                }
            }
        }
        resultat &= path.delete();
        return resultat;
    }
    
    /**
     * 
     * @param root Le dossier racine
     * @param ext L'extension des fichiers attendus (ex: ".jpg")
     * @param rec true si lecture récursive, false sinon
     * @return La liste des fichiers
     * @throws NullPointerException 
     */
    public static List<File> listFiles(File root, final String ext, final boolean rec) throws NullPointerException{
        
        if(root == null || !root.isDirectory()) return null;
        
        List<File> files = new LinkedList<>();
        
        for(File f : root.listFiles()) {
            if(!f.isDirectory() && f.getName().toLowerCase().endsWith(ext.toLowerCase())) {
                files.add(f);
            }
            if(f.isDirectory() && rec) {
                files.addAll(listFiles(f, ext, rec));
            }
        }
        
        return files;
    }

    public static void getThumbnail(String imgPath, String outPath, int height, int width, String type) throws IOException {

        File file = new File(imgPath);
        Image image = ImageIO.read(file);
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();

        g.drawImage(image, 0, 0, width, height, null);

        final int extLength = 4;
        final String fileOut = outPath + File.separator + file.getName().substring(0, file.getName().length() - extLength) + "_thumb." + type;
        ImageIO.write(buf, type, new File(fileOut));
    }
    
    public static boolean containsFile(Path dir, String fileName) {
        for(String file : dir.toFile().list()) 
            if(file.equals(fileName))
                return true;
        
        return false;
    }
    
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);

    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }
}
