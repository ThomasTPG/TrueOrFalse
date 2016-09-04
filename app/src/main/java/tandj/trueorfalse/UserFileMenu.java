package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thomas on 04/09/2016.
 */
public class UserFileMenu extends Activity {

    private ArrayList<String> listOfFacts;

    private UserFactTools userFacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fact_list);
        userFacts = new UserFactTools(this);
        listOfFacts = userFacts.getListOfFiles();
        System.out.println(listOfFacts);

        fillLinearLayout();

    }

    private void fillLinearLayout()
    {
        final LinearLayout ll = (LinearLayout) findViewById(R.id.list_of_user_facts);
        for (int ii = 0; ii < listOfFacts.size(); ii++)
        {
            final int index = ii;

            final LinearLayout factLine = new LinearLayout(this);
            factLine.setWeightSum(10);
            factLine.setOrientation(LinearLayout.HORIZONTAL);

            final AutoResizeTextView fileName = new AutoResizeTextView(this);
            fileName.setText(listOfFacts.get(ii));
            fileName.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6f));
            factLine.addView(fileName);

            final Button edit = new Button(this);
            edit.setText("Edit");
            edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editFile = new Intent(UserFileMenu.this, EditUserFiles.class);
                    editFile.putExtra("file name", listOfFacts.get(index));
                    startActivity(editFile);
                    finish();
                }
            });
            factLine.addView(edit);

            ImageButton delete = new ImageButton(this);
            delete.setImageDrawable(getResources().getDrawable(R.drawable.tick));
            delete.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userFacts.deleteFile(listOfFacts.get(index));
                    ll.removeView(factLine);
                }
            });
            factLine.addView(delete);

            ll.addView(factLine);
        }

        Button add = new Button(this);
        add.setText("Add");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editFile = new Intent(UserFileMenu.this, EditUserFiles.class);
                editFile.putExtra("file name", "new");
                startActivity(editFile);
                finish();
            }
        });
        ll.addView(add);
    }
}
