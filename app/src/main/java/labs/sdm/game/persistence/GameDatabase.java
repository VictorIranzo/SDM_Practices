package labs.sdm.game.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import labs.sdm.game.pojo.Score;

@Database(entities = {Score.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase {

    private static GameDatabase gameDatabase;

    public abstract ScoreDAO scoreDAO();

    public synchronized static GameDatabase getGameDatabase(Context context)
    {
        if(gameDatabase == null)
        {
            gameDatabase = Room.databaseBuilder(context, GameDatabase.class, "game_database")
                            .build();
        }

        return gameDatabase;
    }
}
