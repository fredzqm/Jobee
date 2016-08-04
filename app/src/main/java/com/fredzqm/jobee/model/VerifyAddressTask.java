package com.fredzqm.jobee.model;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.IOException;
import java.util.List;

public class VerifyAddressTask extends AsyncTask<Void, Integer, Address> {
    private String mAddress;
    private String message = "Update to verified address";
    private Fragment mContext;
    private Callback mCallback;

    public VerifyAddressTask(Fragment context, String address, Callback callback) {
        mContext = context;
        mCallback = callback;
        mAddress = address;
    }

    protected Address doInBackground(Void... x) {
        Geocoder geo = new Geocoder(mContext.getContext());
        try {
            List<Address> addrls = geo.getFromLocationName(mAddress, 1);
            if (addrls.size() >= 1)
                return addrls.get(0);
            message = "invalid address";
        } catch (IOException e) {
            message = "No internet connection, cannot verify address";
        } catch (IllegalArgumentException e) {
            message = "address can't be empty";
        }
        return null;
    }

    protected void onPostExecute(Address result) {
        if (result != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.getMaxAddressLineIndex(); i++) {
                sb.append(result.getAddressLine(i) + "\n");
            }
            sb.delete(sb.length() - 1, sb.length());
            mCallback.verifiedResult(sb.toString());
        }
        Snackbar.make(mContext.getView(), message, Snackbar.LENGTH_LONG)
                .setAction("Insist", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.insist(message);
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    public interface Callback {
        void verifiedResult(String verifiedAddress);

        void insist(String oldAddress);
    }
}