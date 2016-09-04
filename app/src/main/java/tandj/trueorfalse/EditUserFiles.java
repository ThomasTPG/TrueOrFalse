package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Thomas on 04/09/2016.
 */
public class EditUserFiles extends Activity {

    private String mFileName;

    private String mFileNameConverted;

    private String mTempFileName = "temptemptemp_temp_aishlfiasuaf";

    private File mFile;

    private EditText mEditFileName;

    private Button mSaveButton;

    private Button mBackButton;

    private LinearLayout mLinearLaytoutEditFacts;

    private ScrollView mScrollViewFacts;

    private LinkedHashMap<String, Boolean> mTheFacts = new LinkedHashMap<>();

    private LinearLayout mLinearLayoutContainingFacts;

    private EditText mFactEditor;

    private Button mSetTrue;

    private Button mSetFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_facts);

        mFileName = getIntent().getStringExtra("file name");
        System.out.println("File name " + mFileName);

        setupDisplays();

        if (!mFileName.equals("new"))
        {
            mFileNameConverted = mFileName.replace(" ", "_");
            mFileNameConverted = mFileNameConverted.toLowerCase();
            String filePath = getFilesDir() + "/" + mFileNameConverted;
            mFile = new File(filePath);
            setUpExistingFile();
        }
        else
        {
            String filePath = getFilesDir() + "/" + mTempFileName;
            mFile = new File(filePath);
            setUpNewFile();
        }

    }

    private void setupDisplays()
    {
        mEditFileName = (EditText) findViewById(R.id.edit_file_name);
        mSaveButton = (Button) findViewById(R.id.save_button_edit_facts);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePressed();
            }
        });
        mBackButton = (Button) findViewById(R.id.back_button_edit_facts);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });
        mLinearLaytoutEditFacts = (LinearLayout) findViewById(R.id.linear_layout_fact_edit_screen);
        mLinearLaytoutEditFacts.setVisibility(View.GONE);
        mScrollViewFacts = (ScrollView) findViewById(R.id.scroll_view_editer);
        mScrollViewFacts.setVisibility(View.VISIBLE);
        mLinearLayoutContainingFacts = (LinearLayout) findViewById(R.id.fact_displayer_editer);
        mFactEditor = (EditText) findViewById(R.id.user_edit_fact);
        mSetTrue = (Button) findViewById(R.id.true_button_editer);
        mSetTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(true);
            }
        });
        mSetFalse = (Button) findViewById(R.id.false_button_editer);
        mSetFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(false);
            }
        });
    }

    private void setUpNewFile()
    {
        mEditFileName.setText("");
        fillLinearLayout();
    }

    private void setUpExistingFile()
    {
        mEditFileName.setText(getIntent().getStringExtra("file name"));
        fillLinearLayout();

    }

    private void fillLinearLayout()
    {
        readFacts();
        ArrayList<String> facts = new ArrayList<>(mTheFacts.keySet());
        for (int ii = 0; ii < mTheFacts.size(); ii++)
        {
            final String currentFact = facts.get(ii);
            final LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.setWeightSum(10);

            if (mTheFacts.get(currentFact))
            {
                ll.setBackgroundColor(Color.GREEN);
            }
            else
            {
                ll.setBackgroundColor(Color.RED);
            }

            final AutoResizeTextView fact = new AutoResizeTextView(this);
            fact.setText(currentFact);
            fact.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6f));
            ll.addView(fact);

            final Button edit = new Button(this);
            edit.setText("Edit");
            edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScrollViewFacts.setVisibility(View.GONE);
                    mLinearLaytoutEditFacts.setVisibility(View.VISIBLE);
                    mFactEditor.setText(currentFact);
                    deleteFact(currentFact);
                }
            });
            ll.addView(edit);

            ImageView delete = new ImageView(this);
            delete.setImageDrawable(getResources().getDrawable(R.drawable.tick));
            delete.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLinearLayoutContainingFacts.removeView(ll);
                    mTheFacts.remove(currentFact);
                    deleteFact(currentFact);
                }
            });
            ll.addView(delete);

            mLinearLayoutContainingFacts.addView(ll);
        }
        Button add = new Button(this);
        add.setText("Add");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollViewFacts.setVisibility(View.GONE);
                mLinearLaytoutEditFacts.setVisibility(View.VISIBLE);
                mFactEditor.setText("");
            }
        });
        mLinearLayoutContainingFacts.addView(add);
    }

    private void onButtonPressed(Boolean button)
    {
        if (mFactEditor.getText().toString() != null)
        {
            mTheFacts.put(mFactEditor.getText().toString(), button);
            deleteFact(mTempFileName);
            mScrollViewFacts.setVisibility(View.VISIBLE);
            mLinearLaytoutEditFacts.setVisibility(View.GONE);
        }
    }


    private void deleteFact(String factToDelete)
    {
        ArrayList<String> facts = new ArrayList<>(mTheFacts.keySet());
        try
        {
            FileWriter fileWriter = new FileWriter(mFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int ii = 0; ii < facts.size(); ii++)
            {
                if (!facts.get(ii).equals(factToDelete))
                {
                    bufferedWriter.write(facts.get(ii) + "#" + mTheFacts.get(facts.get(ii)));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        readFacts();

    }


    private void checkFileExists()
    {
        if (!mFile.exists())
        {
            System.out.println("Create a new file");
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

    private void readFacts()
    {
        mTheFacts.clear();
        checkFileExists();
        try
        {
            FileReader fileReader = new FileReader(mFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            System.out.println(mFile.getAbsolutePath());
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line);
                String[] split = line.split("#");
                mTheFacts.put(split[0], Boolean.valueOf(split[1]));
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void onSavePressed()
    {
        UserFactTools userFileTools = new UserFactTools(this);
        String newFileName = mEditFileName.getText().toString();
        String oldFileName = getIntent().getStringExtra("file name");
        if (userFileTools.getListOfFiles().contains(newFileName) && !(newFileName.equals(oldFileName)))
        {
            //We are overriding an existing file
            Toast.makeText(this,"File already exists",Toast.LENGTH_LONG).show();
        }
        else if (mScrollViewFacts.getVisibility() == View.GONE)
        {
            Toast.makeText(this,"Please finish editing the current fact.",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (userFileTools.getListOfFiles().contains(oldFileName))
            {
                System.out.println("edit");

                userFileTools.changeFileName(oldFileName, newFileName);
            }
            else
            {
                System.out.println("add");
                userFileTools.addFile(newFileName);
            }
            String edittedname = newFileName.replace(" ","_");
            edittedname.toLowerCase();
            String path = getFilesDir() + "/" + edittedname;
            File newFile = new File(path);
            //mFile.delete();
            //mFile = newFile;
            deleteFact(mTempFileName);
            onBackButtonPressed();
        }
    }

    private void onBackButtonPressed()
    {
        if (mScrollViewFacts.getVisibility() == View.GONE)
        {
            Toast.makeText(this,"Please finish editing the current fact.",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent back = new Intent(EditUserFiles.this, UserFileMenu.class);
            startActivity(back);
            finish();
        }
    }
}
