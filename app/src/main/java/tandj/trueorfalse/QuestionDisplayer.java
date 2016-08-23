package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 17/08/2016.
 * Class which displays the questions and allows the user to answer
 */
public class QuestionDisplayer extends Activity {

    /**
     * The hashmap containing all the facts
     */
    private HashMap<String, Boolean> mHashMap;

    /**
     * Tool class for dealing with hashmap
     */
    private HashMapTools mHashMapTools;

    /**
     * Text view that displays the fact
     */
    private TextView mFactDisplayer;

    /**
     * False button
     */
    private Button mFalseButton;

    /**
     * True button
     */
    private Button mTrueButton;

    //Text views that display the correct & incorrect messages
    private TextView mCorrect;
    private TextView mIncorrect;

    /**
     * Handler to provide timing to the game
     */
    private Handler mHandler = new Handler();

    /**
     * Score
     */
    private int mScore;

    /**
     * Number of rounds
     */
    private int mNumberOfQuestions;

    /**
     * Maximum number of questions to be asked
     */
    private int MAX_QUESTIONS;

    /**
     * Seek bar to set how many points you want to gamble
     */
    private SeekBar mGamblingBar;

    /**
     * Text view showing current score
     */
    private TextView mScoreDisplay;

    /**
     * Text view showing score to gamble
     */
    private TextView mScoreToGambleDisplay;

    /**
     * LinearLayout containg fact and buttons
     */
    private LinearLayout mButtonAndFactDisplayer;

    /**
     * Integer showing points we're willing to gamble
     */
    private int mPointsToGamble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHashMapTools = new HashMapTools(FactFiles.MATHS_FACTS, this);

        mScore = getResources().getInteger(R.integer.starting_score);
        MAX_QUESTIONS = getResources().getInteger(R.integer.number_of_questions_per_round);

        setUpDisplay();

        setFact();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay() {
        setContentView(R.layout.question_displayer_layout);

        mButtonAndFactDisplayer = (LinearLayout) findViewById(R.id.fact_and_button_displayer);
        mFactDisplayer = (TextView) findViewById(R.id.fact_displayer);

        mCorrect = (TextView) findViewById(R.id.correct_display);
        mIncorrect = (TextView) findViewById(R.id.incorrect_display);
        mIncorrect.setVisibility(View.GONE);
        mCorrect.setVisibility(View.GONE);
        mTrueButton    = (Button)   findViewById(R.id.true_button);
        mFalseButton   = (Button)   findViewById(R.id.false_button);
        mScoreDisplay  = (TextView) findViewById(R.id.score_displayer);
        mScoreToGambleDisplay = (TextView) findViewById(R.id.score_to_gamble);
        mGamblingBar   = (SeekBar)  findViewById(R.id.seekBar);
        mGamblingBar.setProgress(1);
        mGamblingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mPointsToGamble = (int) ((progress * (mScore - 1) / 100)+1);
                mScoreToGambleDisplay.setText("Gamble: " + mPointsToGamble);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpButtons();
        setScoreDisplays();
    }

    /**
     * Fills the text view with a random fact from the hashmap
     */
    private void setFact() {
        mFactDisplayer.setText(mHashMapTools.getRandomItem());
    }

    /**
     * Method which deals with the result of a true or false button being pressed
     *
     * @param answer The button that has been pressed
     */
    private void onButtonClicked(Boolean answer) {
        if (answer == mHashMapTools.getTrueOrFalse()) {
            //Answer is correct, set a new question

            mCorrect.setVisibility(View.VISIBLE);
            mButtonAndFactDisplayer.setVisibility(View.GONE);

            mHandler.postDelayed(new Runnable() {
                public void run() {
                    setFact();
                    mCorrect.setVisibility(View.GONE);
                    mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                }
            }, 2000);
            calculateNewScore(true);

        } else {
            //Answer is wrong
            mIncorrect.setVisibility(View.VISIBLE);
            mButtonAndFactDisplayer.setVisibility(View.GONE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFact();
                    mIncorrect.setVisibility(View.GONE);
                    mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                }
            },2000);
            calculateNewScore(false);
        }
        if (currentScore() == 0)
        {
            Intent returnToStart = new Intent(QuestionDisplayer.this, MainScreen.class);
            startActivity(returnToStart);
        }
    }

    /**
     * Sets the on click listeners for the true and false buttons
     */
    private void setUpButtons() {
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(false);
            }
        });

    }

    /**
     * Updates the score display and the seek bar
     */
    private void setScoreDisplays()
    {
        mGamblingBar.setProgress(0);
        mScoreDisplay.setText("Score = " +mScore);
    }

    /**
     * Calculates the new score once an answer is given
     * @param correct If the answer is correct or now
     */
    private void calculateNewScore(boolean correct)
    {
        if (correct)
        {
            mScore = mScore + mPointsToGamble;
        }
        else
        {
            mScore = mScore - mPointsToGamble;
        }
        setScoreDisplays();

        FileTools.writeData(FactFileNames.fileNames[FactFileNames.MATHS_FACTS], mScore);
    }

    private int currentScore(){
        return mScore;
    }
}
