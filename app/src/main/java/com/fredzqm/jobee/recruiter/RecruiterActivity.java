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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Recruiter;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.recruiter.JobList.JobFragment;
import com.fredzqm.jobee.recruiter.JobList.JobListFragment;
import com.fredzqm.jobee.recruiter.ScheduledInterview.InterviewFragment;
import com.fredzqm.jobee.recruiter.Home.HomeFragment;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeListFragment;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeFragment;

public class RecruiterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.Callback, ResumeListFragment.Callback, JobListFragment.Callback,
        InterviewFragment.Callback, JobFragment.Callback
{
    public static final String TAG = "JobSeekerActivity";
    public static final String PATH = "recruiter";

    private FloatingActionButton mFab;
    private String userID;

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

        userID = getIntent().getStringExtra(LoginActivity.USERID);
        if (savedInstanceState == null){
            swapFragment(HomeFragment.newInstance(), null);
        }
    }

    private void swapFragment(ContainedFragment fragment, String tag){
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
            finish();finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i ++){
            fm.popBackStackImmediate();
        }

        switch (item.getItemId()){
            case R.id.re_nav_home:
                swapFragment(HomeFragment.newInstance(), null);
                break;
            case R.id.re_nav_resume:
                swapFragment(ResumeListFragment.newInstance(), null);
                break;
            case R.id.re_nav_joblist:
                swapFragment(JobListFragment.newInstance(), null);
                break;
            case R.id.re_nav_interview:
                swapFragment(InterviewFragment.newInstance(2), null);
                break;
            default:
                throw new RuntimeException("Not implemented navigation bar yet");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showJobDetail(Job job) {
        swapFragment(JobFragment.newInstance(job), "edit job");
    }

    @Override
    public void showResumeDetail(Resume resume) {
        swapFragment(ResumeFragment.newInstance(resume), "edit job");
    }

    public FloatingActionButton getFab(){
        return mFab;
    }

    @Override
    public String getUserID() {
        return userID;
    }
}
