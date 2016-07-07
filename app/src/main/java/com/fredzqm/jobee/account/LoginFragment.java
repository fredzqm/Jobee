package com.fredzqm.jobee.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class LoginFragment extends Fragment {
    private Callback mCallback;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Switch accountTypeSwitch = (Switch) view.findViewById(R.id.login_account_type_switch);
        final EditText userNameEditText = (EditText) view.findViewById(R.id.login_username_edit_text);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.login_password_edit_text);

        final Button loginButton = (Button) view.findViewById(R.id.login_button_login_in);
        Button signUpButton = (Button) view.findViewById(R.id.login_button_sign_up);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.login(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.signUp();
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
        void login(String userName, String password);
        void signUp();
        // TODO: Update argument type and name
    }

}
