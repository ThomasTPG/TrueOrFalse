package tandj.trueorfalse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ThemeSelect extends AppCompatActivity {

    private Spinner mThemeSpinner;
    private Button  mGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_select);
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
        setUpDisplay();
        addListenerOnButton();
    }

    private void setUpDisplay() {
        mThemeSpinner = (Spinner) findViewById(R.id.theme_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mThemeSpinner.setAdapter(adapter);

        mGoButton = (Button) findViewById(R.id.go);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();
    }

    //add spinner data
    public void addListenerOnSpinnerItemSelection() {
        mThemeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    //get selected spinner value
    public void addListenerOnButton() {
        mGoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String theme = String.valueOf(mThemeSpinner.getSelectedItem());
                //Toast.makeText(ThemeSelect.this,"theme=" + theme,Toast.LENGTH_LONG).show();
                Intent Start = new Intent(ThemeSelect.this, QuestionDisplayer.class);
                Start.putExtra("theme", String.valueOf(mThemeSpinner.getSelectedItem()));
                if (theme.equals("Maths Facts") || theme.equals("Animal Facts")) {
                    startActivity(Start);
                }
            }

        });
    }
}
