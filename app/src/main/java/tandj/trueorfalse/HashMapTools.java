package tandj.trueorfalse;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas on 16/08/2016.
 * Class containing tools for getting information from a hash map
 */
public class HashMapTools {

    private HashMap<String, Boolean> mHashMap = new HashMap<String,Boolean>();
    private ArrayList<String> mListOfValues;
    private int mNumberOfValues;
    private String mItem = null;
    private Context mContext;
    private List mUsedNumbers = new ArrayList();

    public HashMapTools(String fileName, Context context)
    {
        mContext = context;
        getHashMap(fileName);
        mListOfValues = new ArrayList(mHashMap.keySet());
        mNumberOfValues = mHashMap.size();
    }

    /**
     * Method which reads a file and creates a hashmap from it
     * @param fileName Name of the file containing the facts
     */
    private void getHashMap(String fileName)
    {
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

    /**
     * Method which returns a random fact from a hash map
     * @return A random fact
     */
    public String getRandomItem()
    {
        boolean repeat = true;
        int randomIndex = -1;
        while (repeat) {
            repeat=false;
            randomIndex = (int) Math.floor(Math.random() * mNumberOfValues);
            for (int i=0; i<mUsedNumbers.size(); i++)
            {
                if ((int) mUsedNumbers.get(i) == randomIndex) {repeat = true;}
            }
        }
        mItem = mListOfValues.get(randomIndex);
        mUsedNumbers.add(randomIndex);
        return mItem;
    }

    /**
     * Method which returns True or False
     * @return True or False dependent upon the fact, or null if there is an error
     */
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

    public String recordFact(Boolean correct)
    {
        String fact = mItem;
        String entry = "recordFact error";
        if (correct) {
            entry = fact + "#correct";
        }
        else {
            entry = fact + "#incorrect";
        }
        return entry;
    }

    public String getSpecificItem(int itemNumber)
    {
        mItem = mListOfValues.get(itemNumber);
        return mItem;
    }
}
