package labs.sdm.game.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import labs.sdm.game.pojo.Score;

@Dao
public interface ScoreDAO {

    @Query("SELECT * FROM score_table ORDER BY score DESC")
    List<Score> getOrderedScores();

    @Query("SELECT * FROM score_table WHERE name = :user AND score = :points LIMIT 1")
    Score findScoreByUserandScore(String user, int points);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addScore(Score score);

    @Delete
    void deleteScore(Score score);
}
