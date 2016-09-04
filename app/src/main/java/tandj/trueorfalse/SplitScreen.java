package tandj.trueorfalse;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplitScreen extends AppCompatActivity {

    private Button[] mTrueButton = new Button[2];
    private Button[] mFalseButton = new Button[2];
    private TextView[] mFactDisplayer = new TextView[2];
    private TextView[] mScoreText = new TextView[2];
    private int[] mScore = new int[2];
    private HashmapCombined mHashMapTools;
    private Handler mHandler = new Handler();
    private boolean mQuestionIsOver;
    private int mNumAnswersGiven;
    private int mPlayerNo;
    private TextView mCountdown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_screen);

        String[] questions = getIntent().getStringArrayExtra("Questions");

        mHashMapTools = new HashmapCombined(questions,this);

        setUpDisplay();
        setUpContent();
        updateScoreText();
        beginCountdown();
    }

    private void setUpDisplay() {
        mTrueButton[0] = (Button) findViewById(R.id.true_1);
        mFalseButton[0] = (Button) findViewById(R.id.false_1);
        mTrueButton[1] = (Button) findViewById(R.id.true_2);
        mFalseButton[1] = (Button) findViewById(R.id.false_2);
        mFactDisplayer[0] = (TextView) findViewById(R.id.fact_displayer_1);
        mFactDisplayer[1] = (TextView) findViewById(R.id.fact_displayer_2);
        mScoreText[0] = (TextView) findViewById(R.id.score_1);
        mScoreText[1] = (TextView) findViewById(R.id.score_2);
        mCountdown = (TextView) findViewById(R.id.split_countdown);

        setUpButtons();

        for (int i=0; i<2; i++) {
            mTrueButton[i].setVisibility(View.GONE);
            mFalseButton[i].setVisibility(View.GONE);
        }


    }

    private void setUpContent() {
        for (int i=0; i<2; i++) {
            mScore[i] = 0;
        }
        mQuestionIsOver = false;
        mNumAnswersGiven = 0;
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
                    setCountdownText(Long.toString((millisUntilFinished / 1000) - 1));
                } else {
                    setCountdownText("Go!");
                }
            }

            @Override
            public void onFinish() {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.split_countdown_ll);
                linearLayout.setVisibility(View.GONE);
                for (int i=0; i<2; i++) {
                    mTrueButton[i].setVisibility(View.VISIBLE);
                    mFalseButton[i].setVisibility(View.VISIBLE);
                }
                setFact();
            }
        }.start();
    }

    private void setFactDisplays(String text) {
        for (int i=0; i<2; i++) {
            mFactDisplayer[i].setText(text);
        }
    }

    private void setCountdownText(String text) {
        mCountdown.setText(text);
    }

    private void setFact() {
        setFactDisplays(mHashMapTools.getRandomItem());
    }

    private void updateScoreText() {
        for (int i=0; i<2; i++) {
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
        disableButtons(playerNo);
        mNumAnswersGiven = mNumAnswersGiven + 1;
        if (answer == mHashMapTools.getTrueOrFalse()) {
            mQuestionIsOver = true;
            if (playerNo == 1) {
                mFactDisplayer[0].setText("Correct");
                if (mNumAnswersGiven == 1) {
                    mFactDisplayer[1].setText("Too slow");
                }
                mScore[0] = mScore[0] + 1;
                disableButtons(2);
            } else if (playerNo == 2) {
                mFactDisplayer[1].setText("Correct");
                if (mNumAnswersGiven == 1) {
                    mFactDisplayer[0].setText("Too slow");
                }
                mScore[1] = mScore[1] + 1;
                disableButtons(1);
            }
        } else {
            disableButtons(playerNo);
            mFactDisplayer[playerNo-1].setText("Incorrect");
        }

        if (mNumAnswersGiven == 2) {
            mQuestionIsOver = true;
        }

        if (mScore[0] < 5 && mScore[1] < 5) {
            if (mQuestionIsOver) {
                newFact();
            }
        } else {
            if (mScore[0] > 4) {
                endGame(1);
            } else {
                endGame(2);
            }
        }

    }

    private void disableButtons(int playerNo) {
        mTrueButton[playerNo-1].setEnabled(false);
        mFalseButton[playerNo-1].setEnabled(false);
    }

    private void enableAllButtons() {
        for (int i=0; i<2; i++) {
            mTrueButton[i].setEnabled(true);
            mFalseButton[i].setEnabled(true);
        }
    }

    private void newFact() {
        updateScoreText();
        mQuestionIsOver = false;
        mNumAnswersGiven = 0;
        mHandler.postDelayed(new Runnable() {
            public void run() {
                setFact();
                enableAllButtons();
            }
        }, 2000);
    }

    private void endGame(int playerNo) {
        updateScoreText();
        statisticsUpdated  stats = new statisticsUpdated(this);
        stats.updateStat(statisticsUpdated.NUMBER_OF_SPLIT_SCREEN_GAMES,1);
        mQuestionIsOver = false;
        mNumAnswersGiven = 0;
        mPlayerNo = playerNo;

        mHandler.postDelayed(new Runnable() {
            public void run() {
                for (int i=0; i<2; i++) {
                    mTrueButton[i].setVisibility(View.GONE);
                    mFalseButton[i].setVisibility(View.GONE);
                }
                if (mPlayerNo == 1) {
                    mFactDisplayer[0].setText("You Win!\nPlayer 1 Score: " + mScore[0] + "\nPlayer 2 Score: " + mScore[1]);
                    mFactDisplayer[1].setText("You Lose!\nPlayer 1 Score: " + mScore[0] + "\nPlayer 2 Score: " + mScore[1]);
                } else {
                    mFactDisplayer[1].setText("You Win!\nPlayer 1 Score: " + mScore[0] + "\nPlayer 2 Score: " + mScore[1]);
                    mFactDisplayer[0].setText("You Lose!\nPlayer 1 Score: " + mScore[0] + "\nPlayer 2 Score: " + mScore[1]);
                }
            }
        }, 2000);


    }
}
