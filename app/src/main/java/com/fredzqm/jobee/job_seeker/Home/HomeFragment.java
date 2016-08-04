package com.fredzqm.jobee.job_seeker.Home;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.JobSeekerAccount;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.VerifyAddressTask;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends ContainedFragment {
    private static final String TAG = "HomeFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL_ACCOUNT = "param1";

    private Callback mCallback;

    private AutoCompleteTextView nameEditText;
    private AutoCompleteTextView emailEditText;
    private AutoCompleteTextView addressEditText;
    private AutoCompleteTextView majorEditText;
    private Button mSaveChangesButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static ContainedFragment newInstance() {
        return new HomeFragment();
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

        JobSeekerAccount account = mCallback.getAccount();
        nameEditText.setText(account.getName());
        emailEditText.setText("" + account.getEmailAccount());
        addressEditText.setText(account.getAddress());
        majorEditText.setText(account.getMajor());

        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String major = majorEditText.getText().toString();
                final JobSeekerAccount account = mCallback.getAccount();
                account.setName(name);
                account.setEmailAccount(email);
                account.setMajor(major);
                (new VerifyAddressTask(HomeFragment.this, addressEditText.getText().toString(), new VerifyAddressTask.Callback() {
                    @Override
                    public void verifiedResult(String verifiedAddress) {
                        account.setAddress(verifiedAddress);
                        addressEditText.setText(verifiedAddress);
                    }

                    @Override
                    public void insist(String oldAddress) {
                        account.setAddress(oldAddress);
                        addressEditText.setText(oldAddress);
                    }
                })).execute();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
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
    public void clickFab() {

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
        JobSeekerAccount getAccount();
    }
}
