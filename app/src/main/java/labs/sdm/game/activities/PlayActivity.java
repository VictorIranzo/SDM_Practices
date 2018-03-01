package labs.sdm.game.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.pojo.Question;
import labs.sdm.game.pojo.QuestionGenerator;

public class PlayActivity extends AppCompatActivity {

    List<Question> questions;
    Question currentQuestion;
    private int currentQuestionNum;
    private int availableHints;
    private int prizes[] = {100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};

    private TextView textQuestion;
    private TextView textPlayFor;
    private TextView textNumQuestion;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Button buttonAnswer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textPlayFor = (TextView) findViewById(R.id.textMoneyPlay);
        textNumQuestion = (TextView) findViewById(R.id.textNumQuestion);

        buttonAnswer1 = (Button) findViewById(R.id.butAnswer1);
        buttonAnswer2 = (Button) findViewById(R.id.butAnswer2);
        buttonAnswer3 = (Button) findViewById(R.id.butAnswer3);
        buttonAnswer4 = (Button) findViewById(R.id.butAnswer4);

        questions = QuestionGenerator.generateQuestionList();
        currentQuestionNum = 0;
        GetNextQuestion();


    }

    private void GetNextQuestion() {
        currentQuestion = questions.get(currentQuestionNum);

        textQuestion.setText(currentQuestion.getText());
        textPlayFor.setText(getString(R.string.play_for) + prizes[currentQuestionNum] + getString(R.string.money));
        textNumQuestion.setText(getString(R.string.numQuestion) + currentQuestion.getNumber());

        buttonAnswer1.setText(currentQuestion.getAnswer1());
        buttonAnswer2.setText(currentQuestion.getAnswer2());
        buttonAnswer3.setText(currentQuestion.getAnswer3());
        buttonAnswer4.setText(currentQuestion.getAnswer4());

        currentQuestionNum++;
    }

    private void checkIfCorrect(int answer) {
        if(Integer.parseInt(currentQuestion.getRight()) == answer) {
            GetNextQuestion();
        }
    }

    public void butAnswer1onClicked(View v) {
        checkIfCorrect(1);
    }

    public void butAnswer2onClicked(View v) {
        checkIfCorrect(2);
    }

    public void butAnswer3onClicked(View v) {
        checkIfCorrect(3);
    }

    public void butAnswer4onClicked(View v) {
        checkIfCorrect(4);
    }
}