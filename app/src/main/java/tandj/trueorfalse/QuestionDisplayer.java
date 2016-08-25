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
import android.widget.Toast;

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
    private int[][] mQuestionTracker;

    //private String[] mFactsList = new String[10];
    private String mFactsList;

    //was previous answer correct?
    private Boolean mWasCorrect;

    private Button mQuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String theme = b.getString("theme");
        Toast.makeText(QuestionDisplayer.this,
                "On Button Click : " +
                        "\n" + theme,
                Toast.LENGTH_LONG).show();
        if (theme.equals("Maths Facts")) {mHashMapTools = new HashMapTools(FactFiles.MATHS_FACTS, this);}
        if (theme.equals("Animal Facts")) {mHashMapTools = new HashMapTools(FactFiles.CUTE_ANIMAL_FACTS, this);}

        mScore = getResources().getInteger(R.integer.starting_score);
        MAX_QUESTIONS = getResources().getInteger(R.integer.number_of_questions_per_round);
        mPointsTracker = new int[MAX_QUESTIONS];

        setUpDisplay();

        setFact();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay() {
        setContentView(R.layout.question_displayer_layout);

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

        mFactsList = "";

        setUpButtons();
        setScoreDisplays();
    }

    /**
     * Fills the text view with a random fact from the hashmap
     */
    private void setFact() {

        recordPoints();
        mFactDisplayer.setText(mHashMapTools.getRandomItem());
        mNumberOfQuestions++;
        mQuestionNumber.setText("Question " + mNumberOfQuestions);
    }

    /**
     * Method which deals with the result of a true or false button being pressed
     *
     * @param answer The button that has been pressed
     */
    private void onButtonClicked(Boolean answer) {
        if (answer == mHashMapTools.getTrueOrFalse()) {
            //Answer is correct, set a new question

            mWasCorrect = true;
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
            mWasCorrect = false;
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
        mFactsList = mFactsList.concat(mHashMapTools.recordFact(mWasCorrect));
        String answerString = String.valueOf(answer);
        mFactsList = mFactsList.concat("#" + answerString + "\n");
        Intent GameOver = new Intent(QuestionDisplayer.this, GameOver.class);
        GameOver.putExtra("score",currentScore());
        GameOver.putExtra("numQuestions", mNumberOfQuestions);
        GameOver.putExtra("pointTracker", mPointsTracker);
        GameOver.putExtra("factsList", mFactsList);
        if (currentScore() == 0)
        {
            GameOver.putExtra("win",false);
            startActivity(GameOver);
        }
        if (mNumberOfQuestions >= MAX_QUESTIONS) {
            GameOver.putExtra("win",true);
            startActivity(GameOver);
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
        //mPointsTracker[mNumberOfQuestions] = mScore;
        FileTools.writeData(FactFileNames.fileNames[FactFileNames.MATHS_FACTS], mScore);
    }

    private int currentScore(){
        return mScore;
    }

    private void setQuitButton() {
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(QuestionDisplayer.this, MainScreen.class);
                startActivity(start);
            }
        });
    }

    private void recordPoints() {
        mPointsTracker[mNumberOfQuestions] = mScore;
    }
}
