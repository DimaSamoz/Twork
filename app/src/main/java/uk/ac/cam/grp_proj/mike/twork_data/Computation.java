package uk.ac.cam.grp_proj.mike.twork_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dima on 06/02/16.
 */
public class Computation {
    private int id;
    private String name;
    private String description;
    private String areas;
    private boolean selected = false;
    private static List<Computation> allComps;

    public Computation(String name, String description, String area) {
        this.name = name;
        this.description = description;
        this.areas = area;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    // TODO database integration
    public static List<Computation> getComputations() {
        if (allComps == null) {
            allComps = new ArrayList<>();

            String[] compNames = {"DreamLab", "SETI@home", "Galaxy Zoo", "RNA World", "Malaria Control", "Leiden Classical", "GIMPS", "Electric Sheep", "DistributedDataMining", "Compute For Humanity"};

            String[] compDescs = {
                    "Breast, ovarian, prostate and pancreatic cancer",
                    "Search for extraterrestrial life by analyzing specific radio frequencies emanating from space",
                    "Classifies galaxy types from the Sloan Digital Sky Survey",
                    "Uses bioinformatics software to study RNA structure",
                    "Simulate the transmission dynamics and health effects of malaria",
                    "General classical mechanics for students or scientists",
                    "Searches for Mersenne primes of world record size",
                    "Fractal flame generation",
                    "Research in the various fields of data analysis and machine learning, such as stock market prediction and analysis of medical data",
                    "Generating cryptocurrencies to sell for money to be donated to charities"
            };

            String[] compAreas = {
                    "Cancer research",
                    "Astrobiology",
                    "Astronomy, Cosmology",
                    "Molecular biology",
                    "Epidemiology",
                    "Chemistry",
                    "Mathematics",
                    "Computational art",
                    "Data analysis, Machine learning",
                    "Criptocurrencies, Charitable organisations"

            };
            for (int i = 0; i < compNames.length; i++) {
                allComps.add(new Computation(compNames[i], compDescs[i], compAreas[i]));
            }
        }

        return allComps;
    }

    public int getId() {
        return new Random().nextInt();
//        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAreas() {
        return areas;
    }
}
