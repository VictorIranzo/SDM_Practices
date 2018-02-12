/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.game.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import labs.sdm.game.R;

public class PlayActivity extends AppCompatActivity {

    // Whether the user can ask for a new quotation, so the option can appear in the ActionBar
    boolean newQuotation = true;
    // Whether this quotation could be added to the favourites list,
    // so the option can appear in the ActionBar
    boolean addQuotation = false;

    // Hold references to View objects
    ProgressBar progressBar = null;
    TextView tvQuote;
    TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        // Keep a reference to:
        //  the ProgressBar showing that the HTTP request is still in progress
        //  the TextView displaying the text of the quotation
        //  the TextView displaying the author of the quotation
        progressBar = (ProgressBar) findViewById(R.id.pbGettingQuotation);
        tvQuote = ((TextView) findViewById(R.id.tvQuotation));
        tvAuthor = ((TextView) findViewById(R.id.tvAuthor));

        // As there is no quotation to show when the activity is created,
        // display a greetings message that includes the user's name
        tvQuote.setText(
                String.format(getResources().getString(R.string.greetings),
                        getResources().getString(R.string.nameless)));
    }

    public void refresh(View v) {
        tvQuote.setText(R.string.sample_quotation);
        tvAuthor.setText(R.string.sample_author);
    }

}