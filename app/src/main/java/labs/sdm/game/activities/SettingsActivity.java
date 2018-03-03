package labs.sdm.game.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
}
