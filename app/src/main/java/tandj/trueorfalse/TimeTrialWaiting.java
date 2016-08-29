package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Thomas on 28/08/2016.
 */
public class TimeTrialWaiting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_trial_waiting);
        Intent input = getIntent();
        final Bundle b = input.getExtras();
        final String nextPlayer = b.getString("Next Player");
        final String[] questions = getIntent().getStringArrayExtra("Questions");


        TextView playerDisplay = (TextView) findViewById(R.id.player_number_display);
        playerDisplay.setText(nextPlayer + ", press the start button to begin.");

        final Button start = (Button) findViewById(R.id.start_time_trial_button);
        start.setText("Start");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(TimeTrialWaiting.this, TimeTrial.class);
                startGame.putExtra("Current Player", nextPlayer);
                startGame.putExtra("Questions", questions);
                if (!(nextPlayer.equals("Player 1")))
                {
                    startGame.putExtra("Player 1 Score", b.getInt("Player 1 Score"));
                }
                startActivity(startGame);
                finish();
            }
        });
    }
}
