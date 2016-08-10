package com.fredzqm.jobee.notification;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.*;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Notifier extends TextHttpResponseHandler {
    public static final String TAG = "Notifier";
    public static final String PATH = "token";
    public static final String URL = "https://fcm.googleapis.com/fcm/send";

    // data payload keys
    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";

    private static AsyncHttpClient client;
    private static ResponseHandlerInterface requestSender;

    static {
        client = new AsyncHttpClient();
        client.addHeader("Authorization", " key=AIzaSyC6lpoN9ekbvLjY5AtAOOHZmvLcSscYdes");
        requestSender = new Notifier();
    }

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
    }

    /** Example here
     * String token = FirebaseInstanceId.getInstance().getToken();
     * Log.d(TAG, token);
     * Notifier requestSender = new Notifier(LoginActivity.this);
     * requestSender.notify(token, "Portugal vs. Denmark", "5 to 1");
     *
     * @param userID
     * @param title
     * @param body
     */
    public static void notify(String userID, String title, String body) {
        final RequestParams params = new RequestParams();
        Map<String, String> data = new HashMap<String, String>();
        data.put(TITLE, title);
        data.put(BODY, body);
        params.put("data", data);
        getReference().child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);
                params.put("to", token);
                Log.d(TAG, "to " + token);
                client.post(URL, params, requestSender);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(TAG, "statusCode: " + statusCode + "\n" + responseString);
        throw new RuntimeException(throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.d(TAG, "statusCode: " + statusCode + "\n" + responseString);
    }

}