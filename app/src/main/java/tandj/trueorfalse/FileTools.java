package tandj.trueorfalse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 23/08/2016.
 */
public class FileTools
{
    /**
     * Name of file where the scores are stored
     */
    public static final String scoreDataFileName = "score_data.txt";

    /**
     * File where the scores are stored
     */
    public static File scoreFile;

    /**
     * List of all levels for which to store score for
     */
    private static String[] mListOfAllFactFileNames = FactFileNames.fileNames;

    private static int[] mListOfScores;

    public static void setScoreFile(File f)
    {
        scoreFile = f;
    }

    public static void init()
    {
        mListOfScores = new int[mListOfAllFactFileNames.length];
        for (int ii = 0; ii < mListOfScores.length; ii ++)
        {
            mListOfScores[ii] = 0;
        }
        checkForUpdates();
    }

    private static void checkForUpdates()
    {
        List<String> newFiles = new LinkedList<>(Arrays.asList(mListOfAllFactFileNames));
        try
        {
            FileReader fileInput = new FileReader(scoreFile);
            BufferedReader bufferedInput = new BufferedReader(fileInput);
            String line = null;
            while ((line = bufferedInput.readLine()) != null)
            {
                String[] splitLine = line.split("#");
                for (int jj = 0; jj < mListOfAllFactFileNames.length; jj ++)
                {
                    if (mListOfAllFactFileNames[jj].equals(splitLine[0]))
                    {
                        mListOfScores[jj] = Integer.parseInt(splitLine[1]);
                    }
                }
                System.out.println(splitLine[0]);
                System.out.println(newFiles);
                newFiles.remove(splitLine[0]);
            }
            bufferedInput.close();
            if (!newFiles.isEmpty())
            {
                // New files have been added since last time
                FileWriter fileWriter = new FileWriter(scoreFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int jj = 0; jj < mListOfAllFactFileNames.length; jj++)
                {
                    if (!newFiles.contains(mListOfAllFactFileNames[jj]))
                    {
                        // The file already contained this information - recreating the file, so rewrite
                        bufferedWriter.write(mListOfAllFactFileNames[jj] + "#" + mListOfScores[jj]);
                        bufferedWriter.newLine();
                    }
                }
                for (int ii = 0; ii < newFiles.size(); ii++)
                {
                    // Write any new themes that have been added since last time.
                    bufferedWriter.write(newFiles.get(ii) + "#0");
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            }

        }
        catch (IOException e)
        {            e.printStackTrace();

        }
    }

    public static void writeData(String factFileName, int score)
    {
        init();
        boolean error = true;
        for (int ii = 0; ii < mListOfAllFactFileNames.length; ii++)
        {
            if (mListOfAllFactFileNames[ii].equals(factFileName))
            {
                error = false;
                mListOfScores[ii] = score;
            }
        }

        if (error)
        {
            System.out.println("ERROR: Incorrect file name");
        }

        try
        {
            FileWriter fileWriter = new FileWriter(scoreFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii < mListOfAllFactFileNames.length; ii++)
            {
                bufferedWriter.write(mListOfAllFactFileNames[ii] + "#" + mListOfScores[ii]);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    public static int getScore(String factFileName)
    {
        init();
        for (int ii = 0; ii < mListOfScores.length; ii++)
        {
            if (mListOfAllFactFileNames[ii].equals(factFileName))
            {
                return mListOfScores[ii];
            }
        }
        return 0;
    }



}
