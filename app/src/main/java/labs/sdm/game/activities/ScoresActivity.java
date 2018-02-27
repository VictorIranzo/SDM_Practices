package labs.sdm.game.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import labs.sdm.game.R;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        TabHost host = (TabHost) findViewById(R.id.tabHost_Scores);
        host.setup();

        TabSpec spec = host.newTabSpec(getResources().getString(R.string.tab_friends));
        spec.setIndicator(getResources().getString(R.string.tab_friends));
        spec.setContent(R.id.la);
        host.addTab(spec);
    }
}
