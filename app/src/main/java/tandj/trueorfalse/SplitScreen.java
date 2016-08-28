package tandj.trueorfalse;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplitScreen extends AppCompatActivity {

    private Button mTrueButton1;
    private Button mFalseButton1;
    private Button mTrueButton2;
    private Button mFalseButton2;
    private TextView mFactDisplayer1;
    private TextView mFactDisplayer2;
    private TextView mScoreText1;
    private TextView mScoreText2;
    private int mScore1;
    private int mScore2;
    private HashMapTools mHashMapTools;
    private CountDownTimer mCountDownTimer;



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
        mTrueButton1 = (Button) findViewById(R.id.true_1);
        mFalseButton1 = (Button) findViewById(R.id.false_1);
        mTrueButton2 = (Button) findViewById(R.id.true_2);
        mFalseButton2 = (Button) findViewById(R.id.false_2);
        mFactDisplayer1 = (TextView) findViewById(R.id.fact_displayer_1);
        mFactDisplayer2 = (TextView) findViewById(R.id.fact_displayer_2);
        mScoreText1 = (TextView) findViewById(R.id.score_1);
        mScoreText2 = (TextView) findViewById(R.id.score_2);

        mTrueButton1.setVisibility(View.GONE);
        mFalseButton1.setVisibility(View.GONE);
        mTrueButton2.setVisibility(View.GONE);
        mFalseButton2.setVisibility(View.GONE);

    }

    private void setUpContent() {
        mScore1 = 0;
        mScore2 = 0;
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
        mCountDownTimer = new CountDownTimer(5000, 1000) {
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
                mTrueButton1.setVisibility(View.VISIBLE);
                mFalseButton1.setVisibility(View.VISIBLE);
                mTrueButton2.setVisibility(View.VISIBLE);
                mFalseButton2.setVisibility(View.VISIBLE);
                setFact();
            }
        }.start();
    }

    private void setFactDisplays(String text) {
        mFactDisplayer1.setText(text);
        mFactDisplayer2.setText(text);
    }

    private void setFact() {
        setFactDisplays(mHashMapTools.getRandomItem());
    }

    private void updateScoreText() {
        mScoreText1.setText("Score: " + mScore1);
        mScoreText2.setText("Score: " + mScore2);
    }

    private void setUpButtons() {
        mTrueButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(1,true);
            }
        });
        mFalseButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(1,false);
            }
        });
        mTrueButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(2,true);
            }
        });
        mFalseButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(2,false);
            }
        });

        setHomeButton();
    }

    private void onButtonClicked(int buttonNo, Boolean answer) {

    }
}
