package com.fredzqm.jobee.job_seeker.AppliedJob;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.model.Submission;
import com.fredzqm.jobee.notification.Notifier;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link AppliedJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppliedJobFragment extends ContainedFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "AppliedJobFragment";
    private static final String JOB_ARGUMENT = "param1";
    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private Submission mSubmission;
    private Callback mCallback;

    public AppliedJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param submission Parameter 1.
     * @return A new instance of fragment JobFragment.
     */
    public static AppliedJobFragment newInstance(Submission submission) {
        AppliedJobFragment fragment = new AppliedJobFragment();
        Bundle args = new Bundle();
        args.putParcelable(JOB_ARGUMENT, submission);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubmission = getArguments().getParcelable(JOB_ARGUMENT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.js_appliedjob, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.js_action_update_submission:
                Resume.getReference().child(mSubmission.getResumeKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Resume resume = dataSnapshot.getValue(Resume.class);
                        mSubmission.setResume(resume);
                        Submission.getReference().child(mSubmission.getKey()).setValue(mSubmission);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled " + databaseError.getMessage());
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_jobdetail_frag, container, false);
        TextView mTitleTextView = (TextView) view.findViewById(R.id.js_job_detail_title);
        TextView mCompanyTextView = (TextView) view.findViewById(R.id.js_job_detail_company);
        TextView mDateTextView = (TextView) view.findViewById(R.id.js_job_detail_date);
        TextView mCityTextView = (TextView) view.findViewById(R.id.js_job_detail_city);
        TextView mDetailsTextView = (TextView) view.findViewById(R.id.js_job_detail_detail);
        Button accpetButon = (Button) view.findViewById(R.id.js_job_detail_button);
        Button rejectButton = (Button) view.findViewById(R.id.js_job_detail_button2);

        Job mJob = mSubmission.getJob();
        mTitleTextView.setText(mJob.getTitle());
        mCompanyTextView.setText(mJob.getCompany());
        mDateTextView.setText(DATAFORMAT.format(mJob.getDate()));
        mCityTextView.setText(mJob.getCity());
        mDetailsTextView.setText(mJob.getDetails());
        if (mSubmission.isOffer()) {
            accpetButon.setVisibility(View.VISIBLE);
            accpetButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifier.notifyAccpetOffer(getContext(), mSubmission);
                }
            });
            rejectButton.setVisibility(View.VISIBLE);
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifier.notifyDeclineOffer(getContext(), mSubmission);
                }
            });
        }
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
    }
}
