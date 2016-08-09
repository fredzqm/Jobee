package com.fredzqm.jobee;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RequestSender extends JsonHttpResponseHandler {
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public void notifyApp(RequestParams params) {
        client.post(URL, params, this);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
        // Pull out the first event on the public timeline
        JSONObject firstEvent = null;
        String tweetText = null;
        try {
            firstEvent = (JSONObject) timeline.get(0);
            tweetText = firstEvent.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Do something with the response
        System.out.println(tweetText);
    }

}