package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TwoPlayer extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        setUpContent();
    }

    private void setUpContent()
    {
        setHomeButton();
        setSplitScreenButton();
        setTimeTrialButton();
    }

    private void setHomeButton() {
        ImageButton home = (ImageButton) findViewById(R.id.go_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(TwoPlayer.this, MainScreen.class);
                startActivity(start);
                finish();
            }
        });
    }

    private void setSplitScreenButton() {
        final ImageButton splitScreen = (ImageButton) findViewById(R.id.split_screen_button);
        splitScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(TwoPlayer.this, TwoPlayerThemeSelect.class);
                start.putExtra("Mode", "Split");
                startActivity(start);
                finish();
            }
        });
    }

    private void setTimeTrialButton() {
        ImageButton splitScreen = (ImageButton) findViewById(R.id.time_trial_button);
        splitScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(TwoPlayer.this, TwoPlayerThemeSelect.class);
                start.putExtra("Mode", "Time Trial");
                startActivity(start);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(TwoPlayer.this, MainScreen.class);
        startActivity(back);
        finish();
    }
}
