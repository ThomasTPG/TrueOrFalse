package tandj.trueorfalse;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thomas on 28/08/2016.
 */
public class HashmapCombined {

    Context mContext;
    HashMap<String, Boolean> mHashMap;
    private HashMap<String, Boolean> mAskedQuestions = new HashMap<>();
    String mItem;
    private ArrayList<String> mListOfValues;


    public HashmapCombined(String[] arrayOfFactFileNames, Context c)
    {
        mContext = c;
        mHashMap = new HashMap<>();
        getHashMap(arrayOfFactFileNames);

    }

    private void getHashMap(String[] arrayOfFacts)
    {
        for (int ii = 0; ii < arrayOfFacts.length; ii++)
        {
            String fileName = arrayOfFacts[ii];
            AssetManager assets = mContext.getAssets();
            try
            {
                InputStream fileInput = assets.open(fileName);
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileInput));
                String line = "";
                while ((line = fileReader.readLine()) != null)
                {
                    String[] splitLine = line.split("#");
                    mHashMap.put(splitLine[0], Boolean.valueOf(splitLine[1].toLowerCase()));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        mListOfValues = new ArrayList(mHashMap.keySet());

    }

    public String getRandomItem()
    {
        ArrayList<String> listOfAskedQuestion = new ArrayList(mAskedQuestions.keySet());
        ArrayList<String> questionsLeft = new ArrayList<>();
        for (String i : mListOfValues)
        {
            if (!(listOfAskedQuestion.contains(i)))
            {
                questionsLeft.add(i);
            }
        }
        if (questionsLeft.isEmpty())
        {
            questionsLeft = mListOfValues;
            mAskedQuestions = new HashMap<>();
        }
        int randomItemIndex = (int) Math.floor(Math.random() * questionsLeft.size());
        mItem = questionsLeft.get(randomItemIndex);
        mAskedQuestions.put(mItem, getTrueOrFalse());
        return mItem;
    }

    public Boolean getTrueOrFalse()
    {
        if (mItem != null)
        {
            return mHashMap.get(mItem);
        }
        else
        {
            return null;
        }
    }





}
