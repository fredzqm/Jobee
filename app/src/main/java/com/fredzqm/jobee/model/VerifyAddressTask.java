package com.fredzqm.jobee.model;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fredzqm.jobee.R;

import java.io.IOException;
import java.util.List;

public class VerifyAddressTask extends AsyncTask<Void, Integer, Address> {
    private Fragment mContext;
    private Callback mCallback;
    private String mAddress;
    private int message = R.string.get_verified_address;

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
            message = R.string.invalid_address;
        } catch (IOException e) {
            message = R.string.no_internet;
        } catch (IllegalArgumentException e) {
            message = R.string.empty_address;
        }
        return null;
    }

    protected void onPostExecute(Address result) {
        if (result != null ) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.getMaxAddressLineIndex(); i++) {
                sb.append(result.getAddressLine(i) + "\n");
            }
            if (sb.length() > 0)
                sb.delete(sb.length() - 1, sb.length());
            mCallback.verifiedResult(sb.toString());
        }
        Snackbar.make(mContext.getView(), message, Snackbar.LENGTH_LONG)
            .setAction("Insist", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.insist(mAddress);
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