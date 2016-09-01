package tandj.trueorfalse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Thomas on 26/08/2016.
 */
public class StatisticDisplay extends Activity {

    statisticsUpdated mStats;

    private int mNumAchievements;
    private LinearLayout mAchievementLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStats = new statisticsUpdated(this);
        setContentView(R.layout.statistic_view);

        Button menu = (Button) findViewById(R.id.menu_button);
        menu.setText("Menu");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(StatisticDisplay.this, MainScreen.class);
                startActivity(start);
                finish();
            }
        });

        addStats();
        mNumAchievements = 5;
        addAchievements();
    }

    private void addStats()
    {
        LinearLayout linearLay = (LinearLayout) findViewById(R.id.stats);
        for (int ii = 0; ii < statisticsUpdated.statisticArray.length; ii ++)
        {
            TextView stat = new TextView(this);
            stat.setText(statisticsUpdated.statisticArray[ii] + ": " + mStats.getStat(ii));
            linearLay.addView(stat);
        }

        TextView correct = new TextView(this);
        correct.setText("Correct answers per round: " + mStats.getStat(1)/mStats.getStat(2));
        linearLay.addView(correct);

        TextView incorrect = new TextView(this);
        incorrect.setText("Incorrect answers per round: " + mStats.getStat(4)/mStats.getStat(2));
        linearLay.addView(incorrect);


    }

    private void addAchievements() {
        mAchievementLayout = (LinearLayout) findViewById(R.id.achievements);

        addSingleAchievement("Number of\nGames Played", getAchievementColour(mStats.getStat(2), 10,50,200));
        addSingleAchievement("Total Score", getAchievementColour(mStats.getStat(3), 20000,50000,200000));
        addSingleAchievement("Number of\nCorrect Answers", getAchievementColour(mStats.getStat(1), 100,500,2000));
        addSingleAchievement("Number of Split Screen games played", getAchievementColour(0, 10, 50, 100));
        addSingleAchievement("Number of Time Trial Games played", getAchievementColour(0, 10,50,100));
        addSingleAchievement("Difficulties Unlocked", getAchievementColour(1, 1, 2, 3));
    }

    private void addSingleAchievement(String text, String colour) {
        LinearLayout linearLayout = new LinearLayout(this);
        mAchievementLayout.addView(linearLayout);
        ImageView medal = new ImageView(this);
        medal.setImageResource(R.drawable.medal1);
        setAchievementColour(colour, medal);
        LinearLayout.LayoutParams layoutParamsMedal = new LinearLayout.LayoutParams(200, 200);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(400, 200, LinearLayout.LayoutParams.WRAP_CONTENT);
        medal.setLayoutParams(layoutParamsMedal);
        linearLayout.addView(medal);
        TextView stat = new TextView(this);
        stat.setText(text);
        stat.setLayoutParams(layoutParamsText);
        stat.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        stat.setGravity(Gravity.CENTER);
        linearLayout.addView(stat);
    }

    private void setAchievementColour(String color, ImageView medal) {
        if (color.equals("gold")){
            medal.setBackgroundColor(Color.parseColor("#FFD700"));
        }
        if (color.equals("silver")){
            medal.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }
        if (color.equals("bronze")){
            medal.setBackgroundColor(Color.parseColor("#cc6633"));
        }
        if (color.equals("black")){
            medal.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    private String getAchievementColour(int currentVal, int bronzeVal, int silverVal, int goldVal) {
        if (currentVal < bronzeVal) {
            return "black";
        } else if (currentVal < silverVal) {
            return "bronze";

        } else if (currentVal < goldVal) {
            return "silver";
        } else {
            return "gold";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
