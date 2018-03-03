package labs.sdm.game.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class ScoresActivity extends AppCompatActivity {

    public ArrayList<HashMap<String,String>> localScores = new ArrayList<>();
    public ArrayList<HashMap<String,String>> firendsScores = new ArrayList<>();

    public SimpleAdapter localAdapter;

    public TabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        host = (TabHost) findViewById(R.id.tabHost_Scores);
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

         localAdapter = new SimpleAdapter(this, localScores, R.layout.score_list_row,
                new String[]{"player","score"}, new int[]{R.id.textName, R.id.textScore});

        localTableScores.setAdapter(localAdapter);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(host.getCurrentTab() == 0){
            getMenuInflater().inflate(R.menu.menu_local_scores, menu);
        }

        return true;
    }

    private void addLocalScore(Score score){
        localScores.add(getScoreHashMap(score));
        localAdapter.notifyDataSetChanged();
    }

    private HashMap<String, String> getScoreHashMap(Score score) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("player", score.getName());
        item.put("score", String.valueOf(score.getScore()));
        return item;
    }

    private void getFriendsScores() {

    }

    private void getLocalScores() {
        new LocalScoresAsyncTask().execute();
    }


    private class LocalScoresAsyncTask extends AsyncTask<Void, Score, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            List<Score> scores = GameDatabase.getGameDatabase(ScoresActivity.this).scoreDAO().getOrderedScores();

            for (Score score : scores)
            {
                publishProgress(score);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Score... score){
            addLocalScore(score[0]);
        }
    }
}
