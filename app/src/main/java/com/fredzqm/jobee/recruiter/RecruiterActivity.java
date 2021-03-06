package com.fredzqm.jobee.recruiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.JobSeeker;
import com.fredzqm.jobee.model.Recruiter;
import com.fredzqm.jobee.model.Submission;
import com.fredzqm.jobee.notification.Notifier;
import com.fredzqm.jobee.recruiter.JobList.JobFragment;
import com.fredzqm.jobee.recruiter.JobList.JobListFragment;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeListAdapter;
import com.fredzqm.jobee.recruiter.Home.HomeFragment;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeListFragment;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RecruiterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.Callback, ResumeListFragment.Callback, JobListFragment.Callback, JobFragment.Callback, ResumeFragment.Callback {
    public static final String TAG = "JobSeekerActivity";

    private FloatingActionButton mFab;
    private TextView mNavTitleTextView;
    private TextView mNavSmallTextView;

    private ResumeListAdapter mResumeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainedFragment container = ((ContainedFragment) getSupportFragmentManager().findFragmentById(R.id.re_fragment_container));
                container.clickFab();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.re_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        mNavTitleTextView = (TextView) headView.findViewById(R.id.re_nav_text_title);
        mNavSmallTextView = (TextView) headView.findViewById(R.id.re_nav_text_small);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(Notifier.NOTIF_TYPE) != null) {
            String type = intent.getStringExtra(Notifier.NOTIF_TYPE);
            if (type.equals(Notifier.ACCEPT_OFFER) || type.equals(Notifier.DECLINE_OFFER)) {
                swapFragment(ResumeListFragment.newInstance(), null);
                mResumeListAdapter = new ResumeListAdapter(this, intent.getStringExtra(Notifier.SUBMISSION_KEY));
            }
        } else if (savedInstanceState == null) {
            swapFragment(HomeFragment.newInstance(), null);
        }
        DatabaseReference mRef = JobSeeker.getRefernce().child(getUserID());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecruiter = dataSnapshot.getValue(Recruiter.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled " + databaseError);
            }
        });
    }

    private void swapFragment(ContainedFragment fragment, String tag) {
        mFab.hide();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.re_fragment_container, fragment);
        if (tag != null)
            ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login_action_logout) {
            Intent intent = this.getIntent();
            this.setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStackImmediate();
        }

        switch (item.getItemId()) {
            case R.id.re_nav_home:
                swapFragment(HomeFragment.newInstance(), null);
                break;
            case R.id.re_nav_resume:
                swapFragment(ResumeListFragment.newInstance(), null);
                break;
            case R.id.re_nav_joblist:
                swapFragment(JobListFragment.newInstance(), null);
                break;
            default:
                throw new RuntimeException("Not implemented navigation bar yet");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().findFragmentById(R.id.re_fragment_container).onActivityResult(requestCode, resultCode, data);
    }

    // for home fragment
    private Recruiter mRecruiter;

    @Override
    public Recruiter getRecruiter() {
        return mRecruiter;
    }

    @Override
    public void updateAccount(Recruiter recruiter) {
        mRecruiter = recruiter;
        mNavTitleTextView.setText(getString(R.string.hello, recruiter.getName()));
    }


    @Override
    public void showJobDetail(String jobKey) {
        swapFragment(JobFragment.newInstance(jobKey), "edit job");
    }

    @Override
    public void showResumeDetail(Submission resume) {
        swapFragment(ResumeFragment.newInstance(resume), "edit job");
    }

    @Override
    public ResumeListAdapter getResumeListAdapter() {
        if (mResumeListAdapter == null)
            mResumeListAdapter = new ResumeListAdapter(this);
        return mResumeListAdapter;
    }

    @Override
    public FloatingActionButton getFab() {
        return mFab;
    }

    @Override
    public String getUserID() {
        return LoginActivity.getUserID();
    }


    @Override
    public void showNext() {
        getSupportFragmentManager().popBackStackImmediate();
        mResumeListAdapter.showNext();
    }


}
