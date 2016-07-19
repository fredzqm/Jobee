package com.fredzqm.jobee.job_seeker.resume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.ContainedFragment;
import com.fredzqm.jobee.R;

/**
 * A simple {@link ContainedFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link QRCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRCodeFragment extends ContainedFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Callback mCallback;
    private Object QRCode;

    public QRCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QRCodeFragment.
     */
    public static QRCodeFragment newInstance() {
        QRCodeFragment fragment = new QRCodeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tvStatus;
    TextView tvResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.js_resume_qr_code , container, false);
        HandleClick hc = new HandleClick();
        view.findViewById(R.id.butQR).setOnClickListener(hc);
        view.findViewById(R.id.butProd).setOnClickListener(hc);
        view.findViewById(R.id.butOther).setOnClickListener(hc);
        tvStatus = (TextView)view.findViewById(R.id.tvStatus);
        tvResult = (TextView)view.findViewById(R.id.tvResult);
        return view;
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            switch(arg0.getId()){
                case R.id.butQR:
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    break;
                case R.id.butProd:
                    intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                    break;
                case R.id.butOther:
                    intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
                    break;
            }
            startActivityForResult(intent, 0);	//Barcode Scanner to scan for us
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvStatus.setText("Press a button to start a scan.");
                tvResult.setText("Scan cancelled.");
            }
        }
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
//        void onFragmentInteraction(Uri uri);
    }
}
