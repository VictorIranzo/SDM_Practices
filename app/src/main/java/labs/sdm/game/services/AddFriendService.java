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

import labs.sdm.game.R;
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
                context.showMessageResultAndEmptyFriendNameText(context.getString(R.string.msg_friend_added));
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
