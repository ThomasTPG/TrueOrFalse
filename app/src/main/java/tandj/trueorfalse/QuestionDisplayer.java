package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

    //Text view showing question number
    private TextView mQuestionNumber;

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

    //array to track the number of points after each round
    private int[] mPointsTracker;

    //array to track the questions asked and if the user was correct
    private int[] mAnswerTracker;

    private Button mQuitButton;


    private TextView mCountdownText;

    private CountDownTimer mCountdownTimer;

    private String mTheme;

    private int mMissedQuestions;

    private ProgressBar mTimerProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        mTheme = b.getString("theme");
//        mTheme = "cambridge_facts.txt";
        mHashMapTools = new HashMapTools(mTheme, this);


        mScore = getResources().getInteger(R.integer.starting_score);
        MAX_QUESTIONS = getResources().getInteger(R.integer.number_of_questions_per_round);
        mPointsTracker = new int[MAX_QUESTIONS];
        mAnswerTracker = new int[MAX_QUESTIONS];

        setUpDisplay();

        setUpCountdownTimer();
        mCountdownTimer.start();

        setFact();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay() {
        setContentView(R.layout.question_displayer_layout);

        mCountdownText = (TextView) findViewById(R.id.countdown_timer);
        mQuitButton = (Button) findViewById(R.id.quit);
        mButtonAndFactDisplayer = (LinearLayout) findViewById(R.id.fact_and_button_displayer);
        mFactDisplayer = (TextView) findViewById(R.id.fact_displayer);
        mQuestionNumber = (TextView) findViewById(R.id.question_num);
        mNumberOfQuestions = 0;
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
        mMissedQuestions = 0;
        mTimerProgress = (ProgressBar) findViewById(R.id.timer_progress);
        mGamblingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPointsToGamble = (int) ((progress * (mScore - 1) / 100) + 1);
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
    private void setFact()
    {
        if (mNumberOfQuestions < MAX_QUESTIONS)
        {
            mFactDisplayer.setText(mHashMapTools.getRandomItem());
            mNumberOfQuestions++;
            mQuestionNumber.setText("Question " + mNumberOfQuestions);
        }

    }

    /**
     * Method which deals with the result of a true or false button being pressed
     *
     * @param answer The button that has been pressed
     */
    private void onButtonClicked(Boolean answer) {
        mCountdownTimer.cancel();
        mCountdownText.setText("Time Up!");
        if (answer == mHashMapTools.getTrueOrFalse()) {
            //Answer is correct, set a new question
            mAnswerTracker[mNumberOfQuestions - 1] = 1;

            mCorrect.setVisibility(View.VISIBLE);
            mButtonAndFactDisplayer.setVisibility(View.GONE);

            mHandler.postDelayed(new Runnable() {
                public void run() {
                    setFact();
                    mCorrect.setVisibility(View.GONE);
                    mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                    if (mScore > 0 && mNumberOfQuestions <= MAX_QUESTIONS) {
                        mCountdownTimer.start();
                    }
                }
            }, 2000);
            calculateNewScore(true);

        } else {
            //Answer is wrong
            mAnswerTracker[mNumberOfQuestions - 1] = 0;
            mIncorrect.setVisibility(View.VISIBLE);
            mButtonAndFactDisplayer.setVisibility(View.GONE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFact();
                    mIncorrect.setVisibility(View.GONE);
                    mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                    if (mScore > 0 && mNumberOfQuestions <= MAX_QUESTIONS) {
                        mCountdownTimer.start();
                    }
                }
            },2000);
            calculateNewScore(false);
        }
        checkIfFinished();
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
            numberCorrect = numberCorrect + mAnswerTracker[ii];
        }
        stats.updateStat(statisticsUpdated.NUMBER_OF_CORRECT_ANSWERS, numberCorrect);
        stats.updateStat(statisticsUpdated.NUMBER_OF_ROUNDS_COMPLETE,1);
        stats.updateStat(statisticsUpdated.TOTAL_SCORE,mScore);

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
        mCountdownTimer.cancel();
        startActivity(GameOver);

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

        setQuitButton();

    }

    /**
     * Updates the score display and the seek bar
     */
    private void setScoreDisplays()
    {
        mGamblingBar.setProgress(0);
        mScoreDisplay.setText("Score = " + mScore);
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
        //mPointsTracker[mNumberOfQuestions] = mScore;
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
        Intent start = new Intent(QuestionDisplayer.this, MainScreen.class);
        startActivity(start);
        finish();
    }


    private void recordPoints() {
        if (mNumberOfQuestions < MAX_QUESTIONS) {
            mPointsTracker[mNumberOfQuestions] = mScore;
        }
    }

    private void setUpCountdownTimer() {
        mCountdownTimer = new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountdownText.setText("seconds remaining: " + millisUntilFinished / 1000);
                if (millisUntilFinished < 5000)
                mTimerProgress.incrementProgressBy(1);
            }

            public void onFinish() {
                mCountdownText.setText("Time Up!");
                mTimerProgress.incrementProgressBy(1);
                mMissedQuestions = mMissedQuestions + 1;
                mIncorrect.setVisibility(View.VISIBLE);
                mButtonAndFactDisplayer.setVisibility(View.GONE);
                mAnswerTracker[mNumberOfQuestions - 1] = 2;

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        setFact();
                        mIncorrect.setVisibility(View.GONE);
                        mButtonAndFactDisplayer.setVisibility(View.VISIBLE);
                        if (mScore > 0 && mNumberOfQuestions <= MAX_QUESTIONS) {
                            mCountdownTimer.start();
                            mTimerProgress.setProgress(0);
                        }
                    }
                }, 2000);
                calculateNewScore(false);
                checkIfFinished();
            }
        };
    }

    @Override
    public void onBackPressed() {
        quit();
    }
}
