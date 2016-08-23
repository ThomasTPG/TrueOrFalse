package tandj.trueorfalse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private TextView mCongrats;
    private TextView mUnlucky;
    private TextView mScore;
    private TextView mNumberofQuestions;




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

        setUpDisplay();
        WinOrLose(win);
        showScore(score);
        showNumQuestions(numQuestions);
    }

    private void setUpDisplay() {
        mCongrats = (TextView) findViewById(R.id.congrats);
        mUnlucky = (TextView) findViewById(R.id.unlucky);
        mScore = (TextView) findViewById(R.id.scoretext);
        mNumberofQuestions = (TextView) findViewById(R.id.numquestionstext);



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
}
