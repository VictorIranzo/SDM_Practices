package labs.sdm.game.activities;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.pojo.Question;
import labs.sdm.game.fakes.QuestionGenerator;

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

        // If available hints is -1 is because the last game has end, so the available hints is restart
        // to the hints established in the settings view.
        availableHints = preferences.getInt("available_hints", -1);

        if(availableHints == -1)
        {
            availableHints = preferences.getInt("total_hints",3);
        }

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textPlayFor = (TextView) findViewById(R.id.textMoneyPlay);
        textNumQuestion = (TextView) findViewById(R.id.textNumQuestion);

        buttonAnswer1 = (Button) findViewById(R.id.butAnswer1);
        buttonAnswer2 = (Button) findViewById(R.id.butAnswer2);
        buttonAnswer3 = (Button) findViewById(R.id.butAnswer3);
        buttonAnswer4 = (Button) findViewById(R.id.butAnswer4);

        questions = new ArrayList<Question>();
        new QuestionManager().execute();
    }

    private void addQuestion(Question q){
        questions.add(q);
        if(questions.size()==1){
            GetNextQuestion();
            checkIfDisableHints();
        }
    }

    // This is the unique place where this preferences is stored. It is called when we click Back,
    // minimize the app or finish the activity by code.
    @Override
    protected void onPause(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("current_question_num", currentQuestionNum);
        editor.putInt("available_hints", availableHints);
        editor.apply();

        super.onPause();
    }

    // Gets the next question, updating all the visual information and restarting formats.
    private void GetNextQuestion() {
        currentQuestion = questions.get(currentQuestionNum);

        textQuestion.setText(currentQuestion.getText());
        textPlayFor.setText(getString(R.string.play_for) + prizes[currentQuestionNum] + getString(R.string.money));
        textNumQuestion.setText(getString(R.string.numQuestion) + currentQuestion.getNumber());

        buttonAnswer1.setText(currentQuestion.getAnswer1());
        buttonAnswer2.setText(currentQuestion.getAnswer2());
        buttonAnswer3.setText(currentQuestion.getAnswer3());
        buttonAnswer4.setText(currentQuestion.getAnswer4());

        enableAllAnswerButtons();
        deleteHighlightAnswerButtons();
    }

    // If the answer is correct, we pass to the next question. If not, the game is ended.
    private void checkIfCorrect(int answer) {
        if(Integer.parseInt(currentQuestion.getRight()) == answer) {
            currentQuestionNum++;
            GetNextQuestion();
        } else{
            // TODO: Decide what to do when a wrong answer occurs.
            storeScore();
            endGame();
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

    public void butAnswer4onClicked(View v) { checkIfCorrect(4); }

    // Highligths 1 answer button and check if all the hints have been used.
    public void butAudienceHintonClicked(View v){
        availableHints--;
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getAudience()));
        checkIfDisableHints();
    }

    // Highligths 1 answer button and check if all the hints have been used.
    public void butCallHintonClicked(View v){
        availableHints--;
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getPhone()));
        checkIfDisableHints();
    }

    // Disables 2 answer buttons and check if all the hints have been used.
    public void butFiftyHintonClicked(View v){
        availableHints--;
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty1()));
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty2()));
        checkIfDisableHints();
    }

    // After using a hint and when the activity is created this check is passed to disable hint buttons
    // if no more hints are available.
    private void checkIfDisableHints(){
        if(availableHints < 1){
            // TODO: Review if this has to be enabled at the end of the game.
            hintCall.setEnabled(false);
            hintAudience.setEnabled(false);
            hintFifty.setEnabled(false);
        }
    }

    // Highlights a button that maybe is the correct answer.
    private void hightLightButtonAnswer(int answer){
        // TODO: Do this better, it is difficult to see it.
        getButtonAnswerByIndex(answer).setTypeface(Typeface.DEFAULT_BOLD);
    }

    // Disables an answer button after a hint is used.
    private void disabledButtonAnswer(int answer){
        getButtonAnswerByIndex(answer).setEnabled(false);
    }

    // Returns the button instance of the passed index answer button.
    private Button getButtonAnswerByIndex(int answer){
        switch (answer){
            case 1: return buttonAnswer1;
            case 2: return buttonAnswer2;
            case 3: return buttonAnswer3;
            case 4: return buttonAnswer4;

            default: return new Button(this);
        }
    }

    // Called for every new question.
    private void enableAllAnswerButtons(){
        buttonAnswer1.setEnabled(true);
        buttonAnswer2.setEnabled(true);
        buttonAnswer3.setEnabled(true);
        buttonAnswer4.setEnabled(true);
    }

    // Called for every new question.
    private void deleteHighlightAnswerButtons() {
        buttonAnswer1.setTypeface(Typeface.DEFAULT);
        buttonAnswer2.setTypeface(Typeface.DEFAULT);
        buttonAnswer3.setTypeface(Typeface.DEFAULT);
        buttonAnswer4.setTypeface(Typeface.DEFAULT);
    }

    // TODO: Store score and go back to Menu.
    // Stores score and ends the game.
    public void butLeaveonClicked(View v){
        // TODO: Show confirmation dialog.
        storeScore();
        endGame();
    }

    private void storeScore() {

    }

    private void endGame() {
        // Only the attributes are set as then the onPause method is gonna be called.
        currentQuestionNum = 0;
        availableHints = -1;
        this.finish();
    }

    private class QuestionManager extends AsyncTask<Void, Question, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            XmlResourceParser parser = getResources().getXml(R.xml.questions);
            List<Question> questions = new ArrayList<Question>();

            try
            {
                int eventType = parser.getEventType();
                while(eventType != XmlResourceParser.END_DOCUMENT)
                {
                    // In the comparison is better compare first the constant value (see joda rule) for
                    // prevention of null values.
                    if(!"quizz".equals(parser.getName()) && eventType == XmlResourceParser.START_TAG){
                        Question question = new Question(
                                parser.getAttributeValue(null,"number"),
                                parser.getAttributeValue(null,"text"),
                                parser.getAttributeValue(null,"answer1"),
                                parser.getAttributeValue(null,"answer2"),
                                parser.getAttributeValue(null,"answer3"),
                                parser.getAttributeValue(null,"answer4"),
                                parser.getAttributeValue(null,"right"),
                                parser.getAttributeValue(null,"audience"),
                                parser.getAttributeValue(null,"phone"),
                                parser.getAttributeValue(null,"fifty1"),
                                parser.getAttributeValue(null,"fifty2")
                        );
                        questions.add(question);
                        publishProgress(question);
                    }
                    parser.next();
                    eventType = parser.getEventType();
                }
            }
            catch (XmlPullParserException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }

            return null;
        }

        @Override
        protected void onProgressUpdate(Question... question){
            addQuestion(question[0]);
        }
    }
}