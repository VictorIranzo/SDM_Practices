package labs.sdm.game.services;

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

import labs.sdm.game.activities.PlayActivity;

public class StoreScoreService {
    private PlayActivity context;

    public StoreScoreService(PlayActivity context) {
        this.context = context;
    }

    public void executeService(final String user, final int score){
        String url = "https://wwtbamandroid.appspot.com/rest/highscores";

        RequestQueue queue = Volley.newRequestQueue(context);

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

        StringRequest postRequest = new StringRequest(Request.Method.PUT, url, null, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", user);
                params.put("score", String.valueOf(score));

                return params;
            }
        };
        queue.add(postRequest);
    }
}
