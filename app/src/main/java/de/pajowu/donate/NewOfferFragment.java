package de.pajowu.donate;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.Category;
import com.appspot.donate_backend.donate.model.Offer;
import com.appspot.donate_backend.donate.model.OfferProtoTitleSubtitleDescriptionCategoriesImagesLatLonEndDate;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import de.pajowu.donate.models.ListItem;

//import de.pajowu.donate.*;

public class NewOfferFragment extends Fragment implements View.OnClickListener, OnDateSetListener {
    public static final int SELECT_PHOTO = 1;
    public static final int PLACE_PICKER_REQUEST = 2;
    public static final int LOCATION_PICKER = 3;
    private final String TAG = "GSW MainActivity";
    public ScrollView scrollView;
    public ExpandableGridView gridView;
    public ArrayList<ListItem> arrayList;
    //TODO Get Labels, Sub_labels, categories, images, objects
    Context mContext;
    Bitmap offerImage;
    LatLng offerLoc;
    String endDate;
    String cat;
    Button chooseCategoryButton;
    HashMap<String, String> cats = new HashMap<String, String>();
    private View viewRoot;

    public NewOfferFragment(Context context, ArrayList<ListItem> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;

    }

    public NewOfferFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_new_offer, container, false);
        //Implementation of custom Toolbar
        Button submitButton = (Button) viewRoot.findViewById(R.id.submit);
        submitButton.setOnClickListener(this);
        submitButton.setEnabled(false);
        submitButton.setAlpha(.3f);
        chooseCategoryButton = (Button) viewRoot.findViewById(R.id.choose_category);
        chooseCategoryButton.setOnClickListener(this);
        viewRoot.findViewById(R.id.offerImage).setOnClickListener(this);
        for (HashMap.Entry<String, Category> cat : ((MainActivity) getActivity()).categories.entrySet()) {
            cats.put(cat.getValue().getName(), cat.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>(cats.keySet()));
        MaterialEditText enddate = (MaterialEditText) viewRoot.findViewById(R.id.end_date);
        enddate.setOnClickListener(this);
        enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    chooseDate();
                }
            }
        });
        return viewRoot;

    }


    private void choosePic() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void choosePlace() {
        Intent pickup = new Intent(getActivity().getApplicationContext(), LocationPickerActivity.class);
        pickup.putExtra("lat", "32");
        pickup.putExtra("lon", "32");
        startActivityForResult(pickup, LOCATION_PICKER);
        Log.d("GSW MainActivity", "startActivityForResult");
    }

    public void chooseDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = getString(R.string.offen_end_on) + dayOfMonth + ". " + (monthOfYear + 1) + ". " + year;
        endDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " 00:00";
        ((MaterialEditText) viewRoot.findViewById(R.id.end_date)).setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                Log.d("GSW MainActivity", "pressed");
                submit();
                break;
            case R.id.offerImage:
                Log.d("GSW MainActivity", "pressed");
                choosePic();
                break;
            case R.id.end_date:
                chooseDate();
            case R.id.choose_category:
                chooseCategory();
        }
    }

    public void chooseCategory() {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Example");
        final String[] catkeys = cats.keySet().toArray(new String[0]);
        b.setSingleChoiceItems(catkeys, -1, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                cat = cats.get(catkeys[which]);
                Log.d("GSW MainActivity", cat);
                if (cat != null) {
                    Button submitButton = (Button) viewRoot.findViewById(R.id.submit);
                    submitButton.setEnabled(true);
                    submitButton.setAlpha(1f);
                }
                chooseCategoryButton.setText(catkeys[which]);
            }

        });

        b.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); 

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        offerImage = BitmapFactory.decodeStream(imageStream);
                        Picasso.with(getActivity().getApplicationContext()).load(selectedImage).resize(0, viewRoot.findViewById(R.id.offerImage).getWidth()).into((ImageView) viewRoot.findViewById(R.id.offerImage));
                    } catch (Exception e) {
                        Log.d("GSW MainActivity", "e", e);
                    }

                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == getActivity().RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getActivity().getApplicationContext());
                    offerLoc = place.getLatLng();
                    /*String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(getActivity().getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();*/
                    //setMap(map);
                }
            case LOCATION_PICKER:
                Double selectedLat = data.getDoubleExtra(AppConfig.USER_LAT, 00);
                Double selectedLon = data.getDoubleExtra(AppConfig.USER_LNG, 00);
                //String selectedLocation=data.getStringExtra(AppConfig.USER_LOCATION);   
                offerLoc = new LatLng(selectedLat, selectedLon);
                //setMap(map);
                submit();
        }
    }

    public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    public Bitmap resizeImageForImageView(Bitmap bitmap) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if (originalHeight > originalWidth) {
            newHeight = 1024;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (originalWidth > originalHeight) {
            newWidth = 1024;
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        } else if (originalHeight == originalWidth) {
            newHeight = 1024;
            newWidth = 1024;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    private void submit() {
        MaterialEditText titleTextView = (MaterialEditText) viewRoot.findViewById(R.id.title);
        String title = titleTextView.getText().toString();
        Boolean error = false;
        if (TextUtils.isEmpty(title)) {
            titleTextView.setError(getString(R.string.enter_title));
            error = true;
        }
        MaterialEditText subtitleTextView = (MaterialEditText) viewRoot.findViewById(R.id.subtitle);
        String subtitle = subtitleTextView.getText().toString();
        if (TextUtils.isEmpty(subtitle)) {
            subtitleTextView.setError(getString(R.string.enter_subtitle));
            error = true;
        }
        MaterialEditText descTextView = (MaterialEditText) viewRoot.findViewById(R.id.desc);
        String desc = descTextView.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            descTextView.setError(getString(R.string.enter_description));
            error = true;
        }
        if (error) {
            return;
        }
        if (offerLoc == null) {
            choosePlace();
            return;
        }
        /*cat = cats.get(((Spinner) viewRoot.findViewById(R.id.categories)).getSelectedItem().toString());*/
        OfferProtoTitleSubtitleDescriptionCategoriesImagesLatLonEndDate new_offer = new OfferProtoTitleSubtitleDescriptionCategoriesImagesLatLonEndDate();
        new_offer.setTitle(title);
        new_offer.setSubtitle(subtitle);
        new_offer.setDescription(desc);
        //new_offer.setOffer(offer);
        new_offer.setCategories(Arrays.asList(cat));
        new_offer.setLat(offerLoc.latitude);
        new_offer.setLon(offerLoc.longitude);
        new_offer.setEndDate(endDate);
        final OfferProtoTitleSubtitleDescriptionCategoriesImagesLatLonEndDate final_offer = new_offer;
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                Offer result;
                if (offerImage != null) {
                    final_offer.setImages(Arrays.asList(BitMapToString(resizeImageForImageView(offerImage))));
                } else {
                    final_offer.setImages(new ArrayList<String>());
                }
                try {
                    result = service.offer().create(final_offer).execute();
                    Log.d("GSW MainActivity", result.toString());
                } catch (UserRecoverableAuthIOException e) {
                    final UserRecoverableAuthIOException e2 = e;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            startActivityForResult(e2.getIntent(), 2);
                        }
                    });
                    Log.d("GSW MainActivity", "e", e);
                } catch (Exception e) {
                    Log.d("GSW MainActivity", "e", e);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle(getString(R.string.couldnt_create_offer));
                            alert.setMessage(getString(R.string.couldnt_create_offer_errortext));
                            alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                        }
                    });

                    // Connect to NewOfferFragment
                    //MaterialSection matSec = newSection("NewOfferFragment", new NewOfferFragment(getApplicationContext(), arrayList));
                    //addSection(matSec);


                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                        LocalFragment newFragment = new LocalFragment();
                        //((MaterialNavigationDrawer) getActivity()).setFragment(newFragment, "Local");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
                        ((MainActivity) getActivity()).mDrawer.setSelection(-1, false);

                    }
                });

            }
        };
        new Thread(runnable).start();

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}

