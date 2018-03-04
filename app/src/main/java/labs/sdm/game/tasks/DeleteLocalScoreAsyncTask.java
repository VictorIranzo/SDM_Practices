package labs.sdm.game.tasks;

import android.content.Context;
import android.os.AsyncTask;

import labs.sdm.game.activities.ScoresActivity;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class DeleteLocalScoreAsyncTask extends AsyncTask<Void, Void, Void> {

    private ScoresActivity context;
    private Score deletedScore;

    public DeleteLocalScoreAsyncTask(ScoresActivity context, Score deletedScore) {
        this.context = context;
        this.deletedScore = deletedScore;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        GameDatabase.getGameDatabase(context).scoreDAO().deleteScore(deletedScore);
        return null;
    }
}