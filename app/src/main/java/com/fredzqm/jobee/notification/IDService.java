package com.fredzqm.jobee.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.model.JobSeeker;
import com.fredzqm.jobee.model.Recruiter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class IDService extends FirebaseInstanceIdService {
    private static final String TAG = "InstIDService";

    public IDService() {
    }

    @Override
    public void onTokenRefresh() {
        updateToken();
    }

    public static void updateToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Notifier.getReference().child(LoginActivity.getUserID()).setValue(refreshedToken);
    }

}
