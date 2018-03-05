package labs.sdm.game.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import labs.sdm.game.R;
import labs.sdm.game.services.AddFriendService;

public class SettingsActivity extends AppCompatActivity {

    private EditText userNameText;
    private Spinner hintsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userNameText = (EditText) findViewById(R.id.etUserName);
        hintsSpinner = (Spinner) findViewById(R.id.helpTimesSpinner);
    }

    // Saves the user name and the number of hints when the app go on pause.
    @Override
    protected void onPause() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("user_name", userNameText.getText().toString());
        editor.putInt("total_hints", Integer.parseInt(hintsSpinner.getSelectedItem().toString()));

        editor.apply();

        super.onPause();
    }

    @Override
    public void onBackPressed(){
        if(userNameText.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.msg_enter_user_name), Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(userNameText.getText().toString().isEmpty()){
                    Toast.makeText(this, getString(R.string.msg_enter_user_name), Toast.LENGTH_SHORT).show();
                } else {
                    return super.onOptionsItemSelected(item);
                }
                break;
        }
        return true;
    }

    // Sets the values of the components to the ones stored in the SharedPreferences.
    @Override
    protected void onResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userNameText.setText(preferences.getString("user_name","default_user"));
        hintsSpinner.setSelection(preferences.getInt("total_hints",3));

        super.onResume();
    }

    public void butAddFriendClicked(View v) {
        String user = ((EditText) findViewById(R.id.etUserName)).getText().toString();
        String friend = ((EditText) findViewById(R.id.nameFriendText)).getText().toString();

        if(user.isEmpty()){
            Toast.makeText(this, getString(R.string.msg_enter_user_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if(friend.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_enter_friend_name), Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AddFriendService(this).executeService(user,friend);
    }

    public void showMessageResultAndEmptyFriendNameText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        ((EditText) findViewById(R.id.nameFriendText)).setText("");
    }
}
