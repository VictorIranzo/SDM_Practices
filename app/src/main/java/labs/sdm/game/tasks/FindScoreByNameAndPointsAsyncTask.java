package labs.sdm.game.tasks;

import android.os.AsyncTask;

import labs.sdm.game.activities.ScoresActivity;
import labs.sdm.game.persistence.GameDatabase;
import labs.sdm.game.pojo.Score;

public class FindScoreByNameAndPointsAsyncTask extends AsyncTask<Void, Void, Score> {

    private ScoresActivity context;
    private String user;
    private int points;

    public FindScoreByNameAndPointsAsyncTask(ScoresActivity context, String user, int points) {
        this.context = context;
        this.user = user;
        this.points = points;
    }

    @Override
    protected Score doInBackground(Void... voids) {
        return GameDatabase.getGameDatabase(context).scoreDAO().findScoreByUserandScore(user,points);
    }
}