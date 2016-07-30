package com.fredzqm.jobee.recruiter.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.RecruiterAccount;
import com.fredzqm.jobee.ContainedFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends ContainedFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL_ACCOUNT = "param1";

    private RecruiterAccount mAccount;
    private Callback mCallback;

    private AutoCompleteTextView nameEditText;
    private AutoCompleteTextView emailEditText;
    private AutoCompleteTextView companyEditText;
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
    public static HomeFragment newInstance(RecruiterAccount account) {
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
        View view = inflater.inflate(R.layout.re_home_frag, container, false);
        nameEditText = (AutoCompleteTextView) view.findViewById(R.id.re_home_name);
        emailEditText = (AutoCompleteTextView) view.findViewById(R.id.re_home_email);
        companyEditText = (AutoCompleteTextView) view.findViewById(R.id.re_home_company);
        mSaveChangesButton = (Button) view.findViewById(R.id.re_home_save_changes);

        nameEditText.setText(mAccount.getName());
        emailEditText.setText(mAccount.getEmailAccount());
        companyEditText.setText(mAccount.getCompany());
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String company = companyEditText.getText().toString();
                mCallback.saveAccountUpdates(name, email, company);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        void saveAccountUpdates(String name, String email, String company);
    }
}
