package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.ContainedFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link ResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumeFragment extends ContainedFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ResumeFragment";
    private static final String USER_NAME = "USER_NAME";

    private String mUserName;

    private Callback mCallback;
    private ItemAdapter mSkillAdapter;
    private ItemAdapter mExperienceAdpater;
    private RecyclerView mSkillRecyclerView;
    private RecyclerView mExperinceRecyclerView;

    public ResumeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userName Parameter 1.
     * @return A new instance of fragment ResumeFragment.
     */
    public static ResumeFragment newInstance(String userName) {
        ResumeFragment fragment = new ResumeFragment();
        Bundle args = new Bundle();
        args.putString(USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_fragment_resume, container, false);
        EditText emailEditText = (EditText) view.findViewById(R.id.resume_email);
        emailEditText.setText(mUserName);

        mSkillRecyclerView = (RecyclerView) view.findViewById(R.id.resume_skill_list);
        mSkillAdapter = setItemAdapter(mSkillRecyclerView);

        mExperinceRecyclerView = (RecyclerView) view.findViewById(R.id.resume_experience_list);
        mExperienceAdpater = setItemAdapter(mExperinceRecyclerView);

        return view;
    }

    private ItemAdapter setItemAdapter(RecyclerView recyclerView){
        ItemAdapter adapter = new ItemAdapter(getContext(), recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        return adapter;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void clickFab() {
        mSkillAdapter.addName();
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