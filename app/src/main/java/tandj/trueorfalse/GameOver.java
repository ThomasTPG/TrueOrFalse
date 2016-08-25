package tandj.trueorfalse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameOver extends AppCompatActivity {

    private TextView mResults;
    private TextView mScore;
    private TextView mNumberofQuestions;
    private LinearLayout mFactDisplay;
    private Button   mGoHome;
    private Button   mPlayAgain;
    private Bundle results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game_over);

        results = getIntent().getExtras();

        setUpDisplay();
        setUpContent();
    }

    private void setUpDisplay() {
        mResults = (TextView) findViewById(R.id.result_text);
        mScore = (TextView) findViewById(R.id.scoretext);
        mNumberofQuestions = (TextView) findViewById(R.id.numquestionstext);
        mFactDisplay = (LinearLayout) findViewById(R.id.fact_linear_layout);
    }

    private void setUpContent() {
        if (results.getBoolean("win"))
        {
            mResults.setText("Congrats! You win!");
        }
        else
        {
            mResults.setText("You lose!");
        }
        mScore.setText("Score: " + results.getInt("score"));

        int questionsAnswered = results.getInt("numQuestions") - results.getInt("missedTracker");
        mNumberofQuestions.setText("Number of questions answered: " + questionsAnswered);

        setScrollView();

        mGoHome = (Button) findViewById(R.id.goHome);
        mPlayAgain = (Button) findViewById(R.id.playAgain);

        setPlayAgainButton();
        setGoHomeButton();
    }

    private void setPlayAgainButton() {
        mPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(GameOver.this, ThemeSelect.class);
                startActivity(start);
            }
        });
    }

    private void setGoHomeButton() {
        mGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(GameOver.this, MainScreen.class);
                startActivity(start);
            }
        });
    }

    private void setScrollView()
    {
        String str = (String) results.get("factsList");
        Gson gson = new Gson();
        Type entityType = new TypeToken< LinkedHashMap<String, Boolean>>(){}.getType();
        LinkedHashMap<String, Boolean> questionsAsked = gson.fromJson(str, entityType);
        int[] answers = results.getIntArray("answerTracker");
        int index = 0;
        Iterator factIterator = questionsAsked.entrySet().iterator();
        while (factIterator.hasNext())
        {
            Map.Entry factDetails = (Map.Entry) factIterator.next();
            String fact = (String) factDetails.getKey();
            String correctAnswer = Boolean.toString((Boolean) factDetails.getValue());
            String givenAnswer = "";
            switch (answers[index])
            {
                case (2):
                    givenAnswer = "None";
                    break;
                case (1):
                    givenAnswer = "True";
                    break;
                case (0):
                    givenAnswer = "False";
                    break;
            }
            TextView display = new TextView(this);
            display.setText(fact + "\nYour answer: " + givenAnswer + "\nCorrect answer: " + correctAnswer);
            mFactDisplay.addView(display);
            index ++;
        }

        questionsAsked.size();
    }



}
