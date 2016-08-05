package com.fredzqm.jobee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.model.JobSeeker;
import com.fredzqm.jobee.model.Recruiter;
import com.fredzqm.jobee.recruiter.RecruiterActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.rosehulman.rosefire.Rosefire;
import edu.rosehulman.rosefire.RosefireResult;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements LoaderCallbacks<Cursor>, FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult>, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "LoginActivity";
    public static final String USERID = "USERID";
    public static final String LOGIN_METHOD = "LoginMethod";

    private static final int REQUEST_MAIN_ACTIVITY = 1;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 2;
    private static final int REQUEST_GOOGLE_SIGN_IN = 3;
    private static final int REQUEST_ROSEFIRE_LOGIN = 4;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private FirebaseAuth mAuth;
    private boolean mIsRecruiter;

    private String mLoggedIn = null;
    private String mLoggingMethod = null;
    public static final String EMAIL_LOGIN = "email_login";
    public static final String EMAIL_SIGNUP = "email_signup";
    public static final String GOOGLE_LOGIN = "google";
    public static final String ROSEFIRE_LOGIN = "rose_fire";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Set up the login form.
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Switch mAccoutnTypeSwitch = (Switch) findViewById(R.id.login_account_type_switch);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        SignInButton mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        View rosefireLoginButton = findViewById(R.id.rosefire_sign_in_button);
        rosefireLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithRosefire();
            }
        });

        mAccoutnTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsRecruiter = b;
            }
        });
        populateAutoComplete();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    emailLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin();
            }
        });
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignUp();
            }
        });
        mGoogleSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the title shown.
                // TODO: If you have web page title that matches this app activity's title,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fredzqm.jobee/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the title shown.
                // TODO: If you have web page title that matches this app activity's title,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fredzqm.jobee/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && mLoggedIn == null) {
            final Intent inputIntent;
            if (mIsRecruiter) {
                inputIntent = new Intent(LoginActivity.this, RecruiterActivity.class);
                mLoggedIn = Recruiter.PATH;
            } else {
                inputIntent = new Intent(LoginActivity.this, JobSeekerActivity.class);
                mLoggedIn = JobSeeker.PATH;
            }
            final String loginUserID = user.getUid();
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference userRef = rootRef.child(mLoggingMethod).child(loginUserID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userID = dataSnapshot.getValue(String.class);
                    if (userID == null) {
                        userID = rootRef.child(mLoggedIn).push().getKey();
                        userRef.setValue(userID);
                        userRef.addListenerForSingleValueEvent(this);
                    } else {
                        inputIntent.putExtra(USERID, userID);
                        startActivityForResult(inputIntent, REQUEST_MAIN_ACTIVITY);
                        showProgress(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "ValueEventListener onCancelled", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            switch (mLoggingMethod) {
                case EMAIL_LOGIN:
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    showProgress(false);
                    break;
                case EMAIL_SIGNUP:
                    showLoginError("Sign up failed, account already exist");
                    break;
                case GOOGLE_LOGIN:
                    showLoginError("Google Login failed!");
                    break;
                case ROSEFIRE_LOGIN:
                    showLoginError("Rosefire Login failed!");
                    break;
                default:
                    throw new RuntimeException("not implemented login method " + mLoggingMethod);
            }
            mLoggingMethod = null;
        }else {
            switch (mLoggingMethod) {
                case EMAIL_SIGNUP:
                    showLoginMessage("Successfully create an account");
                    break;
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void emailLogin() {
        if (mLoggingMethod != null)
            return;
        mLoggingMethod = EMAIL_LOGIN;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;
        if (!TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (!email.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
        }
        if (focusView != null) {
            // There was an error; don't attempt login and focus the form field with an error.
            focusView.requestFocus();
            mLoggingMethod = null;
        } else {
            // Show a progress spinner, and perform the user login attempt.
            showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void emailSignUp() {
        if (mLoggingMethod != null)
            return;
        mLoggingMethod = EMAIL_SIGNUP;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;
        if (!TextUtils.isEmpty(password) && password.length() <= 4) {
            // Check for a valid password, if the user entered one.
            //TODO: Replace this with your own password verification logic
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
        } else if (TextUtils.isEmpty(email)) {
            // Check for a valid email address.
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (!email.contains("@")) {
            //TODO: Replace this with your own email verification logic
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
        }
        if (focusView != null) {
            // There was an error; don't attempt login and focus the form field with an error.
            focusView.requestFocus();
            mLoggingMethod = null;
        } else {
            // Show a progress spinner, and perform the user login attempt.
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, this);
        }
    }

    private void loginWithGoogle() {
        if (mLoggingMethod != null)
            return;
        mLoggingMethod = GOOGLE_LOGIN;

        mEmailView.setError(null);
        mPasswordView.setError(null);

        showProgress(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN);
    }

    private void loginWithRosefire() {
        if (mLoggingMethod != null)
            return;
        mLoggingMethod = ROSEFIRE_LOGIN;

        mEmailView.setError(null);
        mPasswordView.setError(null);

        showProgress(true);
        Intent signInIntent = Rosefire.getSignInIntent(this, getString(R.string.rosefire_key));
        startActivityForResult(signInIntent, REQUEST_ROSEFIRE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, this);
            } else {
                showLoginError("Google sign in failed!");
            }
        } else if (requestCode == REQUEST_ROSEFIRE_LOGIN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if (result.isSuccessful()) {
                // The user cancelled the login
                mAuth.signInWithCustomToken(result.getToken())
                        .addOnCompleteListener(this, this);
            } else {
                showLoginError("Rosefire sign in failed!");
            }
        } else if (requestCode == REQUEST_MAIN_ACTIVITY && resultCode == Activity.RESULT_OK) {
            showProgress(false);
            mAuth.signOut();
            mLoggingMethod = null;
            mLoggedIn = null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mLoggingMethod = null;
        showLoginError("Google connection failed");
    }

    // loader interface implementation
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    // auto complete email
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showLoginError(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
        showProgress(false);
        mLoggingMethod = null;
    }

    private void showLoginMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

