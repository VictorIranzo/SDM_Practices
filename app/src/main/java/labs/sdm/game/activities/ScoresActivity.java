package labs.sdm.game.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.util.ArrayList;
import java.util.HashMap;

import labs.sdm.game.R;

public class ScoresActivity extends AppCompatActivity {

    public ArrayList<HashMap<String,String>> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        TabHost host = (TabHost) findViewById(R.id.tabHost_Scores);
        host.setup();

        TabSpec spec1 = host.newTabSpec(getResources().getString(R.string.tab_local));
        spec1.setIndicator(getResources().getString(R.string.tab_local));
        spec1.setContent(R.id.tab1);
        host.addTab(spec1);

        TabSpec spec2 = host.newTabSpec(getResources().getString(R.string.tab_friends));
        spec2.setIndicator(getResources().getString(R.string.tab_friends));
        spec2.setContent(R.id.tab2);
        host.addTab(spec2);

        host.setCurrentTab(0);

        ListView localTableScores = findViewById(R.id.list1);

        this.getFriendsScores();
        this.getLocalScores();

        SimpleAdapter adapter = new SimpleAdapter(this, scores, R.layout.score_list_row,
                new String[]{"player","score"}, new int[]{R.id.textName, R.id.textScore});

        localTableScores.setAdapter(adapter);
    }

    private void addScore(String name, int points){
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("player", name);
        item.put("score", String.valueOf(points));
        scores.add(item);
    }

    private void getFriendsScores() {
        addScore("Paco",20);
        addScore("Vicente",1000);
    }

    private void getLocalScores() {

    }

}
