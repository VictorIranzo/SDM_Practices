package labs.sdm.game.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import labs.sdm.game.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Toast.makeText(this, item.getTitle(),Toast.LENGTH_SHORT).show();

        switch (item.getItemId()){
            case R.id.action_bar_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

                break;
        }

        return true;
    }
}
