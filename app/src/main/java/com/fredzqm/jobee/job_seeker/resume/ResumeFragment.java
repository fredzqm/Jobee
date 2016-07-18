package com.fredzqm.jobee.job_seeker.resume;

import android.content.ClipData;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.ContainedFragment;

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
    private TextView mResumeNameTextView;
    private ResumeAdapter mContentAdapter;
    private ArrayAdapter<Resume> mResumes;
    private Resume mCurResume;

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
        Spinner spinner = (Spinner) item.getActionView().findViewById(R.id.resume_switch_spiner);
        spinner.setAdapter(mResumes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_add:
                final EditText editText = new EditText(getContext());
                editText.setHint("Category");
                editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                new AlertDialog.Builder(getContext())
                        .setView(editText)
                        .setTitle("Add new Category")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mContentAdapter.addCategory(editText.getText().toString());
                            }
                        })
                        .show();
                break;
            default:
                throw new RuntimeException("Not implemented");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(USER_NAME);
        }
        setHasOptionsMenu(true);
        mCurResume = Resume.newResume("Resume 1");
        mResumes = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        mResumes.add(mCurResume);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.js_resume_frag, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.resume_contents_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mContentAdapter = new ResumeAdapter(getContext(), Resume.newResume("Resume1"));
        mRecyclerView.setAdapter(mContentAdapter);
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
        mContentAdapter.addCategory("New category");
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
