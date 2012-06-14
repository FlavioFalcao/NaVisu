package fr.enib.navisu.domain.ships3d.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Category {

    public static final Map<Integer, String> CODE = Collections.unmodifiableMap(new HashMap<Integer, String>() {

        {
            put(0, "default");
            put(20, "wig");
            put(30, "fishing");
            put(31, "towing");
            put(32, "towingWithMoreThan200MeterTow");
            put(33, "underwaterOperation");
            put(34, "divingOperation");
            put(35, "militaryOperation");
            put(36, "sailing");
            put(37, "pleasureCraft");
            put(40, "hsc");
            put(50, "pilotVessel");
            put(51, "searchAndRescueVessel");
            put(52, "tug");
            put(53, "portTender");
            put(54, "antiPollutionVessel");
            put(55, "LawEnforcementVessel");
            put(58, "medicalTransport");
            put(60, "passenger");
            put(70, "cargo");
            put(71, "pollutantCargoCategoryA");
            put(72, "pollutantCargoCategoryB");
            put(73, "pollutantCargoCategoryC");
            put(74, "pollutantCargoCategoryD");
            put(80, "tanker");
            put(81, "pollutantTankerCategoryA");
            put(82, "pollutantTankerCategoryB");
            put(83, "pollutantTankerCategoryC");
            put(84, "pollutantTankerCategoryD");
            put(90, "other");
        }
    });
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Set<Map.Entry<Integer, String>> entries = Category.CODE.entrySet();
        buffer.append("[");
        for (Map.Entry<Integer, String> e : entries) {
            buffer.append("[").append(e.getKey()).append(", ").append(e.getValue()).append("]");
        }
        buffer.append("]");
        return buffer.toString();
    }

    public static void main(String[] args) {
        Category category = new Category();
        System.out.println(category);
    }
}
