package tandj.trueorfalse;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Thomas on 25/08/2016.
 */
public class statisticsUpdated {

    /**
     * Name of file where the stats are stored
     */
    private static final String statFileName = "stats.txt";

    /**
     * File where the scores are stored
     */
    private static File statFile;

    private File mFile;

    private int[] statValueArray;

    public static final int NUMBER_OF_QUESTIONS_ANSWERED = 0;

    public static final int NUMBER_OF_CORRECT_ANSWERS = 1;

    public static final int NUMBER_OF_ROUNDS_COMPLETE = 2;

    public static final int TOTAL_SCORE = 3;

    public static final int NUMBER_OF_INCORRECT_ANSWERS = 4;

    public static final int NUMBER_OF_SPLIT_SCREEN_GAMES = 5;

    public static final int NUMBER_OF_TIME_TRIAL_GAMES = 6;

    public static final int NUMBER_OF_TRUE_ANSWERS = 7;

    public static final int NUMBER_OF_FALSE_ANSWERS = 8;

//Should include scores for each difficulty
    public static final String[] statisticArray = {"Number of questions answered",
                                                   "Number of correct answers",
                                                   "Number of rounds complete",
                                                   "Total score",
                                                   "Number of incorrect answers",
                                                   "Number of Split Screen games",
                                                   "Number of Time Trial Games",
                                                   "Number of True answers",
                                                   "Number of False answers" };

    public statisticsUpdated(Context c)
    {
        String scoreDataFilePath = c.getFilesDir() + "/" + statFileName;
        mFile = new File(scoreDataFilePath);
        statValueArray = new int[statisticArray.length];
        for (int ii = 0; ii < statValueArray.length; ii++)
        {
            statValueArray[ii] = 0;
        }
        checkFileExists();
    }

    private void checkFileExists()
    {
        if (!mFile.exists())
        {
            try
            {
                FileWriter fileWriter = new FileWriter(mFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int ii = 0; ii < statisticArray.length; ii++)
                {
                    bufferedWriter.write(statisticArray[ii] + "#" + statValueArray[ii]);
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();

            }
        }
    }


    public void updateStat(int stat, int increment)
    {

        try
        {
            FileReader fileReader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (int ii = 0; ii < statisticArray.length; ii ++)
            {
                String line = bufferedReader.readLine();
                if (!(line == null))
                {
                    String[] split = line.split("#");
                    statValueArray[ii] = Integer.parseInt(split[1]);
                }
            }
            bufferedReader.close();

            //Update stat
            statValueArray[stat] = statValueArray[stat] + increment;

            FileWriter fileWriter = new FileWriter(mFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii< statisticArray.length; ii++)
            {
                bufferedWriter.write(statisticArray[ii] + "#" + statValueArray[ii]);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    public int getStat(int stat)
    {
        try {
            FileReader fileReader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (int ii = 0; ii < statisticArray.length; ii++) {
                String line = bufferedReader.readLine();
                if (!(line == null)) {
                    String[] split = line.split("#");
                    System.out.println(line);
                    statValueArray[ii] = Integer.parseInt(split[1]);
                }
            }
            bufferedReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        return statValueArray[stat];
    }

}
