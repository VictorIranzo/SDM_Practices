package labs.sdm.game.services;

import android.net.Uri;
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

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest postRequest = new StringRequest(Request.Method.GET,url,responseListener, errorListener);

        queue.add(postRequest);
    }
}
