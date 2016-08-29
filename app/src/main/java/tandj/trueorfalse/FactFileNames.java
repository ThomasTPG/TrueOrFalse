package tandj.trueorfalse;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas on 23/08/2016.
 */
public class FactFileNames
{
    // Add any new file to the following:
    // 1) Add it to the correct difficulty array
    // 2) Add it to the allFiles array and the fileNames array, making sure that they are at the same index in both

    static final String[] easyFiles = {"Cute animal facts", "Number facts", "Cambridge", "Solar System - Easy"};

    static final String[] mediumFiles = {"Tasty Foods", "NBA", "London", "Solar System - Medium", "Wimbledon"};

    static final String[] hardFiles = {"Solar System - Hard", "Africa"};

    static final String[] allFiles = {
            "Cute animal facts",
            "Number facts",
            "Tasty Foods",
            "Cambridge",
            "NBA",
            "London",
            "Solar System - Easy",
            "Solar System - Medium",
            "Solar System - Hard",
            "Wimbledon",
            "Africa"};

    static final String[] fileNames = {
            "cute_animal_facts.txt",
            "maths_facts.txt",
            "tasty_foods.txt",
            "cambridge_facts.txt",
            "nba_facts.txt",
            "london_facts.txt",
            "solar_system_facts_easy.txt",
            "solar_system_facts_medium.txt",
            "solar_system_facts_hard.txt",
            "wimbledon_medium.txt",
            "africa.txt"};

    static final String[] difficulties = {"Easy", "Medium", "Hard"};

    static final String[][] difficultyArrays = {easyFiles, mediumFiles, hardFiles};


}
