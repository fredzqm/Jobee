package com.fredzqm.jobee.notification;

import android.content.Context;
import android.util.Log;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Recruiter;
import com.fredzqm.jobee.model.Submission;
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

    // notificaton types
    public static final String OFFER = "OFFER";
    public static final String REJECT = "REJECT";
    public static final String ACCEPT_OFFER = "ACCEPT_OFFER";
    // data payload keys
    public static final String NOTIF_TYPE = "NOTIF_TYPE";
    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";
    public static final String SUBMISSION_KEY = "SUBMISSION_KEY";
    private static final String DECLINE_OFFER = "DECLINE_OFFER";

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

    public static void notifyOffer(Context context, Submission submission, Recruiter recruiter) {
        // update firebase
        submission.setStatus(Submission.OFFERED);
        DatabaseReference ref = Submission.getReference().child(submission.getKey());
        ref.setValue(submission);
        // send fcm message
        Map<String, String> data = new HashMap<>();
        data.put(NOTIF_TYPE, OFFER);
        data.put(TITLE, context.getString(R.string.notif_offer_title));
        data.put(BODY, context.getString(R.string.notif_offer_body));
        data.put(SUBMISSION_KEY, submission.getKey());
        sendNotification(submission.getJobSeekerKey(), data);
    }

    public static void notifyReject(Context context, Submission submission, Recruiter recruiter) {
        // update firebase
        submission.setStatus(Submission.REJECTED);
        DatabaseReference ref = Submission.getReference().child(submission.getKey());
        ref.setValue(submission);
        // send fcm message
        Map<String, String> data = new HashMap<>();
        data.put(NOTIF_TYPE, REJECT);
        data.put(TITLE, context.getString(R.string.notif_reject_title));
        data.put(BODY, context.getString(R.string.notif_reject_body, recruiter.getCompany()));
        data.put(SUBMISSION_KEY, submission.getKey());
        sendNotification(submission.getJobSeekerKey(), data);
    }

    public static void notifyAccpetOffer(Context context, Submission submission) {
        // update firebase
        submission.setStatus(Submission.ACCEPTED);
        DatabaseReference ref = Submission.getReference().child(submission.getKey());
        ref.setValue(submission);
        // send fcm message
        Map<String, String> data = new HashMap<>();
        data.put(NOTIF_TYPE, ACCEPT_OFFER);
        data.put(TITLE, context.getString(R.string.notif_accept_offer_title));
        data.put(BODY, context.getString(R.string.notif_accept_offer_body));
        data.put(SUBMISSION_KEY, submission.getKey());
        sendNotification(submission.getRecruiterKey(), data);
    }

    public static void notifyDeclineOffer(Context context, Submission submission) {
        // update firebase
        submission.setStatus(Submission.DECLINED);
        DatabaseReference ref = Submission.getReference().child(submission.getKey());
        ref.setValue(submission);
        // send fcm message
        Map<String, String> data = new HashMap<>();
        data.put(NOTIF_TYPE, DECLINE_OFFER);
        data.put(TITLE, context.getString(R.string.notif_decline_offer_title));
        data.put(BODY, context.getString(R.string.notif_decline_offer_body));
        data.put(SUBMISSION_KEY, submission.getKey());
        sendNotification(submission.getRecruiterKey(), data);
    }

    private static void sendNotification(String userID, Map<String, String> data) {
        final RequestParams params = new RequestParams();
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