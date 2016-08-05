package com.fredzqm.jobee.recruiter.ResumeList;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Resume;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callback}
 * interface.
 */
public class ResumeListFragment extends ContainedFragment {

    private Callback mListener;

    public static ResumeListFragment newInstance() {
        ResumeListFragment fragment = new ResumeListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResumeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // TODO: connect to firebase
        List<Resume> ITEMS = new ArrayList<>();
        ITEMS.add(Resume.newInstance("a"));
        ITEMS.add(Resume.newInstance("b"));
        ITEMS.add(Resume.newInstance("c"));
        recyclerView.setAdapter(new ResumeListAdapter(ITEMS, mListener));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void showResumeDetail(Resume mItem);
    }
}
