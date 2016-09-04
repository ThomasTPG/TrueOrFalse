package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ThemeSelect extends Activity {

    private Spinner mThemeSpinner;
    private Spinner mDifficultySpinner;
    private Button  mGoButton;
    private TextView mScoreDisplay;
    private TextView mDifficultyScoreDisplay;
    private String defaultFile = FactFileNames.fileNames[0];
    String mFileToOpen;
    ArrayList<String> selectedFactFiles = new ArrayList<String>();
    private TextView mChooseThemeText;
    private String mSelectedTheme;
    public static int mLastSinglePlayerTheme;
    private String mSelectedDifficulty;
    public static int mLastSinglePlayerDifficulty;
    public String mSelectedThemeRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileToOpen = defaultFile;
        setContentView(R.layout.content_theme_select);
        setUpDisplay();
        addListenerOnButton();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(ThemeSelect.this, MainScreen.class);
        startActivity(back);
        finish();
    }

    private void setUpDisplay() {
        mGoButton = (Button) findViewById(R.id.go);
        mThemeSpinner = (Spinner) findViewById(R.id.theme_spinner);
        mDifficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        mScoreDisplay = (TextView) findViewById(R.id.your_hiscore);
        mScoreDisplay.setText("High score: " + FileTools.getScore(defaultFile));
        mDifficultyScoreDisplay = (TextView) findViewById(R.id.difficulty_score);
        mDifficultyScoreDisplay.setText("Difficuly Score");
        mChooseThemeText = (TextView) findViewById(R.id.choose_theme);


        setUpThemeSpinner(FactFileNames.easyFiles);
        setUpDifficultySpinner();

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();
    }

    //add spinner data
    public void addListenerOnSpinnerItemSelection() {
        mThemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTheme = String.valueOf(mThemeSpinner.getSelectedItem());
                mSelectedThemeRemember = mSelectedTheme;
                mSelectedTheme = mSelectedTheme.split(":")[0];

                if (mSelectedTheme.equals("All"))
                {
                    for (int ii = 0; ii< FactFileNames.difficulties.length; ii++)
                    {
                        if (mSelectedDifficulty.equals(FactFileNames.difficulties[ii]))
                        {
                            selectedFactFiles.clear();
                            for (int jj = 0; jj < FactFileNames.difficultyArrays[ii].length; jj++)
                            {
                                for (int kk = 0; kk < FactFileNames.allFiles.length; kk++)
                                {
                                    if (FactFileNames.difficultyArrays[ii][jj].equals(FactFileNames.allFiles[kk]))
                                    {
                                        selectedFactFiles.add(FactFileNames.fileNames[kk]);
                                        mScoreDisplay.setText("High score: " + FileTools.getScore("All " + mSelectedDifficulty));
                                    }
                                }
                            }
                        }
                    }
                }
                else if (mSelectedTheme.equals("Random"))
                {
                    for (int ii = 0; ii< FactFileNames.difficulties.length; ii++)
                    {
                        if (mSelectedDifficulty.equals(FactFileNames.difficulties[ii]))
                        {
                            String[] array = FactFileNames.difficultyArrays[ii];
                            int index = (int) Math.floor(Math.random() * array.length);
                            selectedFactFiles.clear();
                            for (int kk = 0; kk < FactFileNames.allFiles.length; kk++)
                            {
                                if (FactFileNames.difficultyArrays[ii][index].equals(FactFileNames.allFiles[kk]))
                                {
                                    selectedFactFiles.add(FactFileNames.fileNames[kk]);
                                    mScoreDisplay.setText("");

                                }
                            }
                        }
                    }
                }
                else
                {
                    for (int ii = 0; ii < FactFileNames.allFiles.length; ii++)
                    {
                        if (mSelectedTheme.equals(FactFileNames.allFiles[ii]))
                        {
                            selectedFactFiles.clear();
                            selectedFactFiles.add(FactFileNames.fileNames[ii]);
                            mScoreDisplay.setText("High score: " + FileTools.getScore(FactFileNames.fileNames[ii]));

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChooseThemeText.setText("Choose Theme:");
                mThemeSpinner.setVisibility(View.VISIBLE);
                mSelectedDifficulty = String.valueOf(mDifficultySpinner.getSelectedItem());

                for (int ii = 0; ii < FactFileNames.difficulties.length; ii++) {
                    if (mSelectedDifficulty.equals(FactFileNames.difficulties[ii])) {
                        switch (ii) {
                            case 0:

                                setUpThemeSpinner(FactFileNames.difficultyArrays[ii]);
                                mDifficultyScoreDisplay.setText("Difficulty score: " + countDifficultyScores("Easy"));
                                break;
                            case 1:
                                mDifficultyScoreDisplay.setText("Difficulty score: " + countDifficultyScores("Normal"));
                                if (difficultyThresholds() < 1) {
                                    mChooseThemeText.setText("You need " + getResources().getInteger(R.integer.scores_to_unlock_medium) + " points in Easy mode to unlock Medium mode");
                                    mThemeSpinner.setVisibility(View.GONE);
                                }
                                else {
                                    setUpThemeSpinner(FactFileNames.difficultyArrays[ii]);
                                }
                                break;
                            case 2:
                                mDifficultyScoreDisplay.setText("Difficulty score: " + countDifficultyScores("Hard"));
                                if (difficultyThresholds() < 2) {
                                    mChooseThemeText.setText("You need " + getResources().getInteger(R.integer.scores_to_unlock_hard) + " points in Medium mode to unlock Hard mode");
                                    mThemeSpinner.setVisibility(View.GONE);
                                }
                                else {
                                    setUpThemeSpinner(FactFileNames.difficultyArrays[ii]);
                                }
                                break;
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //get selected spinner value
    public void addListenerOnButton() {
        mGoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberOptions();
                Intent Start = new Intent(ThemeSelect.this, QuestionDisplayer.class);
                Start.putExtra("theme",(String[]) selectedFactFiles.toArray(new String[0]));
                Start.putExtra("Difficulty", mSelectedDifficulty);
                startActivity(Start);
            }

        });
    }

    private void setUpThemeSpinner(String[] array) {
        String[] newArray = new String[array.length + 2];
        for (int mm = 0; mm < array.length; mm++)
        {
            for (int ii = 0; ii < FactFileNames.allFiles.length; ii++)
            {
                if (array[mm].equals(FactFileNames.allFiles[ii]))
                {
                    newArray[mm] = array[mm] + ": " + FileTools.getScore(FactFileNames.fileNames[ii]);
                }
            }
        }
        newArray[array.length] = "Random";
        newArray[array.length + 1] = "All: " + FileTools.getScore("All " + mSelectedDifficulty);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, newArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mThemeSpinner.setAdapter(adapter);
        mThemeSpinner.setSelection(mLastSinglePlayerTheme);
//        mThemeSpinner.setSelection(1);
    }

    private void setUpDifficultySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FactFileNames.difficulties);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mDifficultySpinner.setAdapter(adapter);
        mDifficultySpinner.setSelection(mLastSinglePlayerDifficulty);
//        mDifficultySpinner.setSelection(1);
    }

    public int countDifficultyScores(String difficulty) {
        int totalScore = 0;
        if (difficulty.equals("Easy")) {
            for (int i = 0; i < FactFileNames.easyFiles.length; i++) {
                String selectedTheme = FactFileNames.easyFiles[i];
                for (int ii = 0; ii < FactFileNames.allFiles.length; ii++) {
                    if (selectedTheme.equals(FactFileNames.allFiles[ii])) {
                        mFileToOpen = FactFileNames.fileNames[ii];
                        totalScore = totalScore + FileTools.getScore(mFileToOpen);

                    }
                }
            }
        }
        if (difficulty.equals("Normal")) {
            for (int i = 0; i < FactFileNames.mediumFiles.length; i++) {
                String selectedTheme = FactFileNames.mediumFiles[i];
                for (int ii = 0; ii < FactFileNames.allFiles.length; ii++) {
                    if (selectedTheme.equals(FactFileNames.allFiles[ii])) {
                        mFileToOpen = FactFileNames.fileNames[ii];
                        totalScore = totalScore + FileTools.getScore(mFileToOpen);

                    }
                }
            }
        }
        if (difficulty.equals("Hard")) {
            for (int i = 0; i < FactFileNames.hardFiles.length; i++) {
                String selectedTheme = FactFileNames.hardFiles[i];
                for (int ii = 0; ii < FactFileNames.allFiles.length; ii++) {
                    if (selectedTheme.equals(FactFileNames.allFiles[ii])) {
                        mFileToOpen = FactFileNames.fileNames[ii];
                        totalScore = totalScore + FileTools.getScore(mFileToOpen);

                    }
                }
            }
        }
        return totalScore;
    }

    private int difficultyThresholds() {
        int val = 0;
        if (countDifficultyScores("Easy") > getResources().getInteger(R.integer.scores_to_unlock_medium)){
            val=1;
            if (countDifficultyScores("Normal") > getResources().getInteger(R.integer.scores_to_unlock_hard)) {
                val =2;
            }
        }
        return val;
    }

    private void rememberOptions() {
        mLastSinglePlayerTheme = ((ArrayAdapter) mThemeSpinner.getAdapter()).getPosition(mSelectedThemeRemember);
        mLastSinglePlayerDifficulty = ((ArrayAdapter) mDifficultySpinner.getAdapter()).getPosition(mSelectedDifficulty);
    }
}
