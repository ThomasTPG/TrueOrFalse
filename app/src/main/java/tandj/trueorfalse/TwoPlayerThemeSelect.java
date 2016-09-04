package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 28/08/2016.
 */
public class TwoPlayerThemeSelect extends Activity {

    private LinearLayout mDisplayer;

    private ArrayList<String> selectedFacts = new ArrayList<String>();

    private Bundle mBundle;

    private FileTools mFileTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_theme_select);

        Intent intent = getIntent();
        mBundle = intent.getExtras();
        mFileTools = new FileTools(this);

        mDisplayer = (LinearLayout) findViewById(R.id.store_check_boxes);

        createBackButton();
        setOptionsDisplay();
        createStartButton();
    }

    private void createBackButton()
    {
        Button back = (Button) findViewById(R.id.back_button_theme_select);
        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(TwoPlayerThemeSelect.this, TwoPlayer.class);
                startActivity(back);
                finish();

            }
        });
    }

    private void createStartButton()
    {
        Button back = (Button) findViewById(R.id.start_two_player);
        back.setText("Start");
        back.setEnabled(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start;
                if (mBundle.getString("Mode").equals("Split"))
                {
                    start = new Intent(TwoPlayerThemeSelect.this, SplitScreen.class);
                }
                else
                {
                    start = new Intent(TwoPlayerThemeSelect.this, TimeTrialWaiting.class);
                    start.putExtra("Next Player", "Player 1");
                }
                start.putExtra("Questions", (String[]) selectedFacts.toArray(new String[0]));
                startActivity(start);
                finish();

            }
        });
    }

    private void setThemeButtons()
    {

    }

    private void setOptionsDisplay()
    {
        for (int ii = 0; ii < FactFileNames.difficultyArrays.length; ii++)
        {
            int totalScore = 0;
            for (int jj = 0; jj < FactFileNames.difficultyArrays[ii].length; jj ++)
            {
                final String currentFile = FactFileNames.difficultyArrays[ii][jj];
                for (int ll = 0; ll < FactFileNames.allFiles.length; ll++) {
                    if (FactFileNames.allFiles[ll].equals(currentFile)) {
                        totalScore = totalScore + mFileTools.getScore(FactFileNames.fileNames[ll]);
                    }
                }

            }


            String resName = "scores_to_unlock_" + FactFileNames.difficulties[ii].toLowerCase();
            int id = getResources().getIdentifier(resName, "integer", getPackageName());
            int requiredScore = getResources().getInteger(id);
            System.out.println(totalScore + " asfi " + requiredScore + resName);
            if (totalScore >= requiredScore)
            {
                for (int kk = 0; kk < FactFileNames.difficultyArrays[ii].length; kk ++)
                {
                    CheckBox newCheckBox = new CheckBox(this);
                    System.out.println("new check box");
                    final String currentFile = FactFileNames.difficultyArrays[ii][kk];
                    newCheckBox.setText(currentFile);
                    newCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox) v).isChecked()) {
                                for (int ll = 0; ll < FactFileNames.allFiles.length; ll++) {
                                    if (FactFileNames.allFiles[ll].equals(currentFile)) {
                                        selectedFacts.add(FactFileNames.fileNames[ll]);
                                    }
                                }

                            } else {
                                for (int ll = 0; ll < FactFileNames.allFiles.length; ll++) {
                                    if (FactFileNames.allFiles[ll].equals(currentFile)) {
                                        selectedFacts.remove(FactFileNames.fileNames[ll]);
                                    }
                                }
                            }
                            Button start = (Button) findViewById(R.id.start_two_player);
                            if (selectedFacts.size() > 0) {
                                start.setEnabled(true);
                            } else {
                                start.setEnabled(false);
                            }
                        }
                    });

                    mDisplayer.addView(newCheckBox);
                }

            }
            else
            {
                break;
            }
        }

    }


}
