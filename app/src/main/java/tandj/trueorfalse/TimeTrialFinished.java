package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Thomas on 28/08/2016.
 */
public class TimeTrialFinished extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_trial_finished);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        int p1Score = b.getInt("Player 1 Score");
        int p2Score = b.getInt("Player 2 Score");
        String winner;
        if (p1Score > p2Score)
        {
            winner = "Player 1";
        }
        else
        {
            winner = "Player 2";
        }

        Button themeSelect = (Button) findViewById(R.id.theme_select);
        themeSelect.setText("Back");
        themeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(TimeTrialFinished.this, TwoPlayerThemeSelect.class);
                startActivity(back);
                finish();
            }
        });

        TextView result = (TextView) findViewById(R.id.winner);
        result.setText(winner + " is the winner!");

        TextView scores = (TextView) findViewById(R.id.scores);
        scores.setText("Player 1 scored: " + p1Score + " and Player 2 scored : " + p2Score);

        statisticsUpdated  stats = new statisticsUpdated(this);
        stats.updateStat(statisticsUpdated.NUMBER_OF_TIME_TRIAL_GAMES,1);
    }
}
