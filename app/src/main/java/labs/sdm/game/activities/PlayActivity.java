package labs.sdm.game.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    private ImageButton hintAudience;
    private ImageButton hintFifty;
    private ImageButton hintCall;

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

        hintAudience = (ImageButton) findViewById(R.id.but_public_comodin);
        hintFifty = (ImageButton) findViewById(R.id.but_half_comodin);
        hintCall = (ImageButton) findViewById(R.id.but_call_comodin);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentQuestionNum = preferences.getInt("current_question_num", 0);
        availableHints = preferences.getInt("available_hints", 3);

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textPlayFor = (TextView) findViewById(R.id.textMoneyPlay);
        textNumQuestion = (TextView) findViewById(R.id.textNumQuestion);

        buttonAnswer1 = (Button) findViewById(R.id.butAnswer1);
        buttonAnswer2 = (Button) findViewById(R.id.butAnswer2);
        buttonAnswer3 = (Button) findViewById(R.id.butAnswer3);
        buttonAnswer4 = (Button) findViewById(R.id.butAnswer4);

        questions = QuestionGenerator.generateQuestionList();

        GetNextQuestion();
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("current_question_num", currentQuestionNum);
        editor.putInt("available_hints", availableHints);
        editor.apply();
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

        enabledAllAnswerButtons();
        deleteHighlightAnswerButtons();
    }

    private void checkIfCorrect(int answer) {
        if(Integer.parseInt(currentQuestion.getRight()) == answer) {
            currentQuestionNum++;
            GetNextQuestion();
        } else{
            // TODO: Decide what to do when a wrong answer occurs.
            storeScore();
            this.finish();
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

    public void butAudienceHintonClicked(View v){
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getAudience()));
        checkIfDisableHints();
    }

    public void butCallHintonClicked(View v){
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getPhone()));
        checkIfDisableHints();
    }

    public void butFiftyHintonClicked(View v){
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty1()));
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty2()));
        checkIfDisableHints();
    }

    private void checkIfDisableHints(){
        availableHints--;
        if(availableHints < 1){
            // TODO: Review if this has to be enabled at the end of the game.
            hintCall.setEnabled(false);
            hintAudience.setEnabled(false);
            hintFifty.setEnabled(false);
        }
    }

    private void hightLightButtonAnswer(int answer){
        // TODO: Do this better, it is difficult to see it.
        getButtonAnswerByIndex(answer).setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void disabledButtonAnswer(int answer){
        getButtonAnswerByIndex(answer).setEnabled(false);
    }

    private Button getButtonAnswerByIndex(int answer){
        switch (answer){
            case 1: return buttonAnswer1;
            case 2: return buttonAnswer2;
            case 3: return buttonAnswer3;
            case 4: return buttonAnswer4;

            default: return new Button(this);
        }
    }

    private void enabledAllAnswerButtons(){
        buttonAnswer1.setEnabled(true);
        buttonAnswer2.setEnabled(true);
        buttonAnswer3.setEnabled(true);
        buttonAnswer4.setEnabled(true);
    }

    private void deleteHighlightAnswerButtons() {
        buttonAnswer1.setTypeface(Typeface.DEFAULT);
        buttonAnswer2.setTypeface(Typeface.DEFAULT);
        buttonAnswer3.setTypeface(Typeface.DEFAULT);
        buttonAnswer4.setTypeface(Typeface.DEFAULT);
    }

    // TODO: Store score and go back to Menu.
    public void butLeaveonClicked(View v){
        storeScore();
        this.finish();
    }

    private void storeScore(){

    }
}