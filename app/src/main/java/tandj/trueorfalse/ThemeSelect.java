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

public class ThemeSelect extends Activity {

    private Spinner mThemeSpinner;
    private Spinner mDifficultySpinner;
    private Button  mGoButton;
    private TextView mScoreDisplay;
    private String defaultFile = FactFileNames.fileNames[0];
    String mFileToOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileToOpen = defaultFile;
        setContentView(R.layout.content_theme_select);
        setUpDisplay();
        addListenerOnButton();
    }

    private void setUpDisplay() {
        mGoButton = (Button) findViewById(R.id.go);
        mThemeSpinner = (Spinner) findViewById(R.id.theme_spinner);
        mDifficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        mScoreDisplay = (TextView) findViewById(R.id.your_hiscore);
        mScoreDisplay.setText("High score: " + FileTools.getScore(defaultFile));

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
                String selectedTheme = String.valueOf(mThemeSpinner.getSelectedItem());

                for (int ii = 0; ii < FactFileNames.allFiles.length; ii++)
                {
                    if (selectedTheme.equals(FactFileNames.allFiles[ii]))
                    {
                        mFileToOpen = FactFileNames.fileNames[ii];
                        mScoreDisplay.setText("High score: " + FileTools.getScore(mFileToOpen));

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
                String selectedDifficulty = String.valueOf(mDifficultySpinner.getSelectedItem());

                for (int ii = 0; ii < FactFileNames.difficulties.length; ii++) {
                    if (selectedDifficulty.equals(FactFileNames.difficulties[ii])) {
                        setUpThemeSpinner(FactFileNames.difficultyArrays[ii]);
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
                Intent Start = new Intent(ThemeSelect.this, QuestionDisplayer.class);
                Start.putExtra("theme",mFileToOpen);
                startActivity(Start);
            }

        });
    }

    private void setUpThemeSpinner(String[] array) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mThemeSpinner.setAdapter(adapter);
    }

    private void setUpDifficultySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FactFileNames.difficulties);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mDifficultySpinner.setAdapter(adapter);
    }
}
