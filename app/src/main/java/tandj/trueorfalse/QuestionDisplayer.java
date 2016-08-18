package tandj.trueorfalse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Thomas on 17/08/2016.
 * Class which displays the questions and allows the user to answer
 */
public class QuestionDisplayer extends Activity{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHashMapTools = new HashMapTools(FactFiles.CUTE_ANIMAL_FACTS, this);

        setUpDisplay();

        setFact();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay()
    {
        setContentView(R.layout.question_displayer_layout);

        mFactDisplayer = (TextView) findViewById(R.id.fact_displayer);
        mTrueButton    = (Button)   findViewById(R.id.true_button);
        mFalseButton   = (Button)   findViewById(R.id.false_button);

        setUpButtons();
    }

    /**
     * Fills the text view with a random fact from the hashmap
     */
    private void setFact()
    {
        mFactDisplayer.setText(mHashMapTools.getRandomItem());
    }

    /**
     * Method which deals with the result of a true or false button being pressed
     * @param answer The button that has been pressed
     */
    private void onButtonClicked(Boolean answer)
    {
        if (answer == mHashMapTools.getTrueOrFalse())
        {
            //Answer is correct, set a new question
            setFact();
        }
        else
        {
            //Answer is wrong
        }
    }

    /**
     * Sets the on click listeners for the true and false buttons
     */
    private void setUpButtons()
    {
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
}
