package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;


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
    private HashmapCombined mHashMapTools;

    /**
     * Text view that displays the fact
     */
    private TextView mFactDisplayer;

    /**
     * False button
     */
    private Button mGoButton;

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

    //Text view showing question number
    private TextView mQuestionNumber;

    private TextView mPointsOnTrueDisplay;

    private int MS_WHILE_WAITING = 2000;

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

    private int mPointsOnTrue;

    private int mPointsOnFalse;

    //array to track the number of points after each round
    private int[] mPointsTracker;

    //array to track the questions asked and if the user was correct
    private int[] mAnswerTracker;

    private Button mQuitButton;


    private TextView mCountdownText;

    private CountDownTimer mCountdownTimerRound;

    private CountDownTimer mCountdownTimerWaiting;

    private String mTheme;

    private String[] mThemesSelected;

    private int mMissedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        mThemesSelected = b.getStringArray("theme");
        if(mThemesSelected.length == 1)
        {
            mTheme = mThemesSelected[0];
        }
        else
        {
            mTheme = "All " + b.getString("Difficulty");
        }
//        mTheme = "cambridge_facts.txt";
        mHashMapTools = new HashmapCombined(mThemesSelected, this);



        mScore = getResources().getInteger(R.integer.starting_score);
        MAX_QUESTIONS = getResources().getInteger(R.integer.number_of_questions_per_round);
        mPointsTracker = new int[MAX_QUESTIONS];
        mAnswerTracker = new int[MAX_QUESTIONS];

        setUpDisplay();

        setUpCountdownTimers();
        mCountdownTimerRound.start();

        setFact();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay() {
        setContentView(R.layout.question_displayer_layout);

        mCountdownText = (TextView) findViewById(R.id.time_countdown);
        mQuitButton = (Button) findViewById(R.id.quit);
        mButtonAndFactDisplayer = (LinearLayout) findViewById(R.id.fact_and_button_displayer);
        mFactDisplayer = (TextView) findViewById(R.id.fact_displayer);
        mQuestionNumber = (TextView) findViewById(R.id.question_num);
        mNumberOfQuestions = 0;
        mCorrect = (TextView) findViewById(R.id.correct_display);
        mIncorrect = (TextView) findViewById(R.id.incorrect_display);
        mIncorrect.setVisibility(View.GONE);
        mCorrect.setVisibility(View.GONE);
        mGoButton   = (Button)   findViewById(R.id.go_button);
        mPointsOnTrueDisplay = (TextView) findViewById(R.id.points_on_true);
        mScoreDisplay  = (TextView) findViewById(R.id.score_displayer);
        mScoreToGambleDisplay = (TextView) findViewById(R.id.score_to_gamble);
        mGamblingBar   = (SeekBar)  findViewById(R.id.seekBar);
        mGamblingBar.setProgress(50);
        mPointsOnFalse = mScore/2;
        mPointsOnTrue = mScore/2;
        mPointsOnTrueDisplay.setText("True: " + mPointsOnTrue);
        mScoreToGambleDisplay.setText("False: " + mPointsOnFalse);
        mMissedQuestions = 0;
        mGamblingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=50)
                {
                    mPointsOnTrue = (int) Math.ceil((progress * mScore) / 100);

                }
                else
                {
                    mPointsOnTrue = (int) Math.floor((progress * mScore) / 100);

                }
                mPointsOnTrue = (int) Math.round((progress * mScore) / 100);
                mPointsOnFalse = mScore - mPointsOnTrue;
                mPointsOnTrueDisplay.setText("True: " + mPointsOnTrue);
                mScoreToGambleDisplay.setText("False: " + mPointsOnFalse);
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
    private void setFact()
    {
        if (mNumberOfQuestions < MAX_QUESTIONS)
        {
            mFactDisplayer.setText(mHashMapTools.getRandomItem());
            mNumberOfQuestions++;
            mQuestionNumber.setText("Question " + mNumberOfQuestions);
        }

    }

    private void onCorrect()
    {
        mAnswerTracker[mNumberOfQuestions - 1] = 1;
        mCorrect.setVisibility(View.VISIBLE);
        calculateNewScore();
    }

    private void onIncorrect()
    {
        mAnswerTracker[mNumberOfQuestions - 1] = 0;
        mIncorrect.setVisibility(View.VISIBLE);
        calculateNewScore();
    }


    private void createGameOver(boolean win)
    {
        //Save the score if it's a new hiscore
        if (mScore > FileTools.getScore(mTheme))
        {
            FileTools.writeData(mTheme, mScore);
        }

        //Update stats
        statisticsUpdated  stats = new statisticsUpdated(this);
        stats.updateStat(statisticsUpdated.NUMBER_OF_QUESTIONS_ANSWERED, mNumberOfQuestions);
        int numberCorrect = 0;
        for (int ii = 0; ii < mAnswerTracker.length; ii ++)
        {
            if (mAnswerTracker[ii] == 1)
            {
                numberCorrect++;
            }
        }
        stats.updateStat(statisticsUpdated.NUMBER_OF_CORRECT_ANSWERS, numberCorrect);
        stats.updateStat(statisticsUpdated.NUMBER_OF_ROUNDS_COMPLETE,1);
        stats.updateStat(statisticsUpdated.TOTAL_SCORE,mScore);
        stats.updateStat(statisticsUpdated.NUMBER_OF_INCORRECT_ANSWERS, mNumberOfQuestions - numberCorrect - mMissedQuestions);
        stats.updateStat(statisticsUpdated.NUMBER_OF_MISSED_QUESTIONS, mMissedQuestions);

        //Create an intent for the summary page
        Intent GameOver = new Intent(QuestionDisplayer.this, GameOver.class);
        GameOver.putExtra("win", win);
        GameOver.putExtra("score", mScore);
        GameOver.putExtra("numQuestions", mNumberOfQuestions);
        GameOver.putExtra("pointTracker", mPointsTracker);
        GameOver.putExtra("answerTracker", mAnswerTracker);
        GameOver.putExtra("missedTracker", mMissedQuestions);
        Gson gson = new Gson();
        String list = gson.toJson( mHashMapTools.getAskedQuestion());
        GameOver.putExtra("factsList",list);
        mCountdownTimerRound.cancel();
        mCountdownTimerWaiting.cancel();
        startActivity(GameOver);
        finish();

    }

    private void checkIfFinished()
    {
        if (mScore == 0)
        {
            createGameOver(false);
        }
        else if (mNumberOfQuestions >= MAX_QUESTIONS)
        {
            createGameOver(true);
        }
    }

    /**
     * Sets the on click listeners for the true and false buttons
     */
    private void setUpButtons() {
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoPressed();
            }
        });

        setQuitButton();

    }

    /**
     * Updates the score display and the seek bar
     */
    private void setScoreDisplays()
    {
        mScoreDisplay.setText("Score = " + mScore);
    }

    /**
     * Calculates the new score once an answer is given
     */
    private void calculateNewScore()
    {
        int lostPoints = 0;
        if (mHashMapTools.getTrueOrFalse())
        {
            lostPoints = mPointsOnFalse;
            mScore = mScore - mPointsOnFalse;
        }
        else
        {
            lostPoints = mPointsOnTrue;
            mScore = mScore - mPointsOnTrue;
        }
        if (lostPoints == 0)
        {
            mScoreDisplay.setText("Perfect!");

        }
        else
        {
            mScoreDisplay.setText("-" + lostPoints);

        }
        mCountdownTimerWaiting.start();

    }

    private void setQuitButton() {
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit();

            }
        });
    }

    private void quit()
    {
        mCountdownTimerWaiting.cancel();
        mCountdownTimerRound.cancel();
        Intent start = new Intent(QuestionDisplayer.this, MainScreen.class);
        startActivity(start);
        finish();
    }

    private void onGoPressed()
    {
        boolean correctAnswer = mHashMapTools.getTrueOrFalse();
        mCountdownTimerRound.cancel();
        if (mPointsOnTrue >= mScore/2)
        {
            //Player thought true
            if(correctAnswer)
            {
                onCorrect();
            }
            else
            {
                onIncorrect();
            }
        }
        else
        {
            //Player thought false
            if(correctAnswer)
            {
                onIncorrect();
            }
            else
            {
                onCorrect();
            }
        }
        mButtonAndFactDisplayer.setVisibility(View.GONE);

        mGamblingBar.setProgress(50);
        checkIfFinished();
    }


    private void setUpCountdownTimers() {
        mCountdownTimerRound = new CountDownTimer(6000, 100) {

            public void onTick(long millisUntilFinished) {
                mCountdownText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mMissedQuestions = mMissedQuestions + 1;
                mIncorrect.setVisibility(View.VISIBLE);
                mButtonAndFactDisplayer.setVisibility(View.GONE);
                mAnswerTracker[mNumberOfQuestions - 1] = 2;

                onGoPressed();
                checkIfFinished();
            }
        };

        mCountdownTimerWaiting = new CountDownTimer(MS_WHILE_WAITING,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mScoreDisplay.setTextSize((int) ((MS_WHILE_WAITING - millisUntilFinished)/70));

            }

            @Override
            public void onFinish() {
                setScoreDisplays();

                setFact();
                mIncorrect.setVisibility(View.GONE);
                mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                if (mScore > 0 && mNumberOfQuestions < MAX_QUESTIONS) {
                    mCountdownTimerRound.start();

                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        quit();
    }
}
