package com.fredzqm.jobee.job_seeker;

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
import com.fredzqm.jobee.job_seeker.AppliedJob.AppliedJobFragment;
import com.fredzqm.jobee.job_seeker.Home.HomeFragment;
import com.fredzqm.jobee.job_seeker.JobList.JobDetailFragment;
import com.fredzqm.jobee.job_seeker.JobList.JobListFragment;
import com.fredzqm.jobee.job_seeker.resume.ResumeFragment;
import com.fredzqm.jobee.model.JobSeekerAccount;

public class JobSeekerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ResumeFragment.Callback, HomeFragment.Callback, JobListFragment.Callback,
        AppliedJobFragment.Callback, JobDetailFragment.Callback {
    private static final String TAG = "JobSeekerActivity";

    private JobSeekerAccount mAccount;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainedFragment container = ((ContainedFragment) getSupportFragmentManager().findFragmentById(R.id.js_fragment_container));
                container.clickFab();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.js_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String email = getIntent().getStringExtra(LoginActivity.SIGNIN_EMAIL);
        mAccount = new JobSeekerAccount(email == null ? "" : email);
        if (savedInstanceState == null) {
            swapFragment(HomeFragment.newInstance(), null);
        }
    }

    private void swapFragment(ContainedFragment fragment, String tag) {
        mFab.hide();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.js_fragment_container, fragment);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.js_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStackImmediate();
        }

        switch (item.getItemId()) {
            case R.id.js_nav_home:
                swapFragment(HomeFragment.newInstance(), null);
                break;
            case R.id.js_nav_resume:
                swapFragment(ResumeFragment.newInstance(), null);
                break;
            case R.id.js_nav_joblist:
                swapFragment(JobListFragment.newInstance(), null);
                break;
            case R.id.js_nav_applied:
                swapFragment(AppliedJobFragment.newInstance(2), null);
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
        getSupportFragmentManager().findFragmentById(R.id.js_fragment_container).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public JobSeekerAccount getAccount() {
        return mAccount;
    }

    @Override
    public void showJobDetail(Job job) {
        swapFragment(JobDetailFragment.newInstance(job), "Job Detail");
    }

    @Override
    public FloatingActionButton getFab() {
        return mFab;
    }

}
