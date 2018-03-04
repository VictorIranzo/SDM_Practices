package labs.sdm.game.services;

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

import labs.sdm.game.activities.SettingsActivity;

public class AddFriendService {
    private SettingsActivity context;

    public AddFriendService(SettingsActivity context) {
        this.context = context;
    }

    public void executeService(final String user, final String friend){
        String url = "https://wwtbamandroid.appspot.com/rest/friends";

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO: Move this messages to strings.xml
                context.showMessageResultAndEmptyFriendNameText("Friend added correctly");
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

                context.showMessageResultAndEmptyFriendNameText(message);
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
    }
}
