package tandj.trueorfalse;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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

    //Used to implement pause
    private Handler mHandler = new Handler();

    //Text views that display the correct & incorrect messages
    private TextView mCorrect;
    private TextView mIncorrect;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHashMapTools = new HashMapTools(FactFiles.MATHS_FACTS, this);

        setUpDisplay();

        setFact();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Sets up the display for the user
     */
    private void setUpDisplay() {
        setContentView(R.layout.question_displayer_layout);

        mFactDisplayer = (TextView) findViewById(R.id.fact_displayer);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);

        mCorrect = (TextView) findViewById(R.id.correct_display);
        mIncorrect = (TextView) findViewById(R.id.incorrect_display);


        setUpButtons();
        setUpCorrectDisplay();
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
        setUpCorrectDisplay();
        if (answer == mHashMapTools.getTrueOrFalse()) {
            //Answer is correct, set a new question
            mCorrect.setVisibility(View.VISIBLE);
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    setFact();
                    setUpCorrectDisplay();
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                }
            }, 3000);

        } else {
            //Answer is wrong
            mIncorrect.setVisibility(View.VISIBLE);
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

    private void setUpCorrectDisplay() {
        mCorrect.setVisibility(View.GONE);
        mIncorrect.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "QuestionDisplayer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tandj.trueorfalse/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "QuestionDisplayer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tandj.trueorfalse/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
