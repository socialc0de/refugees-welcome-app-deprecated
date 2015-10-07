package de.pajowu.donate;


import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.*;
import com.appspot.donate_backend.donate.model.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.github.androidprogresslayout.ProgressLayout;
import java.util.ArrayList;
import org.json.JSONArray;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.widget.TextView;

public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    UserProtoImAddressName user_data;
    public Person appOwner;
    public TextView textViewName;
    public TextView textViewPhone;
    public TextView textViewCity;
    public TextView textViewEmail;
    public EditText textViewWebsite;
    public FloatingActionButton editButton;
    private boolean editMode = false;
    private final String TAG = "MainActivity";
    View viewRoot;
    Map<String,Object> im;
    public ProfileFragment(Person appOwner) {
        this.appOwner = appOwner;
    }

    public ProfileFragment() {
        Log.d("ProfFrag called: ", "no args");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        //Implementation of custom Toolbar
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(viewRoot.getContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);
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
            final UserProtoAddressImInterest user = new UserProtoAddressImInterest();
            JSONObject im_json = new JSONObject();
            JSONObject phone = new JSONObject();
            phone.put("url","tel:"+appOwner.phone);
            phone.put("display",appOwner.phone);
            JSONObject email = new JSONObject();
            email.put("url","mailto:"+appOwner.email);
            email.put("display",appOwner.email);
            JSONObject website = new JSONObject();
            website.put("url",appOwner.url);
            website.put("display",appOwner.url);
            im_json.put("mail",email);
            im_json.put("phone",phone);
            im_json.put("web",website);
            Log.d(TAG,im_json.toString());
            user.setIm(im_json.toString());
            if (appOwner.city != null) {
                user.setAddress(appOwner.city);
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
    /*
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)
                        homeFragment = new HomeFragment(getApplicationContext(), arrayList);
                        fragmentTransaction.add(R.id.container, homeFragment);
                        fragmentTransaction.commit();
    */

                    } catch (UserRecoverableAuthIOException e) {
                        final UserRecoverableAuthIOException e2 = e;
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startActivityForResult(e2.getIntent(), 2);
                            }
                        });
                        Log.d("MainActivity", "e", e);
                    } catch (Exception e) {
                        Log.d("MainActivity", "e", e);



                        /*
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)
                        homeFragment = new HomeFragment(getApplicationContext(), arrayList);
                        fragmentTransaction.add(R.id.container, homeFragment);
                        fragmentTransaction.commit();
                        */

                        // Connect to HomeFragment
                        //MaterialSection matSec = newSection("homeFragment", new HomeFragment(getApplicationContext(), arrayList));
                        //addSection(matSec);


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
            Log.d(TAG,"Error",e);
        }
        
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_profile_editButton:
                Log.d("EditButton: ", "pressed");

                if (!editMode) {
                    editMode = true;
                    editButton.setImageResource(R.drawable.ic_done_white);

                    //textViewName.setEnabled(true);
                    textViewPhone.setEnabled(true);
                    
                    textViewCity.setEnabled(true);
                    textViewEmail.setEnabled(true);
                    textViewWebsite.setEnabled(true);

                } else if (editMode) {
                    
                    appOwner.phone = textViewPhone.getText().toString();
                    appOwner.city = textViewCity.getText().toString();
                    appOwner.email = textViewEmail.getText().toString();
                    appOwner.url = textViewWebsite.getText().toString();

                    textViewName.setEnabled(false);
                    textViewPhone.setEnabled(false);
                    textViewCity.setEnabled(false);
                    textViewEmail.setEnabled(false);
                    textViewWebsite.setEnabled(false);


                    editButton.setImageResource(R.drawable.ic_mode_edit_white);
                    editMode = false;
                    updateUser();

                }

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
        textViewName = (TextView) viewRoot.findViewById(R.id.fragment_profile_name);
        textViewPhone = (TextView) viewRoot.findViewById(R.id.fragment_profile_phone);
        textViewCity = (TextView) viewRoot.findViewById(R.id.fragment_profile_city);
        textViewEmail = (TextView) viewRoot.findViewById(R.id.fragment_profile_mail);
        //textViewWebsite = (EditText) viewRoot.findViewById(R.id.fragment_profile_website);

        if (appOwner.phone.equals("") && appOwner.city.equals("") && appOwner.email.equals("")){
            textViewPhone.setText("01575 064 5725");
            textViewCity.setText("Frankfurt");
            textViewEmail.setText("patrice@5becker.de");
        }
        else {
            textViewPhone.setText(appOwner.phone);

            textViewCity.setText(appOwner.city);
            textViewEmail.setText(appOwner.email);
        }
        textViewName.setText(appOwner.name);

        //textViewWebsite.setText(appOwner.url);
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
                    im =  jsonToMap(new JSONObject(user_data.getIm().toString()));
                    appOwner = new Person("","","","","","");
                    if (im.get("gplus") != null) {
                        appOwner.name = ((HashMap<String,String>)im.get("gplus")).get("display");
                    }
                    if (im.get("phone") != null) {
                        appOwner.phone = ((HashMap<String,String>)im.get("phone")).get("display");
                    }
                    if (im.get("mail") != null) {
                        appOwner.email = ((HashMap<String,String>)im.get("mail")).get("display");
                    }
                    if (im.get("web") != null) {
                        appOwner.url = ((HashMap<String,String>)im.get("web")).get("display");
                    }
                    if (user_data.getAddress() != null) {
                        appOwner.city = user_data.getAddress();
                    }
                    Log.d(TAG,user_data.toString());

                    // Do NOT use the same variable name for different things, just as dummy and database content
/*
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)
                    homeFragment = new HomeFragment(getApplicationContext(), arrayList);
                    fragmentTransaction.add(R.id.container, homeFragment);
                    fragmentTransaction.commit();
*/

                } catch (UserRecoverableAuthIOException e) {
                    final UserRecoverableAuthIOException e2 = e;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            startActivityForResult(e2.getIntent(), 2);
                        }
                    });
                    Log.d("MainActivity", "e", e);
                } catch (Exception e) {
                    Log.d("MainActivity", "e", e);



                    /*
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //Context context, int resource, String[] labels, String[] resources, int[] images, int[] primaryKeys, int[] objects)
                    homeFragment = new HomeFragment(getApplicationContext(), arrayList);
                    fragmentTransaction.add(R.id.container, homeFragment);
                    fragmentTransaction.commit();
                    */

                    // Connect to HomeFragment
                    //MaterialSection matSec = newSection("homeFragment", new HomeFragment(getApplicationContext(), arrayList));
                    //addSection(matSec);


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
    }
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { 
        //super.onActivityResult(requestCode, resultCode, data); 

        switch(requestCode) { 
            case CHOOSE_BACKGROUND_PIC:
                if(resultCode == getActivity().RESULT_OK){  
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        backgroundPicture = BitmapFactory.decodeStream(imageStream);
                        Picasso.with(getActivity().getApplicationContext()).load(selectedImage).resize(0, ((ImageView)viewRoot.findViewById(R.id.fragment_profile_mainImage)).getWidth()).into((ImageView)viewRoot.findViewById(R.id.fragment_profile_mainImage));
                    } catch (Exception e) {
                        Log.d("MainActivity","e",e);
                    }
                    
                }
                break;
            case CHOOSE_PROFILE_PIC:
                if(resultCode == getActivity().RESULT_OK){  
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        profilePicture = BitmapFactory.decodeStream(imageStream);
                        Picasso.with(getActivity().getApplicationContext()).load(selectedImage).resize(0, ((ImageView)viewRoot.findViewById(R.id.fragment_profile_mainImage)).getWidth()).into((ImageView)viewRoot.findViewById(R.id.fragment_profile_profileImage));
                    } catch (Exception e) {
                        Log.d("MainActivity","e",e);
                    }
                    
                }
                break;
        }
    }*/
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
