package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Thomas on 17/08/2016.
 * Class which is displayed on start up
 */
public class MainScreen extends Activity{

    /**
     * Button which starts the true or false game
     */
    private Button mStartButton;

    /**
     * Method called when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpContent();
    }

    /**
     * Sets up the display
     */
    private void setUpContent()
    {
        setContentView(R.layout.main_screen_layout);

        mStartButton = (Button) findViewById(R.id.start);

        setStartButton();
    }

    /**
     * Set the action for when the start button is clicked
     */
    public void setStartButton()
    {
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(MainScreen.this, QuestionDisplayer.class);
                startActivity(start);
            }
        });
    }
}