package com.fredzqm.jobee.recruiter.ResumeList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.model.ResumeCategory;
import com.fredzqm.jobee.model.Submission;
import com.fredzqm.jobee.notification.Notifier;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumeFragment extends ContainedFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ResumeFragment";
    private static final String SUBMISSION = "SUBMISSION";

    private Submission mSubmission;
    private LinearLayout mLinearLayout;

    public ResumeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param submission Parameter 1.
     * @return A new instance of fragment ResumeFragment.
     */
    public static ResumeFragment newInstance(Submission submission) {
        ResumeFragment fragment = new ResumeFragment();
        Bundle args = new Bundle();
        args.putParcelable(SUBMISSION, submission);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubmission = getArguments().getParcelable(SUBMISSION);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int x = (int) getResources().getDimension(R.dimen.card_view_margin);
        layoutParams.setMargins(x, x, x, x);
        mLinearLayout.setLayoutParams(layoutParams);

        Resume mResume = mSubmission.getResume();
        for (int i = 0; i < mResume.size(); i++) {
            ResumeCategory resumeCategory = mResume.get(i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.js_resume_category, mLinearLayout, false);
            TextView mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
            LinearLayout mListView = (LinearLayout) itemView.findViewById(R.id.listviewTasks);
            mTypeTextView.setText(resumeCategory.getType());
            mListView.removeAllViews();
            for (int j = 0; j < resumeCategory.size(); j++) {
                String s = resumeCategory.get(j);
                TextView tv = new TextView(getContext());
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setId(mListView.getChildCount());
                tv.setTextSize(20);
                tv.setText(s);
                mListView.addView(tv);
            }
            mLinearLayout.addView(itemView);
            Log.d(TAG, "" + resumeCategory);
        }
        return mLinearLayout;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.re_resume, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText editText = new EditText(getContext());
        DatabaseReference ref = Submission.getReference().child(mSubmission.getKey());
        String jobSeekerKey = mSubmission.getJobSeekerKey();
        switch (item.getItemId()) {
            case R.id.re_action_decide_later:
                showNext();
                break;
            case R.id.re_action_offer:
                mSubmission.setStatus(Submission.OFFERED);
                Notifier.notify(jobSeekerKey, "Offer", "offer");
                ref.setValue(mSubmission);
                break;
            case R.id.re_action_weed_out:
                mSubmission.setStatus(Submission.REJECTED);
                Notifier.notify(jobSeekerKey, "Reject", "Sorry, ... rejected you");
                ref.setValue(mSubmission);
                break;
            case R.id.re_action_schedule_interview:
                break;
            default:
                throw new RuntimeException("Not implemented");
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNext() {
        getFragmentManager().popBackStack();
    }
}
