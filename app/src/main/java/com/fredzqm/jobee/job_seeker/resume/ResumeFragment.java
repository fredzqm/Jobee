package com.fredzqm.jobee.job_seeker.resume;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
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
import android.widget.Toast;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.fredzqm.jobee.model.Resume;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link ResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumeFragment extends ContainedFragment implements ChildEventListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "ResumeFragment";
    public static final String PATH = "resumes";

    private Callback mCallback;

    private RecyclerView mRecyclerView;
    private ResumeAdapter mResumeAdapter;

    private Spinner mSpinner;
    private ArrayAdapter<String> mSwitchAdapter;

    private ArrayList<Resume> mResumes;
    private int curIndex;
    private DatabaseReference mRef;


    public ResumeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResumeFragment.
     */
    public static ResumeFragment newInstance() {
        ResumeFragment fragment = new ResumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mResumes = new ArrayList<>();
        mResumeAdapter = new ResumeAdapter(getContext());
        mRef = FirebaseDatabase.getInstance().getReference()
                .child(JobSeekerActivity.PATH).child(mCallback.getUserID()).child(PATH);
        mRef.addChildEventListener(this);
        mRef.push().setValue(Resume.newInstance("first resume"));
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
        mCallback.getFab().show();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.js_resume, menu);
        MenuItem item = menu.findItem(R.id.js_action_switch);
        item.setActionView(R.layout.resume_switch_list);
        mSpinner = (Spinner) item.getActionView().findViewById(R.id.resume_switch_spiner);
        mSwitchAdapter = new ArrayAdapter<String>(getContext(), R.layout.resume_switch_list_content, R.id.resume_name) {
            public int getCount() {
                return mResumes.size();
            }
            public String getItem(int position) {
                return mResumes.get(position).getResumeName();
            }
        };
        mSwitchAdapter.setDropDownViewResource(R.layout.resume_switch_list_content);
        mSpinner.setAdapter(mSwitchAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchTo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final EditText editText = new EditText(getContext());
        switch (item.getItemId()) {
            case R.id.js_action_logout:
                break;
            case R.id.js_action_add_category:
                editText.setHint("Category");
                editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                new AlertDialog.Builder(getContext())
                        .setView(editText)
                        .setTitle("Add new Category")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mResumeAdapter.addCategory(editText.getText().toString());
                            }
                        })
                        .show();
                break;
            case R.id.js_action_delete:
                mRef.child(mResumes.get(curIndex).getKey()).removeValue();
//                mResumes.remove(mResumeAdapter.getResume());
//                if (mResumes.isEmpty()) {
//                    mResumes.add(Resume.newInstance("Resume 1"));
//                }
//                switchTo(0);
                break;
            case R.id.js_action_add_resume:
                editText.setHint("Resume name");
                editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                new AlertDialog.Builder(getContext())
                        .setView(editText)
                        .setTitle("Create new resume")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Resume created = Resume.newInstance(editText.getText().toString());
                                mRef.push().setValue(created);
//                                mResumes.add(created);
//                                switchTo(mResumes.size() - 1);
                            }
                        })
                        .show();
                break;
            default:
                throw new RuntimeException("Not implemented");
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchTo(int index) {
        curIndex = index;
        mResumeAdapter.setResume(mResumes.get(index), mRef);
        mResumeAdapter.notifyDataSetChanged();
        mSwitchAdapter.notifyDataSetChanged();
        mSpinner.setSelection(index);
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        Resume resume = dataSnapshot.getValue(Resume.class);
        resume.setKey(key);
        mResumes.add(resume);
        switchTo(mResumes.size() - 1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        Resume changedTo = dataSnapshot.getValue(Resume.class);
        for (int i = 0; i < mResumes.size(); i++) {
            Resume r = mResumes.get(i);
            if (key.equals(r.getKey())) {
                mResumes.set(i, changedTo);
                if (i == curIndex) {
                    switchTo(i);
                } else {
                    mSwitchAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        Resume changedTo = dataSnapshot.getValue(Resume.class);
        for (int i = 0; i < mResumes.size(); i++) {
            Resume r = mResumes.get(i);
            if (key.equals(r.getKey())) {
                mResumes.remove(i);
                if (i == curIndex) {
                    switchTo(0);
                } else {
                    mSwitchAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
                Toast.makeText((Context) mCallback, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Context) mCallback, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        FloatingActionButton getFab();

        String getUserID();
    }
}
