package labs.sdm.game.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import labs.sdm.game.pojo.HighScore;
import labs.sdm.game.pojo.Score;
import labs.sdm.game.services.GetFriendHighScoresService;
import labs.sdm.game.tasks.DeleteLocalScoreAsyncTask;
import labs.sdm.game.tasks.FindScoreByNameAndPointsAsyncTask;
import labs.sdm.game.tasks.GetLocalScoresAsyncTask;

public class ScoresActivity extends AppCompatActivity {

    // The type of the ArrayLists is this one as is the one required by the SimpleAdapter.
    public ArrayList<HashMap<String,String>> localScores = new ArrayList<>();
    public ArrayList<HashMap<String,String>> friendsScores = new ArrayList<>();

    public SimpleAdapter localAdapter;
    public SimpleAdapter friendsAdapter;

    public TabHost host;

    public AsyncTask<Void, Void, Score> selectedLocalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Sets up the tab layout, setting the current tab to the Local one.
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

        // Creates a SimpleAdapter for the Local tab list of scores.
        ListView localTableScores = findViewById(R.id.list1);

         localAdapter = new SimpleAdapter(this, localScores, R.layout.score_list_row,
                new String[]{"player","score"}, new int[]{R.id.textName, R.id.textScore});

        localTableScores.setAdapter(localAdapter);

        // Updates the Local Score selected.
        localTableScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Gets the text user and score of the item selected.
                String user = ((TextView)view.findViewById(R.id.textName)).getText().toString();
                int points = Integer.parseInt(((TextView)view.findViewById(R.id.textScore)).getText().toString());

                // This object is an aysnc task and when it's required, it's result is get.
                // A call to the DB to get the Score object is needed as for deleting it we need
                // that the Score id matches to the one in the database, and here we don't know it.
                selectedLocalScore = new FindScoreByNameAndPointsAsyncTask(ScoresActivity.this,user, points).execute();
            }
        });

        // Creates a SimpleAdapter for the Friends tab list of scores.
        ListView friendsTableScores = findViewById(R.id.list2);

        friendsAdapter = new SimpleAdapter(this, friendsScores, R.layout.score_list_row,
                new String[]{"player","score"}, new int[]{R.id.textName, R.id.textScore});

        friendsTableScores.setAdapter(friendsAdapter);

        // Ivalidates the Options in the menu action bar when we change between tabs. This is done
        // because the Friends tab doesn't allow Deleting a score. So every time we change the current
        // tab, the onCreateOptionsMenu is called.
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                invalidateOptionsMenu();
            }
        });

        // Calls both async tasks to get the list of scores.
        this.getFriendsScores();
        this.getLocalScores();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Only adds the Delete score option for the Local tab.
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
                if(score != null) {
                    // Deletes the score from DB.
                    new DeleteLocalScoreAsyncTask(this,score).execute();

                    // Deletes the score from the list of scores and udpates the view.
                    localScores.remove(getScoreHashMap(score.getName(),String.valueOf(score.getScore())));
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

    // Resolves the async task call.
    private Score getSelectedScore() {
        // It can be null if any row has been selected or the last selected has been removed.
        if(selectedLocalScore != null) {
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

    // This method is call by the async task. It adds the score to the list and updates the ListView.
    public void addLocalScore(Score score){
        localScores.add(getScoreHashMap(score.getName(), String.valueOf(score.getScore())));
        localAdapter.notifyDataSetChanged();
    }

    // This method is call by the async task. It adds the score to the list and updates the ListView.
    public void addFriendScore(HighScore score){
        friendsScores.add(getScoreHashMap(score.getName(),score.getScoring()));
        friendsAdapter.notifyDataSetChanged();
    }

    // Transform to the HashMap required by the list of elements passed to the SimpleAdapter.
    private HashMap<String, String> getScoreHashMap(String user, String score) {
        HashMap<String,String> item = new HashMap<String,String>();
        item.put("player", user);
        item.put("score", score);
        return item;
    }

    // Calls the aysnc task to get the ordered Friends scores.
    private void getFriendsScores() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        new GetFriendHighScoresService(this).executeService(preferences.getString("user_name",""));
    }

    // Calls the aysnc task to get the ordered Local scores.
    private void getLocalScores() {
        new GetLocalScoresAsyncTask(this).execute();
    }
}
