package tandj.trueorfalse;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 04/09/2016.
 */
public class UserFactTools {

    private static final String userFactListFileName = "list_of_facts.txt";

    private File mFile;

    private Context ctx;

    private ArrayList<String> listOfUserFiles = new ArrayList<>();

    public UserFactTools(Context c)
    {
        String userFilePath = c.getFilesDir() + "/" + userFactListFileName;
        mFile = new File(userFilePath);
        checkFileExists();
        ctx = c;
        readFiles();
    }

    private String convertFileName(String fileName)
    {
        String fileNameFormatted = fileName.toLowerCase();
        fileNameFormatted = fileNameFormatted.replace(" ","_");
        return fileNameFormatted;
    }

    private void checkFileExists()
    {
        if (!mFile.exists())
        {
            try
            {
                FileWriter fileWriter = new FileWriter(mFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();

            }
        }
    }

    public void deleteFile(String filename)
    {
        //Delete the file
        String fileToDeletePath = ctx.getFilesDir() + "/" + convertFileName(filename);
        File fileToDelete = new File(fileToDeletePath);
        fileToDelete.delete();

        //And remove from our list
        listOfUserFiles.clear();
        try
        {
            FileReader reader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                if (!line.equals(filename))
                {
                    listOfUserFiles.add(line);
                }
            }
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(mFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii < listOfUserFiles.size(); ii ++)
            {
                bufferedWriter.write(listOfUserFiles.get(ii));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    public void changeFileName(String oldFileName, String newFileName)
    {
        //Rename the file
        String fileToRenamePath = ctx.getFilesDir() + "/" + convertFileName(oldFileName);
        String newFilePath = ctx.getFilesDir() + "/" + convertFileName(newFileName);
        File fileToRename = new File(fileToRenamePath);
        File newFile = new File(newFilePath);
        fileToRename.renameTo(newFile);

        //Rename in our list
        listOfUserFiles.clear();
        try
        {
            FileReader reader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.equals(oldFileName))
                {
                    listOfUserFiles.add(newFileName);
                }
                else
                {
                    listOfUserFiles.add(line);
                }
            }
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(mFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii < listOfUserFiles.size(); ii ++)
            {
                bufferedWriter.write(listOfUserFiles.get(ii));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
    }

    private void readFiles()
    {
        listOfUserFiles.clear();
        try {
            FileReader reader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                listOfUserFiles.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public ArrayList getListOfFiles()
    {
        return listOfUserFiles;
    }

    public void addFile(String newFile)
    {
        listOfUserFiles.clear();
        try
        {
            FileReader reader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                listOfUserFiles.add(line);
            }
            bufferedReader.close();
            listOfUserFiles.add(newFile);
            System.err.println("list of files" + listOfUserFiles);

            FileWriter fileWriter = new FileWriter(mFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii < listOfUserFiles.size(); ii ++)
            {
                bufferedWriter.write(listOfUserFiles.get(ii));
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
