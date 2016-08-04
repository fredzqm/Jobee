package com.fredzqm.jobee.job_seeker.Home;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
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

    private JobSeekerAccount mAccount;
    private Callback mCallback;

    private AutoCompleteTextView nameEditText;
    private AutoCompleteTextView emailEditText;
    private AutoCompleteTextView addressEditText;
    private Button mSaveChangesButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param account the account of the game
     * @return A new instance of fragment JobListFragment.
     */
    public static HomeFragment newInstance(JobSeekerAccount account) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable(EMAIL_ACCOUNT, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = getArguments().getParcelable(EMAIL_ACCOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_home_frag, container, false);
        nameEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_name);
        emailEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_email);
        addressEditText = (AutoCompleteTextView) view.findViewById(R.id.js_home_address);
        mSaveChangesButton = (Button) view.findViewById(R.id.js_home_save_changes);

        nameEditText.setText(mAccount.getName());
        emailEditText.setText("" + mAccount.getEmailAccount());
        addressEditText.setText(addressToString(mAccount.getAddress()));
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                mCallback.saveAccountUpdates(name, email, null);
                (new VerifyAddressTask()).execute(addressEditText.getText().toString());
            }
        });

        return view;
    }

    private class VerifyAddressTask extends AsyncTask<String, Integer, Address> {
        private String errorMessage;

        protected Address doInBackground(String... addressString) {
            Geocoder geo = new Geocoder(getContext());
            try {
                List<Address> addrls = geo.getFromLocationName(addressString[0], 1);
                if (addrls.size() >= 1)
                    return addrls.get(0);
                errorMessage = "invalid address";
            } catch (IOException e) {
                errorMessage = "No internet connection, cannot verify address";
            } catch (IllegalArgumentException e) {
                errorMessage = "address can't be empty";
            }
            return null;
        }

        protected void onPostExecute(Address result) {
            if (result != null) {
                mCallback.saveAccountUpdates(null, null, result);
                addressEditText.setText(addressToString(result));
                Log.d(TAG, "" + result);
                return;
            } else {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String addressToString(Address address) {
        if (address == null || address.getMaxAddressLineIndex() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i) + "\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
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
        void saveAccountUpdates(String name, String email, Address address);
    }
}
