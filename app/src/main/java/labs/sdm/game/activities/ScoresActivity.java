/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.game.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.adapters.QuotationAdapter;
import labs.sdm.game.pojo.Quotation;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        final QuotationAdapter quotationAdapter = new QuotationAdapter(this,R.layout.activity_favourite,getMockQuotations());
        final ListView listViewFavQuotes = findViewById(R.id.listViewFavQuotes);
        listViewFavQuotes.setAdapter(quotationAdapter);
        listViewFavQuotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String authorName = ((TextView)view.findViewById(R.id.tvAuthor)).getText().toString();
                getAuthorInfo(authorName);
            }
        });
        listViewFavQuotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.deleteDialog);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quotationAdapter.remove(quotationAdapter.getItem(position));
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

                return false;
            }
        });
    }

    public void getAuthorInfo(String author) {
        // Get the quotation author from the data source and
        // encode it using UTF-8 to be used as part of an URL

        // If the quotation is not anonymous, then access Wikipedia
        if (author!= null && !author.isEmpty()) {
            // Create an implicit Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Specify the URI required to search for the author on Wikipedia
            intent.setData(
                    Uri.parse("http://en.wikipedia.org/wiki/Special:Search?search="
                            + author));
            // Check whether there is an application able to handle that Intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Launch the activity
                startActivity(intent);
            }
        }
        // If the quotation is anonymous then display a message
        else {
            Toast.makeText(this, R.string.anonymous_author, Toast.LENGTH_SHORT).show();
        }
    }

    public List<Quotation> getMockQuotations()
    {
        List<Quotation> result = new ArrayList<>();
        Quotation item;

        item = new Quotation();
        item.setQuote("Think Big");
        item.setAuthor("IMAX");
        result.add(item);

        item = new Quotation();
        item.setQuote("Push button publishing");
        item.setAuthor("Blogger");
        result.add(item);

        item = new Quotation();
        item.setQuote("Beauty outside. Beast inside");
        item.setAuthor("Mac Pro");
        result.add(item);

        item = new Quotation();
        item.setQuote("American by birth. Rebel by choice");
        item.setAuthor("Harley Davidson");
        result.add(item);

        item = new Quotation();
        item.setQuote("Don't be evil");
        item.setAuthor("Google");
        result.add(item);

        item = new Quotation();
        item.setQuote("If you want to impress someone, put him on your Black list");
        item.setAuthor("Johnnie Walker");
        result.add(item);

        item = new Quotation();
        item.setQuote("Live in your world. Play in ours");
        item.setAuthor("Playstation");
        result.add(item);

        item = new Quotation();
        item.setQuote("Impossible is nothing");
        item.setAuthor("Adidas");
        result.add(item);

        item = new Quotation();
        item.setQuote("Solutions for a small planet");
        item.setAuthor("IBM");
        result.add(item);

        item = new Quotation();
        item.setQuote("I'm lovin it");
        item.setAuthor("McDonalds");
        result.add(item);

        item = new Quotation();
        item.setQuote("Just do it");
        item.setAuthor("Nike");
        result.add(item);

        item = new Quotation();
        item.setQuote("Melts in your mouth, not in your hands");
        item.setAuthor("M&M");
        result.add(item);

        item = new Quotation();
        item.setQuote("Because you're worth it");
        item.setAuthor("Loreal");
        result.add(item);

        return result;
    }
}
