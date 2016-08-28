package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Thomas on 28/08/2016.
 */
public class TimeTrial extends Activity{

    CountDownTimer mCountDownTimer;

    int LENGTH_PER_ROUND_MS = 30*1000;

    private HashmapCombined mFactStore;

    TextView mTimeRemainingDisplay;

    TextView mFactDisplay;

    TextView mScoreDisplayer;

    TextView mPlayerDisplayer;

    Button mTrueButton;

    Button mFalseButton;

    private int mScore = 0;

    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBundle = getIntent().getExtras();

        mFactStore = new HashmapCombined(mBundle.getStringArray("Selected Facts"), this);

        setUpDisplay();

        mFactDisplay.setText(mFactStore.getRandomItem());

        mPlayerDisplayer.setText(mBundle.getString("Current Player"));

        setScoreDisplay();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(true);

            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(false);
            }
        });

        setUpCountDown();
    }

    private void onAnswerSelected(boolean answer)
    {
        boolean correctAnswer = mFactStore.getTrueOrFalse();

        if(answer == correctAnswer)
        {
            mScore ++;
        }
        else
        {
            mScore --;
        }

        setScoreDisplay();
        mFactDisplay.setText(mFactStore.getRandomItem());
    }


    private void setUpCountDown()
    {
        mCountDownTimer = new CountDownTimer(LENGTH_PER_ROUND_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                mTimeRemainingDisplay.setText("Time remaining: " + secondsRemaining);
            }

            @Override
            public void onFinish() {
                Intent end;
                if (mBundle.getString("Current Player").equals("Player 1"))
                {
                    end = new Intent(TimeTrial.this, TimeTrialWaiting.class);
                    end.putExtra("Next Player", "Player 2");
                    end.putExtra("Player 1 Score", mScore);
                }
                else
                {
                    end = new Intent(TimeTrial.this, TimeTrialFinished.class);
                    end.putExtra("Player 1 Score", mBundle.getInt("Player 1 Score"));
                }
                startActivity(end);
                this.cancel();
                finish();
            }
        };
    }

    private void setScoreDisplay()
    {
        mScoreDisplayer.setText("Score: " + mScore);
    }

    private void setUpDisplay()
    {
        mFactDisplay = (TextView) findViewById(R.id.fact_displayer);

        mPlayerDisplayer = (TextView) findViewById(R.id.current_player);

        mScoreDisplayer = (TextView) findViewById(R.id.score_displayer);

        mTimeRemainingDisplay = (TextView) findViewById(R.id.time_remaining);

        mTrueButton = (Button) findViewById(R.id.true_button_time_trial);

        mFalseButton = (Button) findViewById(R.id.false_button_time_trial);
    }
}
