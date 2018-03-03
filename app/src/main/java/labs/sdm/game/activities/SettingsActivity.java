package labs.sdm.game.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

    // Sets the values of the components to the ones stored in the SharedPreferences.
    @Override
    protected void onResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userNameText.setText(preferences.getString("user_name",""));
        hintsSpinner.setSelection(preferences.getInt("total_hints",3));

        super.onResume();
    }

    public void butAddFriendClicked(View v) {
        String user = ((EditText) findViewById(R.id.etUserName)).getText().toString();
        String friend = ((EditText) findViewById(R.id.nameFriendText)).getText().toString();
        new AddFriendAsyncTask(user,friend).execute();
    }

    private void showMessageResultAndEmptyFriendNameText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        ((EditText) findViewById(R.id.nameFriendText)).setText("");
    }

    private class AddFriendAsyncTask extends AsyncTask<Void, String, Void>
    {

        private String user;
        private String friend;

        public AddFriendAsyncTask(String user, String friend) {
            this.user = user;
            this.friend = friend;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://wwtbamandroid.appspot.com/rest/friends";

            RequestQueue queue = Volley.newRequestQueue(SettingsActivity.this);

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // TODO: Move this messages to strings.xml
                    onProgressUpdate("Friend added correctly");
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // TODO: Move this messages to strings.xml
                    String message = "An error occured.";
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }

                    onProgressUpdate(message);
                }
            };

            StringRequest postRequest = new StringRequest(Request.Method.POST,url,responseListener, errorListener){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name",user);
                    params.put("friend_name",friend);

                    return params;
                }
            };

            queue.add(postRequest);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... message){
            showMessageResultAndEmptyFriendNameText(message[0]);
        }
    }
}
