package tandj.trueorfalse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Thomas on 26/08/2016.
 */
public class StatisticDisplay extends Activity {

    statisticsUpdated mStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStats = new statisticsUpdated(this);
        setContentView(R.layout.statistic_view);
        addStats();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
