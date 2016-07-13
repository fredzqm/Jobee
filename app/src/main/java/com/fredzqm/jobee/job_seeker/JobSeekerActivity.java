package com.fredzqm.jobee.job_seeker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.AppliedJob.AppliedJobFragment;
import com.fredzqm.jobee.job_seeker.Home.HomeFragment;
import com.fredzqm.jobee.job_seeker.JobList.JobListFragment;
import com.fredzqm.jobee.job_seeker.resume.ResumeFragment;

public class JobSeekerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ResumeFragment.Callback, HomeFragment.Callback, JobListFragment.Callback, AppliedJobFragment.Callback{
    private static final String TAG = "JobSeekerActivity";

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainedFragment container = ((ContainedFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_resume));
                container.clickFab();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAccount = new Account(getIntent().getStringExtra(LoginActivity.SIGNIN_EMAIL));
        if (savedInstanceState == null){
            swapFragment(HomeFragment.newInstance(mAccount));
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.js_nav_home:
                swapFragment(HomeFragment.newInstance(mAccount));
                break;
            case R.id.js_nav_resume:
                swapFragment(ResumeFragment.newInstance(mAccount.getEmailAccount()));
                break;
            case R.id.js_nav_job_list:
                swapFragment(JobListFragment.newInstance(mAccount.getEmailAccount()));
                break;
            case R.id.js_nav_applied:
                swapFragment(AppliedJobFragment.newInstance(10));
                break;
            default:
                Log.d(TAG, "Not implemented navigation bar yet");
                return  true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void swapFragment(ContainedFragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_resume, fragment);
        ft.commit();
    }

    @Override
    public void saveAccountUpdates(String name, String email, String address) {
        mAccount.setName(name);
        mAccount.setEmailAccount(email);
        mAccount.setAddress(address);
    }

}
