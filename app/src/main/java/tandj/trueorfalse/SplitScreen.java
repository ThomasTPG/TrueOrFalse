package tandj.trueorfalse;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplitScreen extends AppCompatActivity {

    private Button[] mTrueButton = new Button[2];
    private Button[] mFalseButton = new Button[2];
    private TextView[] mFactDisplayer = new TextView[2];
    private TextView[] mScoreText = new TextView[2];
    private int[] mScore = new int[2];
    private HashMapTools mHashMapTools;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_screen);

        mHashMapTools = new HashMapTools("cambridge_facts.txt", this);

        setUpDisplay();
        setUpContent();
        updateScoreText();
        beginCountdown();
    }

    private void setUpDisplay() {
        setUpButtons();

        mTrueButton[0] = (Button) findViewById(R.id.true_1);
        mFalseButton[0] = (Button) findViewById(R.id.false_1);
        mTrueButton[1] = (Button) findViewById(R.id.true_2);
        mFalseButton[1] = (Button) findViewById(R.id.false_2);
        mFactDisplayer[0] = (TextView) findViewById(R.id.fact_displayer_1);
        mFactDisplayer[1] = (TextView) findViewById(R.id.fact_displayer_2);
        mScoreText[0] = (TextView) findViewById(R.id.score_1);
        mScoreText[1] = (TextView) findViewById(R.id.score_2);

        for (int i=0; i<1; i++) {
            mTrueButton[i].setVisibility(View.GONE);
            mFalseButton[i].setVisibility(View.GONE);
        }


    }

    private void setUpContent() {
        for (int i=0; i<1; i++) {
            mScore[i] = 0;
        }
    }

    private void setHomeButton() {
        Button home = (Button) findViewById(R.id.split_home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(SplitScreen.this, MainScreen.class);
                startActivity(start);
                finish();
            }
        });
    }

    private void beginCountdown() {
        CountDownTimer mCountDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                String text = Long.toString(millisUntilFinished / 1000);
                int count = (int) (millisUntilFinished / 1000) - 1;
                if (count != 0) {
                    setFactDisplays(Long.toString((millisUntilFinished / 1000) - 1));
                } else {
                    setFactDisplays("Go!");
                }
            }

            @Override
            public void onFinish() {
                for (int i=0; i<1; i++) {
                    mTrueButton[i].setVisibility(View.VISIBLE);
                    mFalseButton[i].setVisibility(View.VISIBLE);
                }
                setFact();
            }
        }.start();
    }

    private void setFactDisplays(String text) {
        for (int i=0; i<1; i++) {
            mFactDisplayer[i].setText(text);
        }
    }

    private void setFact() {
        setFactDisplays(mHashMapTools.getRandomItem());
    }

    private void updateScoreText() {
        for (int i=0; i<1; i++) {
            mScoreText[i].setText("Score: " + mScore[i]);
        }
    }

    private void setUpButtons() {
        mTrueButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(1,true);
            }
        });
        mFalseButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(1,false);
            }
        });
        mTrueButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(2,true);
            }
        });
        mFalseButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(2,false);
            }
        });

        setHomeButton();
    }

    private void onButtonClicked(int playerNo, Boolean answer) {
        if (answer == mHashMapTools.getTrueOrFalse()) {
            if (playerNo == 1) {
                mFactDisplayer[0].setText("Correct");
                mFactDisplayer[1].setText("Too slow");
                mScore[0] = mScore[0] + 1;
            } else if (playerNo == 2) {
                mFactDisplayer[1].setText("Correct");
                mFactDisplayer[0].setText("Too slow");
                mScore[1] = mScore[1] + 1;
            }
        }

    }

    private void disableButtons(int playerNo) {
        mTrueButton[playerNo-1].setEnabled(false);
        mFalseButton[playerNo-1].setEnabled(false);
    }
}
