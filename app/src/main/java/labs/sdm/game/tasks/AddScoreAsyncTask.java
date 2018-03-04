package labs.sdm.game.tasks;

import android.os.AsyncTask;

import labs.sdm.game.activities.PlayActivity;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class AddScoreAsyncTask extends AsyncTask<Void, Void, Void>{

    private  PlayActivity context;
    private Score score;

    public AddScoreAsyncTask(PlayActivity context, Score score) {
        this.context = context;
        this.score = score;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        GameDatabase.getGameDatabase(context).scoreDAO().addScore(score);

        return null;
    }
}
