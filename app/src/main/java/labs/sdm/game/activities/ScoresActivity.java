package labs.sdm.game.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import labs.sdm.game.R;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class ScoresActivity extends AppCompatActivity {

    public ArrayList<HashMap<String,String>> localScores = new ArrayList<>();
    public ArrayList<HashMap<String,String>> firendsScores = new ArrayList<>();

    public SimpleAdapter localAdapter;

    public TabHost host;

    public AsyncTask<Void, Void, Score> selectedLocalScore;

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

        localTableScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = ((TextView)view.findViewById(R.id.textName)).getText().toString();
                int points = Integer.parseInt(((TextView)view.findViewById(R.id.textScore)).getText().toString());

                selectedLocalScore = new FindScoreByNameAndPointsAsyncTask(user, points).execute();
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_bar_delete:
                Score score = getSelectedScore();
                    new DeleteLocalScoreAsyncTask(score).execute();
                    localScores.remove(getScoreHashMap(score));
                    localAdapter.notifyDataSetChanged();

                    // It's set to null as in this way a score it's not deleted twice in
                    // different attempts. In other words, another row must be selected to delete again.
                    selectedLocalScore = null;
                }
                // TODO: Show a Toast message.
        }

        // This is required, as the Back arrow click must be handled in this method.
        return super.onOptionsItemSelected(item);
    }

    private Score getSelectedScore() {
        if(selectedLocalScore != null) { // It can be null if any row has been selected.
            try {
                return selectedLocalScore.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return null;
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
        new GetLocalScoresAsyncTask().execute();
    }


    private class GetLocalScoresAsyncTask extends AsyncTask<Void, Score, Void>{

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

    private class DeleteLocalScoreAsyncTask extends AsyncTask<Void, Void, Void>{

        // TODO: Refactor this to pass parameters using parameters in execute. https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
        private Score deletedScore;

        public DeleteLocalScoreAsyncTask(Score deletedScore) {
            this.deletedScore = deletedScore;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GameDatabase.getGameDatabase(ScoresActivity.this).scoreDAO().deleteScore(deletedScore);
            return null;
        }
    }

    private class FindScoreByNameAndPointsAsyncTask extends AsyncTask<Void, Void, Score>{

        private String user;
        private int points;

        public FindScoreByNameAndPointsAsyncTask(String user, int points) {
            this.user = user;
            this.points = points;
        }

        @Override
        protected Score doInBackground(Void... voids) {
            return GameDatabase.getGameDatabase(ScoresActivity.this).scoreDAO().findScoreByUserandScore(user,points);
        }
    }
}
