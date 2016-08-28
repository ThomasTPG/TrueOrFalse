package tandj.trueorfalse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplitScreen extends AppCompatActivity {

    private Button mTrueButton1;
    private Button mFalseButton1;
    private Button mTrueButton2;
    private Button mFalseButton2;
    private TextView mFactDisplayer1;
    private TextView mFactDisplayer2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_screen);

        setUpDisplay();
    }

    private void setUpDisplay() {
        setHomeButton();
        mTrueButton1 = (Button) findViewById(R.id.true_1);
        mFalseButton1 = (Button) findViewById(R.id.false_1);
        mTrueButton2 = (Button) findViewById(R.id.true_2);
        mFalseButton2 = (Button) findViewById(R.id.false_2);
        mFactDisplayer1 = (TextView) findViewById(R.id.fact_displayer_1);
        mFactDisplayer2 = (TextView) findViewById(R.id.fact_displayer_2);


    }

    private void setHomeButton()
    {
        Button home = (Button) findViewById(R.id.split_home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(SplitScreen.this, MainScreen.class);
                startActivity(start);
                finish();
            }
        });
    }
}
