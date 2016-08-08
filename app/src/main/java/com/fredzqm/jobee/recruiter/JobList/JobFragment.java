package com.fredzqm.jobee.recruiter.JobList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.model.Submission;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends ContainedFragment implements View.OnClickListener, ValueEventListener {
    private static final String TAG = "JobFragment";
    private static final String JOB_ARGUMENT = "jobKey";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private Job mJob;
    private Callback mCallback;

    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mCityTextView;
    private TextView mDetailsTextView;
    private boolean mEditing = false;
    private DatabaseReference mRef;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jobID Parameter 1.
     * @return A new instance of fragment JobFragment.
     */
    public static JobFragment newInstance(String jobID) {
        JobFragment fragment = new JobFragment();
        Bundle args = new Bundle();
        args.putString(JOB_ARGUMENT, jobID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jobKey = getArguments().getString(JOB_ARGUMENT);
        mRef = Job.getRefernce().child(jobKey);
        mRef.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.re_jobdetail_frag, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.re_job_detail_title);
        mDateTextView = (TextView) view.findViewById(R.id.re_job_detail_date);
        mCityTextView = (TextView) view.findViewById(R.id.re_job_detail_city);
        mDetailsTextView = (TextView) view.findViewById(R.id.re_job_detail_detail);

        mTitleTextView.setOnClickListener(this);
        mDateTextView.setOnClickListener(this);
        mCityTextView.setOnClickListener(this);
        mDetailsTextView.setOnClickListener(this);

        mCallback.getFab().show();
        
        return view;
    }


    @Override
    public void onClick(final View view) {
        mEditing = true;
        final int position = view.getId();
        if (view == mDateTextView) { // detail in the list
            DialogFragment newFragment = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the current date as the default date in the picker
                    final Calendar c = Calendar.getInstance();
                    c.setTime(mJob.getDate());
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    // Create a new instance of DatePickerDialog and return it
                    return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Date newDate = new Date(year - 1900, month, day);
                            mJob.setDate(newDate);
                            mRef.setValue(mJob);
                        }
                    }, year, month, day);
                }
            };
            newFragment.show(getFragmentManager(), "timePicker");
        } else {
            final EditText editText = new EditText(getContext());
            String title;
            if (view == mTitleTextView) {
                editText.setText(mJob.getTitle());
                title = "Edit job title";
            } else if (view == mCityTextView) {
                editText.setHint(mJob.getCity());
                title = "Edit city";
            } else if (view == mDetailsTextView) {
                editText.setHint(mJob.getDetails());
                title = "Edit detail";
            } else {
                throw new RuntimeException("not implemented!");
            }
            new AlertDialog.Builder(getContext())
                    .setView(editText)
                    .setTitle(title)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = editText.getText().toString();
                            if (view == mTitleTextView) {
                                mJob.setTitle(str);
                            } else if (view == mCityTextView) {
                                mJob.setCity(str);
                            } else if (view == mDetailsTextView){
                                mJob.setDetails(str);
                            }
                            mRef.setValue(mJob);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            mEditing = false;
                        }
                    })
                    .show();
        }

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
        mJob = dataSnapshot.getValue(Job.class);
        mJob.setKey(dataSnapshot.getKey());
        mTitleTextView.setText(mJob.getTitle());
        mDateTextView.setText(DATE_FORMAT.format(mJob.getDate()));
        mCityTextView.setText(mJob.getCity());
        mDetailsTextView.setText(mJob.getDetails());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled " + databaseError.getMessage());
    }

    @Override
    public void clickFab() {
        IntentIntegrator integrator = new IntentIntegrator((Activity) mCallback);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText((Context) mCallback, R.string.resume_scan_cancelled, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Context) mCallback, R.string.scan_resume_toast, Toast.LENGTH_LONG).show();
              Submission.handleNewSubmission(mJob, result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        FloatingActionButton getFab();
    }
}
