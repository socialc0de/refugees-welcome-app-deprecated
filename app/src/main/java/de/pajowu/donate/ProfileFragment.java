package de.pajowu.donate;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.melnykov.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import de.pajowu.donate.models.Person;

public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    UserProtoImAddressNameImageUrl user_data;
    public Person appOwner;
    public MaterialEditText textViewName;
    public MaterialEditText textViewPhone;
    public MaterialEditText textViewCity;
    public MaterialEditText textViewEmail;
    public EditText textViewWebsite;
    public FloatingActionButton editButton;
    private boolean editMode = false;
    private final String TAG = "GSW MainActivity";
    View viewRoot;
    Map<String, Object> im;
    private boolean editModePossible = false;

    public ProfileFragment(Person appOwner) {
        this.appOwner = appOwner;
    }

    public ProfileFragment() {
        editModePossible = true;
        Log.d("ProfFrag called: ", "no args");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        if (appOwner != null) {
            fillLayout();
        } else {
            loadFragmentData();
        }

        // TODO set Content to each individual Textview and ImageView in xml File

        return viewRoot;
    }

    private void updateUser() {
        try {
            final UserProtoAddressImInterestImage user = new UserProtoAddressImInterestImage();
            JSONObject im_json = new JSONObject();
            JSONObject phone = new JSONObject();
            phone.put("url", "tel:" + appOwner.getPhone());
            phone.put("display", appOwner.getPhone());
            JSONObject email = new JSONObject();
            email.put("url", "mailto:" + appOwner.getEmail());
            email.put("display", appOwner.getEmail());
            JSONObject website = new JSONObject();
            website.put("url", appOwner.getUrl());
            website.put("display", appOwner.getUrl());
            im_json.put("mail", email);
            im_json.put("phone", phone);
            im_json.put("web", website);
            Log.d(TAG, im_json.toString());
            user.setIm(im_json.toString());
            if (appOwner.getCity() != null) {
                user.setAddress(appOwner.getCity());
            }
            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Builder endpointBuilder = new Donate.Builder(
                            AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                            CloudEndpointBuilderHelper.getRequestInitializer());

                    Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();


                    try {
                        user_data = service.user().update(user).execute(); //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)*/
                        //user_data = service.user().data().execute(); //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)*/
                        // Do NOT use the same variable name for different things, just as dummy and database content

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
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                            fillLayout();
                        }
                    });

                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.d(TAG, "Error", e);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_profile_editButton:
                Log.d("EditButton: ", "pressed");

                EditProfileFragment nextFrag = new EditProfileFragment(appOwner);
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
                ((MainActivity)getActivity()).mDrawer.setSelection(-1, false);

                //TODO Set Editable = true (search fitting code for it
                //TODO Maybe add lines again to make obvious, that they can be edited
                break;
        }
    }

    private void fillLayout() {

        editButton = (FloatingActionButton) viewRoot.findViewById(R.id.fragment_profile_editButton);
        editButton.setOnClickListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, viewRoot.getResources().getDisplayMetrics());
        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, viewRoot.getResources().getDisplayMetrics());

        int complexUnitDip = TypedValue.COMPLEX_UNIT_DIP;
        float ht_px2 = TypedValue.applyDimension(complexUnitDip, 250, viewRoot.getResources().getDisplayMetrics());
        textViewName = (MaterialEditText) viewRoot.findViewById(R.id.fragment_profile_name);
        textViewPhone = (MaterialEditText) viewRoot.findViewById(R.id.fragment_profile_phone);
        textViewCity = (MaterialEditText) viewRoot.findViewById(R.id.fragment_profile_city);
        textViewEmail = (MaterialEditText) viewRoot.findViewById(R.id.fragment_profile_mail);
        //textViewWebsite = (EditText) viewRoot.findViewById(R.id.fragment_profile_website);

        ImageView imageView = (ImageView) viewRoot.findViewById(R.id.profileImage);

        textViewPhone.setText(appOwner.getPhone());
        textViewCity.setText(appOwner.getCity());
        textViewEmail.setText(appOwner.getEmail());
        disableTextView(textViewName);
        disableTextView(textViewPhone);
        disableTextView(textViewCity);
        disableTextView(textViewEmail);
        textViewName.setText(appOwner.getName());
        if (appOwner.getProfileImage() != null && appOwner.getProfileImage() != "") {
            Picasso.with(getActivity().getApplicationContext()).load(appOwner.getProfileImage()).into(imageView);
            imageView.setAlpha(0.7f);
        }
    }

    public void loadFragmentData() {
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();


                try {
                    user_data = service.user().data().execute(); //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)*/
                    im = jsonToMap(new JSONObject(user_data.getIm().toString()));
                    appOwner = new Person("", "", "", "", "", "");
                    if (im.get("gplus") != null) {
                        appOwner.setName(((HashMap<String, String>) im.get("gplus")).get("display"));
                    }
                    if (im.get("phone") != null) {
                        appOwner.setPhone(((HashMap<String, String>) im.get("phone")).get("display"));
                    }
                    if (im.get("mail") != null) {
                        appOwner.setEmail(((HashMap<String, String>) im.get("mail")).get("display"));
                    }
                    if (im.get("web") != null) {
                        appOwner.setUrl(((HashMap<String, String>) im.get("web")).get("display"));
                    }
                    if (user_data.getAddress() != null) {
                        appOwner.setCity(user_data.getAddress());
                    }
                    if (user_data.getImageUrl() != null) {
                        appOwner.setProfileImage(user_data.getImageUrl());
                    }
                    Log.d(TAG, user_data.toString());

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                            fillLayout();
                        }
                    });
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
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.couldnt_fetch,getString(R.string.profile)));
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private void disableTextView(MaterialEditText met) {
        met.setFocusable(false);
        met.setFocusableInTouchMode(false);
        met.setClickable(false);
        met.setHideUnderline(true);
    }

    private void enableTextView(MaterialEditText met) {
        met.setFocusable(true);
        met.setFocusableInTouchMode(true);
        met.setClickable(true);
        met.setHideUnderline(false);
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
