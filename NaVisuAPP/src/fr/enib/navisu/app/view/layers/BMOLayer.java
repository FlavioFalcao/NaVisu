 /* 
  * To change this template, choose Tools | Templates 
  * and open the template in the editor. 
  */ 
 package fr.enib.navisu.app.view.layers;
  
 import gov.nasa.worldwind.avlist.*; 
 import gov.nasa.worldwind.geom.*; 
 import gov.nasa.worldwind.layers.BasicTiledImageLayer; 
 import gov.nasa.worldwind.util.*; 
  
  
 /** 
  * 
  * @author Jboll 
  */ 
 public class BMOLayer extends BasicTiledImageLayer { 
  
     /**
      *
      */
     public BMOLayer() {
         super(makeLevels()); 
         this.setUseTransparentTextures(true); 
     } 
  
     private static LevelSet makeLevels() { 
      
         AVList params = new AVListImpl();
         params.setValue(AVKey.TILE_WIDTH, 512); 
         params.setValue(AVKey.TILE_HEIGHT, 512); 
         params.setValue(AVKey.DATA_CACHE_NAME, "Earth/BMO Layer");      
      //   params.setValue(AVKey.SERVICE, "http://127.0.0.1/index.php"); 
         params.setValue(AVKey.DATASET_NAME, "BMOLayer"); 
         params.setValue(AVKey.FORMAT_SUFFIX, ".dds");
         params.setValue(AVKey.NUM_LEVELS, 16);       
         params.setValue(AVKey.NUM_EMPTY_LEVELS, 0);     
         params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(Angle.fromDegrees(36d), Angle.fromDegrees(36d))); 
         params.setValue(AVKey.SECTOR, Sector.FULL_SPHERE); 
         return new LevelSet(params);

     } 
  
     @Override 
     public String toString() { 
         return "Brest Metropole Oceane"; 
     } 
 } 

