package com.fredzqm.jobee.recruiter.ResumeList.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.Resume;

import java.util.ArrayList;

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

    private RecyclerView mRecyclerView;
    private ResumeAdapter mResumeAdapter;
    private ArrayAdapter<String> mSwitchAdapter;

    private ArrayList<Resume> mResumes;


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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.js_resume, menu);
        MenuItem item = menu.findItem(R.id.action_switch);
        final MenuItem menuItem = item.setActionView(R.layout.resume_switch_list);
        final Spinner spinner = (Spinner) item.getActionView().findViewById(R.id.resume_switch_spiner);
        mSwitchAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, android.R.id.text1){
            public int getCount(){
                return mResumes.size() + 1;
            }

            public String getItem(int position){
                if (position == mResumes.size()){
                    return "Create new";
                }
                return mResumes.get(position).getName();
            }
        };
        spinner.setAdapter(mSwitchAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if (position == mResumes.size()){
                    final EditText editText = new EditText(getContext());
                    editText.setHint("Resume name");
                    editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    new AlertDialog.Builder(getContext())
                            .setView(editText)
                            .setTitle("Create new resume")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Resume created = Resume.newInstance(editText.getText().toString());
                                    mResumes.add(created);
                                    mResumeAdapter.setResume(created);
                                    mSwitchAdapter.notifyDataSetChanged();
                                }
                            })
                            .show();
                }else{
                    mResumeAdapter.setResume(mResumes.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(USER_NAME);
        }
        setHasOptionsMenu(true);
        mResumeAdapter = new ResumeAdapter(getContext());
        // TODO: replace those with resume from database
        Resume resume = Resume.newInstance("Resume 1");
        mResumeAdapter.setResume(resume);
        mResumes = new ArrayList<>();
        mResumes.add(resume);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recyclerview, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mResumeAdapter);
        mRecyclerView.setHasFixedSize(false);

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
        mResumeAdapter.addCategory("New category");
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
