package labs.sdm.game.services;

import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;

import labs.sdm.game.R;
import labs.sdm.game.activities.ScoresActivity;
import labs.sdm.game.pojo.HighScore;
import labs.sdm.game.pojo.HighScoreList;

public class GetFriendHighScoresService {
    public ScoresActivity context;

    public GetFriendHighScoresService(ScoresActivity context) {
        this.context = context;
    }

    public void executeService(String user){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("wwtbamandroid.appspot.com")
                .appendPath("rest").appendPath("highscores").appendQueryParameter("name",user);
        String url = builder.build().toString();

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HighScoreList listScores = gson.fromJson(response, HighScoreList.class);
                for (HighScore score: listScores.getScores()) {
                    context.addFriendScore(score);
                }

                // The space that uses the progress bar is removed.
                ((ProgressBar)context.findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = context.getString(R.string.error_occured);
                if (volleyError instanceof NetworkError) {
                    message = context.getString(R.string.error_network);
                } else if (volleyError instanceof ServerError) {
                    message = context.getString(R.string.error_server);
                } else if (volleyError instanceof AuthFailureError) {
                    message = context.getString(R.string.error_authorization);
                } else if (volleyError instanceof ParseError) {
                    message = context.getString(R.string.error_parsing);
                } else if (volleyError instanceof NoConnectionError) {
                    message = context.getString(R.string.error_noconnection);
                } else if (volleyError instanceof TimeoutError) {
                    message = context.getString(R.string.error_timeot);
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest postRequest = new StringRequest(Request.Method.GET,url,responseListener, errorListener);

        queue.add(postRequest);
    }
}
