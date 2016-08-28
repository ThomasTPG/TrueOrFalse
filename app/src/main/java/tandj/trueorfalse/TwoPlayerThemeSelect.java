package tandj.trueorfalse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Thomas on 28/08/2016.
 */
public class TwoPlayerThemeSelect extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_theme_select);

        createBackButton();
    }

    private void createBackButton()
    {
        Button back = (Button) findViewById(R.id.back_button_theme_select);
        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
