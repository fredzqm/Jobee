package com.fredzqm.jobee.job_seeker.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.JobSeeker;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.VerifyAddressTask;
import com.fredzqm.jobee.notification.Notifier;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends ContainedFragment implements ValueEventListener {
    private static final String TAG = "job_seeker.HomeFragment";

    private AutoCompleteTextView nameEditText;
    private AutoCompleteTextView emailEditText;
    private AutoCompleteTextView addressEditText;
    private AutoCompleteTextView majorEditText;
    private Button mSaveChangesButton;

    private Callback mCallback;
    private JobSeeker mAccount;
    private DatabaseReference mRef;

    public HomeFragment() {
    }

    public static ContainedFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = JobSeeker.getRefernce().child(mCallback.getUserID());
        mRef.addValueEventListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.js_home_frag, container, false);
        nameEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_name);
        emailEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_email);
        addressEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_address);
        majorEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_major);
        mSaveChangesButton = (Button) view.findViewById(R.id.js_home_save_changes);
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String major = majorEditText.getText().toString();
                mAccount.setName(name);
                mAccount.setEmailAccount(email);
                mAccount.setMajor(major);
                mRef.setValue(mAccount);
                (new VerifyAddressTask(HomeFragment.this, addressEditText.getText().toString(), new VerifyAddressTask.Callback() {
                    @Override
                    public void verifiedResult(String verifiedAddress) {
                        mAccount.setAddress(verifiedAddress);
                        mRef.setValue(mAccount);
                    }

                    @Override
                    public void insist(String oldAddress) {
                        mAccount.setAddress(oldAddress);
                        mRef.setValue(mAccount);
                    }
                })).execute();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mAccount = dataSnapshot.getValue(JobSeeker.class);
        if (mAccount == null) {
            mAccount = JobSeeker.newInstance(mCallback.getUserID());
            mRef.setValue(mAccount);
        } else {
            nameEditText.setText(mAccount.getName());
            mCallback.setNavTitle(getContext().getString(R.string.hello) + mAccount.getName());
            emailEditText.setText(mAccount.getEmailAccount());
            addressEditText.setText(mAccount.getAddress());
            majorEditText.setText(mAccount.getMajor());
        }
        Notifier.notify(mAccount.getKey(), "changed", "notify");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled " + databaseError);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        String getUserID();
        void setNavTitle(String str);
    }
}
