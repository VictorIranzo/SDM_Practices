package labs.sdm.game.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.managers.QuestionManager;
import labs.sdm.game.pojo.Question;
import labs.sdm.game.pojo.Score;
import labs.sdm.game.services.StoreScoreService;
import labs.sdm.game.tasks.AddScoreAsyncTask;

public class PlayActivity extends AppCompatActivity {

    List<Question> questions;
    Question currentQuestion;
    private int currentQuestionNum;
    private int availableHints;
    private int prizes[] = {100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};

    private ImageButton hintAudience;
    private ImageButton hintFifty;
    private ImageButton hintCall;
    private ImageButton leaveButton;

    private TextView textQuestion;
    private TextView textPlayFor;
    private TextView textNumQuestion;

    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Button buttonAnswer4;

    private String hintApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        hintAudience = (ImageButton) findViewById(R.id.but_public_comodin);
        hintFifty = (ImageButton) findViewById(R.id.but_half_comodin);
        hintCall = (ImageButton) findViewById(R.id.but_call_comodin);
        leaveButton = (ImageButton) findViewById(R.id.but_close_comodin);

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

        leaveButton.getBackground().setColorFilter(
                getResources().getColor(R.color.play_background_exit), PorterDuff.Mode.MULTIPLY);

        questions = QuestionManager.GetQuestions(this);

        hintApplied = preferences.getString("hint_applied","");

        GetNextQuestion();
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
        textPlayFor.setText(getString(R.string.play_for) + " " + prizes[currentQuestionNum] + " " + getString(R.string.money));
        textNumQuestion.setText(getString(R.string.numQuestion) + " " + currentQuestion.getNumber());

        buttonAnswer1.setText(currentQuestion.getAnswer1());
        buttonAnswer2.setText(currentQuestion.getAnswer2());
        buttonAnswer3.setText(currentQuestion.getAnswer3());
        buttonAnswer4.setText(currentQuestion.getAnswer4());

        enableAllAnswerButtons();
        deleteHighlightAnswerButtons();

        applyHintAfterPause();
    }

    // If the answer is correct, we pass to the next question. If not, the game is ended.
    private void checkIfCorrect(int answer) {
        if(Integer.parseInt(currentQuestion.getRight()) == answer) {
            if(currentQuestionNum == 14){
                winDialogAndEnd();
                storeScore(prizes[currentQuestionNum]);
            }
            else {
                currentQuestionNum++;
                setHintApplied("");
                GetNextQuestion();
            }
        } else{
            // The order of this 2 method calls is important, as the score stored depends on the current
            // question number, that is set to 0 in the endGame() method.
            showLoseDialog();
            storeScore(getScoreWhenLose());
        }
    }

    // The hint applied is restart to null after a question is answered correctly.
    private void setHintApplied(String hint) {
        hintApplied = hint;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("hint_applied", hintApplied);
        editor.apply();
    }

    private void applyHintAfterPause()
    {
        switch(hintApplied){
            case "":
                checkIfDisableHints();
                break;
            case "call":
                hightLightButtonAnswer(Integer.parseInt(currentQuestion.getPhone()));
                checkIfDisableHints();
                break;
            case "audience":
                hightLightButtonAnswer(Integer.parseInt(currentQuestion.getAudience()));
                checkIfDisableHints();
                break;
            case "fifty":
                disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty1()));
                disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty2()));
                checkIfDisableHints();
                break;
        }
    }

    private void showLoseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_Lose));
        builder.setMessage(getString(R.string.dialog_prize) + " " + getScoreWhenLose() + " " + getString(R.string.money));
        builder.setPositiveButton(android.R.string.yes,null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                endGame();
            }
        });
        builder.create().show();
    }

    private void winDialogAndEnd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_Win));
        builder.setMessage(getString(R.string.dialog_prize) + " " + prizes[currentQuestionNum] + " " + getString(R.string.money));
        builder.setPositiveButton(android.R.string.yes,null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                endGame();
            }
        });
        builder.create().show();
    }

    // When the game is lost, the score depends on the question reached.
    private int getScoreWhenLose() {
        if(currentQuestionNum < 5) return 0;
        if(currentQuestionNum < 10) return 1000;
        return 32000;
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
        setHintApplied("audience");
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getAudience()));
        checkIfDisableHints();
    }

    // Highligths 1 answer button and check if all the hints have been used.
    public void butCallHintonClicked(View v){
        availableHints--;
        setHintApplied("call");
        hightLightButtonAnswer(Integer.parseInt(currentQuestion.getPhone()));
        checkIfDisableHints();
    }

    // Disables 2 answer buttons and check if all the hints have been used.
    public void butFiftyHintonClicked(View v){
        availableHints--;
        setHintApplied("fifty");
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty1()));
        disabledButtonAnswer(Integer.parseInt(currentQuestion.getFifty2()));
        checkIfDisableHints();
    }

    // After using a hint and when the activity is created this check is passed to disable hint buttons
    // if no more hints are available.
    private void checkIfDisableHints(){
        // In case that a hint has been used in this question or we don't have more hints, they are disabled.
        if(availableHints < 1 || hintApplied!=""){
            hintCall.setEnabled(false);
            hintAudience.setEnabled(false);
            hintFifty.setEnabled(false);

            hintCall.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_disabled), PorterDuff.Mode.MULTIPLY);
            hintAudience.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_disabled), PorterDuff.Mode.MULTIPLY);
            hintFifty.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_disabled), PorterDuff.Mode.MULTIPLY);
        }else {
            hintCall.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_available), PorterDuff.Mode.MULTIPLY);
            hintAudience.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_available), PorterDuff.Mode.MULTIPLY);
            hintFifty.getBackground().setColorFilter(
                    getResources().getColor(R.color.play_background_available), PorterDuff.Mode.MULTIPLY);
        }
    }

    // Highlights a button that maybe is the correct answer.
    private void hightLightButtonAnswer(int answer){
        getButtonAnswerByIndex(answer).getBackground().setColorFilter(
                getResources().getColor(R.color.play_background_hightlihgt), PorterDuff.Mode.MULTIPLY);
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
        buttonAnswer1.getBackground().clearColorFilter();
        buttonAnswer2.getBackground().clearColorFilter();
        buttonAnswer3.getBackground().clearColorFilter();
        buttonAnswer4.getBackground().clearColorFilter();
    }

    // Stores score and ends the game.
    public void butLeaveonClicked(View v){
        // If the game is leaved in the first question, the actual prize is 0. Otherwise, it can be
        // get from the prizes array.
        int aux;
        if(currentQuestionNum - 1 < 0) aux = 0;
        else aux = prizes[currentQuestionNum-1];
        final int money = aux;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_leave));
        builder.setMessage(getString(R.string.dialog_prize) + " " + money + getString(R.string.money) + '\n' +
                getString(R.string.dialog_numHints) + " " + availableHints);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                storeScore(money);
                endGame();
            }
        });

        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    // Stores the score in the DB and the server.
    private void storeScore(int prize) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String user = prefs.getString("user_name","");

        final Score score = new Score(user, prize);

        // Store in local DB.
        new AddScoreAsyncTask(this,score).execute();

        // Store in the server.
        new StoreScoreService(this).executeService(user,prize);
    }

    private void endGame() {
        // Only the attributes are set as then the onPause method is gonna be called.
        currentQuestionNum = 0;
        availableHints = -1;
        setHintApplied("");
        this.finish();
    }
}