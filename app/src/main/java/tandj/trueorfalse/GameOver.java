package tandj.trueorfalse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import java.util.HashMap;

public class GameOver extends AppCompatActivity {

    private TextView mCongrats;
    private TextView mUnlucky;
    private TextView mScore;
    private TextView mNumberofQuestions;
    private TextView mFactsList;

    private Button   mGoHome;
    private Button   mPlayAgain;

    private HashMap<String, Boolean> mHashMap;
    private HashMapTools mHashMapTools;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Bundle b = getIntent().getExtras();
        boolean win = b.getBoolean("win");
        int score = b.getInt("score");
        int numQuestions = b.getInt("numQuestions");
        int[] pointsTracker = b.getIntArray("pointsTracker");
        String factsList = b.getString("factsList");

        mHashMapTools = new HashMapTools(factsList, this);
        String test  = mHashMapTools.getRandomItem();
        Toast.makeText(GameOver.this,"test=" + test,Toast.LENGTH_LONG).show();

        setUpDisplay();
        setUpContent();
        WinOrLose(win);
        showScore(score);
        showNumQuestions(numQuestions);
        if (factsList != null) {
            mFactsList.setText(factsList);
        }

    }

    private void setUpDisplay() {
        mCongrats = (TextView) findViewById(R.id.congrats);
        mUnlucky = (TextView) findViewById(R.id.unlucky);
        mScore = (TextView) findViewById(R.id.scoretext);
        mNumberofQuestions = (TextView) findViewById(R.id.numquestionstext);
        mFactsList = (TextView) findViewById(R.id.summaryQuestions);
    }

    private void setUpContent() {
        mGoHome = (Button) findViewById(R.id.goHome);
        mPlayAgain = (Button) findViewById(R.id.playAgain);

        setPlayAgainButton();
        setGoHomeButton();
    }

    private void WinOrLose(boolean win) {
        if (win) {
            mUnlucky.setVisibility(View.GONE);
        }
        else {
            mCongrats.setVisibility(View.GONE);
        }
    }

    private void showScore(int score) {
        mScore.setText("Score: " + score);
    }

    private void showNumQuestions(int num) {
        mNumberofQuestions.setText("Number of Questions: " + num);
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


}
