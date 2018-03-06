package labs.sdm.game.tasks;

import android.os.AsyncTask;

import java.util.List;

import labs.sdm.game.activities.ScoresActivity;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class GetLocalScoresAsyncTask extends AsyncTask<Void, Score, Void> {

    private ScoresActivity context;

    public GetLocalScoresAsyncTask(ScoresActivity context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Score> scores = GameDatabase.getGameDatabase(context).scoreDAO().getOrderedScores();

        for (Score score : scores)
        {
            publishProgress(score);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Score... score){
        context.addLocalScore(score[0]);
    }
}
