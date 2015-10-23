package com.github.socialc0de.gsw.android.tools;


import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import com.github.socialc0de.gsw.android.AppConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author livin
 */
public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("test");
        // TODO Auto-generated constructor stub
    }

    public FetchAddressIntentService(String name) {
        super("Sample");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        String errorMessage = "";
        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                AppConfig.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(
                AppConfig.RECEIVER);
        // Get the location passed to this service through an extra.
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            deliverResultToReceiver(AppConfig.FAILURE_RESULT, errorMessage);
            //ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            deliverResultToReceiver(AppConfig.FAILURE_RESULT, errorMessage);
            //illegalArgumentException.printStackTrace();
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            deliverResultToReceiver(AppConfig.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            deliverResultToReceiver(AppConfig.SUCCESS_RESULT, TextUtils.join(
                    System.getProperty("line.separator"), addressFragments));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
