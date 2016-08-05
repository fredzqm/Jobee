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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private static final String TAG = "HomeFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private AutoCompleteTextView nameEditText;
    private AutoCompleteTextView emailEditText;
    private AutoCompleteTextView addressEditText;
    private AutoCompleteTextView majorEditText;
    private Button mSaveChangesButton;

    private Callback mCallback;
    private JobSeeker mAccount;
    private DatabaseReference ref;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static ContainedFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference().child("mAccount");
        ref.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_home_frag, container, false);
        nameEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_name);
        emailEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_email);
        addressEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_address);
        majorEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_major);
        mSaveChangesButton = (Button) view.findViewById(R.id.js_home_save_changes);

        mAccount = new JobSeeker(mCallback.getUserID());
        nameEditText.setText(mAccount.getName());
        emailEditText.setText("" + mAccount.getEmailAccount());
        addressEditText.setText(mAccount.getAddress());
        majorEditText.setText(mAccount.getMajor());

        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String major = majorEditText.getText().toString();
                mAccount.setName(name);
                mAccount.setEmailAccount(email);
                mAccount.setMajor(major);
                (new VerifyAddressTask(HomeFragment.this, addressEditText.getText().toString(), new VerifyAddressTask.Callback() {
                    @Override
                    public void verifiedResult(String verifiedAddress) {
                        mAccount.setAddress(verifiedAddress);
                        addressEditText.setText(verifiedAddress);
                    }

                    @Override
                    public void insist(String oldAddress) {
                        mAccount.setAddress(oldAddress);
                        addressEditText.setText(oldAddress);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        String getUserID();
    }
}
