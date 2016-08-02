package com.fredzqm.jobee.recruiter.JobList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Job;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends ContainedFragment {
    private static final String JOB_ARGUMENT = "param1";

    private Job mJob;
    private Callback mCallback;

    private TextView mTitleTextView;
    private TextView mCompanyTextView;
    private TextView mDetailsTextView;
    private TextView mDateTextView;
    private TextView mCityTextView;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param job Parameter 1.
     * @return A new instance of fragment JobFragment.
     */
    public static JobFragment newInstance(Job job) {
        JobFragment fragment = new JobFragment();
        Bundle args = new Bundle();
        args.putParcelable(JOB_ARGUMENT, job);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mJob = getArguments().getParcelable(JOB_ARGUMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_jobdetail_frag, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.js_job_detail_title);
        mCompanyTextView = (TextView) view.findViewById(R.id.js_job_detail_company);
        mDateTextView = (TextView) view.findViewById(R.id.js_job_detail_date);
        mCityTextView = (TextView) view.findViewById(R.id.js_job_detail_city);
        mDetailsTextView = (TextView) view.findViewById(R.id.js_job_detail_detail);

        mTitleTextView.setText(mJob.getTitle());
        mCompanyTextView.setText(mJob.getCompany());
        mDateTextView.setText((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(mJob.getDate()));
        mCityTextView.setText(mJob.getCity());
        mDetailsTextView.setText(mJob.getDetails());

        mTitleTextView.setText(mJob.getTitle());
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
    }
}
