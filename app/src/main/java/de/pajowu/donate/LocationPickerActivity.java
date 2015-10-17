package de.pajowu.donate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import java.lang.String;
import org.json.JSONArray;
import org.json.JSONObject;

import de.pajowu.donate.tools.FetchAddressIntentService;
import de.pajowu.donate.tools.GPSTracker;
import de.pajowu.donate.tools.ServerConnector;

/**
 * @author livin
 */
public class LocationPickerActivity extends ActionBarActivity implements
        OnMapClickListener, OnMapLongClickListener {
    public static boolean mMapIsTouched = false;
    public String userLocation = "";
    Toolbar toolbar;
    GoogleMap mMap;
    GPSTracker gps;
    LatLng userSelectedLatLng;
    ProgressDialog progress_dialogue;
    ImageView iv_clear;
    AutoCompleteTextView autoCompView;
    ArrayAdapter<String> adapter;
    private AddressResultReceiver mResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        init();
        getData();

    }

    //Receives the lat and lon points to in the map
    public void getData() {
        double lat = getIntent().getDoubleExtra("lat", 0);
        double lon = getIntent().getDoubleExtra("lon", 0);
        if (lat == 0 && lon == 0) {
            showCurrentLocation();
        } else {
            showPreviouslySelectedLocation(lat, lon);
        }
    }

    //Intialises the fields
    public void init() {
        progress_dialogue = new ProgressDialog(this);
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCommon)).getMap();
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);

        gps = new GPSTracker(this);
        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleRegion visibleRegion = mMap.getProjection()
                        .getVisibleRegion();

                Point x = mMap.getProjection().toScreenLocation(
                        visibleRegion.farRight);

                Point y = mMap.getProjection().toScreenLocation(
                        visibleRegion.nearLeft);

                Point centerPoint = new Point(x.x / 2, y.y / 2);

                LatLng centerFromPoint = mMap.getProjection().fromScreenLocation(
                        centerPoint);

                userSelectedLatLng = centerFromPoint;
                Intent resultIntent = new Intent();
                LatLng topRight = visibleRegion.farRight;
                LatLng bottomLeft = visibleRegion.nearLeft;
                String bbox = Double.toString(bottomLeft.longitude) + ",";
                bbox += Double.toString(bottomLeft.latitude) + ",";
                bbox += Double.toString(topRight.longitude) + ",";
                bbox += Double.toString(topRight.latitude);
                Log.d("MainActivity",bbox);
                resultIntent.putExtra(AppConfig.BBOX,
                        bbox);
                resultIntent.putExtra(AppConfig.USER_LAT,
                        userSelectedLatLng.latitude);
                resultIntent.putExtra(AppConfig.USER_LNG,
                        userSelectedLatLng.longitude);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        gps = new GPSTracker(this);
    }

    /**
     * Map points the current user location
     */
    public void showCurrentLocation() {
        if (gps.canGetLocation()) {
            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
            userSelectedLatLng = latLng;
            // mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            new GetLocationTask().execute(0);
        } else {
            showSettingsAlert();
        }
    }

    /**
     * Moves the map to the given lat lon points, usefull if you wanted to point
     * any particular place
     *
     * @param lat
     * @param lon
     */
    public void showPreviouslySelectedLocation(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);
        userSelectedLatLng = latLng;
        // mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        new GetLocationTask().execute(0);
    }

    /**
     * GPS Settings alert
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LocationPickerActivity.this);
        alertDialog.setTitle("Location Settings");
        alertDialog
                .setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        LocationPickerActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.get_selected_location, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent resultIntent = new Intent();
        if (item.getItemId() == android.R.id.home) {
            VisibleRegion visibleRegion = mMap.getProjection()
                    .getVisibleRegion();
            LatLng topRight = visibleRegion.farRight;
            LatLng bottomLeft = visibleRegion.nearLeft;
            String bbox = Double.toString(bottomLeft.longitude) + ",";
            bbox += Double.toString(bottomLeft.latitude) + ",";
            bbox += Double.toString(topRight.longitude) + ",";
            bbox += Double.toString(topRight.latitude);
            Log.d("MainActivity",bbox);
            resultIntent.putExtra(AppConfig.BBOX,
                    bbox);
            resultIntent.putExtra(AppConfig.USER_LAT,
                    userSelectedLatLng.latitude);
            resultIntent.putExtra(AppConfig.USER_LNG,
                    userSelectedLatLng.longitude);

            resultIntent.putExtra(AppConfig.USER_LOCATION, userLocation);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_done) {
            //Gets the centre point of the map displayed
            VisibleRegion visibleRegion = mMap.getProjection()
                    .getVisibleRegion();

            Point x = mMap.getProjection().toScreenLocation(
                    visibleRegion.farRight);

            Point y = mMap.getProjection().toScreenLocation(
                    visibleRegion.nearLeft);

            Point centerPoint = new Point(x.x / 2, y.y / 2);

            LatLng centerFromPoint = mMap.getProjection().fromScreenLocation(
                    centerPoint);

            userSelectedLatLng = centerFromPoint;

            LatLng topRight = visibleRegion.farRight;
            LatLng bottomLeft = visibleRegion.nearLeft;
            String bbox = Double.toString(bottomLeft.longitude) + ",";
            bbox += Double.toString(bottomLeft.latitude) + ",";
            bbox += Double.toString(topRight.longitude) + ",";
            bbox += Double.toString(topRight.latitude);
            resultIntent.putExtra(AppConfig.USER_LAT,
                    userSelectedLatLng.latitude);
            resultIntent.putExtra(AppConfig.USER_LNG,
                    userSelectedLatLng.longitude);
            resultIntent.putExtra(AppConfig.BBOX,
                    bbox);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Statrs intnet service to get address from geocoder
     */
    protected void startIntentService() {
        // Get address from geocoder
        mResultReceiver = new AddressResultReceiver(new Handler());
        Location targetLocation = new Location("");// provider name is
        // unecessary
        targetLocation.setLatitude(userSelectedLatLng.latitude);// your coords
        // of course
        targetLocation.setLongitude(userSelectedLatLng.longitude);
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConfig.RECEIVER, mResultReceiver);
        intent.putExtra(AppConfig.LOCATION_DATA_EXTRA, targetLocation);
        startService(intent);
    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // TODO Auto-generated method stub
        userSelectedLatLng = latLng;
        mMap.clear();
        // mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        new GetLocationTask().execute(0);

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            // mAddressOutput = resultData.getString(AppConfig.RESULT_DATA_KEY);
            // displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppConfig.SUCCESS_RESULT) {
                if (progress_dialogue.isShowing()) {
                    progress_dialogue.dismiss();
                }
                userLocation = resultData.getString(AppConfig.RESULT_DATA_KEY);
                autoCompView.setText(userLocation);
                // finish();
                // appConfig.showToast(resultData.toString());
            } else {
                // Get location from google api if geocoder is not responding
                new GetLocationTask().execute(0);
            }
        }
    }

    /**
     * Process the Put Message REQUEST TO THE SERVER
     */
    public class GetLocationTask extends
            AsyncTask<Integer, JSONObject, JSONObject> {
        int whichTask = 0;

        @Override
        protected JSONObject doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            whichTask = params[0];
            return ServerConnector
                    .getJSONObjectfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + userSelectedLatLng.latitude
                            + ","
                            + userSelectedLatLng.longitude + "&sensor=false");

        }

        @Override
        protected void onPostExecute(JSONObject jsonObj) {
            // TODO Auto-generated method stub
            super.onPostExecute(jsonObj);
            if (progress_dialogue.isShowing()) {
                progress_dialogue.dismiss();
            }
            if (jsonObj != null && !jsonObj.equals("")) {
                try {

                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(AppConfig.USER_LAT,
                                userSelectedLatLng.latitude);
                        resultIntent.putExtra(AppConfig.USER_LNG,
                                userSelectedLatLng.longitude);
                        resultIntent.putExtra(AppConfig.USER_LOCATION,
                                zero.getString("formatted_address"));
                        setResult(Activity.RESULT_OK, resultIntent);
                        userLocation = zero.getString("formatted_address");
                        //autoCompView.setText(userLocation);
                        // finish();

                        if (whichTask == 1) {
                            VisibleRegion visibleRegion = mMap.getProjection()
                                    .getVisibleRegion();
                            LatLng topRight = visibleRegion.farRight;
                            LatLng bottomLeft = visibleRegion.nearLeft;
                            String bbox = Double.toString(bottomLeft.longitude) + ",";
                            bbox += Double.toString(bottomLeft.latitude) + ",";
                            bbox += Double.toString(topRight.longitude) + ",";
                            bbox += Double.toString(topRight.latitude);
                            Log.d("MainActivity",bbox);
                            resultIntent.putExtra(AppConfig.BBOX,
                                    bbox);
                            resultIntent.putExtra(AppConfig.USER_LAT,
                                    userSelectedLatLng.latitude);
                            resultIntent.putExtra(AppConfig.USER_LNG,
                                    userSelectedLatLng.longitude);
                            resultIntent.putExtra(AppConfig.USER_LOCATION,
                                    userLocation);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else {
                Toast.makeText(
                        LocationPickerActivity.this,
                        "Some error occured. Make sure that you are connected to internet.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    /**
     * Getting google place suggestions
     *
     * @author livin
     *
     */
    /*class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		// three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args) {

			ArrayList<String> predictionsArr = new ArrayList<String>();

			try {

				URL googlePlaces = new URL(
						// URLEncoder.encode(url,"UTF-8");
						"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
								+ URLEncoder.encode(args[0].toString(), "UTF-8")
								+ "&types=geocode&language=en&sensor=true&key=AIzaSyAUG-M-cu2bxWEVGoUZXvU5WO6WMeP3vns");
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				// take Google's legible JSON and turn it into one big string.
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}

				// turn that string into a JSON object
				JSONObject predictions = new JSONObject(sb.toString());
				// now get the JSON array that's inside that object
				JSONArray ja = new JSONArray(
						predictions.getString("predictions"));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					// add each entry to our array
					predictionsArr.add(jo.getString("description"));
				}
			} catch (IOException e) {
				Log.e("YourApp", "GetPlaces : doInBackground", e);

			} catch (JSONException e) {
				Log.e("YourApp", "GetPlaces : doInBackground", e);

			}

			return predictionsArr;

		}

		// then our post

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// Log.e("RESULT LOCATIONS", ""+result);
			// update the adapter
			adapter = new ArrayAdapter<String>(LocationPickerActivity.this,
					android.R.layout.simple_list_item_1);
			adapter.setNotifyOnChange(true);
			// attach the adapter to textview
			//autoCompView.setAdapter(adapter);
			adapter.clear();
			for (String string : result) {
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}

		}

	}*/

}
