package com.fredzqm.jobee.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.fredzqm.jobee.R;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";
    private Callback mCallback;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        final Switch accountTypeSwitch = (Switch) view.findViewById(R.id.login_account_type_switch);
        final EditText userNameEditText = (EditText) view.findViewById(R.id.signup_username_edit_text);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.signup_password_edit_text);
        final EditText repeatEditText = (EditText) view.findViewById(R.id.signup_repeat_password_edit_text);

        Button signUpButton = (Button) view.findViewById(R.id.signup_button_sign_up);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = passwordEditText.getText().toString();
                String b = repeatEditText.getText().toString();
                if (a!= null && a.equals(b))
                    mCallback.signUp(userNameEditText.getText().toString(), a, accountTypeSwitch.isChecked());
                else
                    Log.d(TAG, "password does not match");
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        void signUp(String userName, String password, boolean isRecruiter);
    }

}
