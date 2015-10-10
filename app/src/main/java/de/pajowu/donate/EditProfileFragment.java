package de.pajowu.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.melnykov.fab.FloatingActionButton;
import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.melnykov.fab.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.widget.TextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private MaterialEditText name, emailPersonal, emailWork, phonePersonal, phoneWork, location;
    private ImageView imageView;
    private Button editButton;
    private FloatingActionButton finishedButton;
    public static final int CHOOSE_PROFILE_PIC = 1;
    public static final String TAG = "MainActivity";
    private View viewRoot;
    UserProtoImAddressNameImageUrl user_data;
    Bitmap profilePicture;
    Person mPerson;

    public EditProfileFragment() {
        // Required empty public constructor
    }
    public EditProfileFragment(Person p) {
        // Required empty public constructor
        mPerson = p;
        Log.d(TAG,p.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initializing Variables

        // MaterialEditTexts
        //TODO Load current profile data into MaterialEditText as hints
        name = (MaterialEditText) viewRoot.findViewById(R.id.editProfileName);
        disableTextView(name);
        emailPersonal = (MaterialEditText) viewRoot.findViewById(R.id.editProfileEmailPersonal);
        emailPersonal.addValidator(new RegexpValidator("Email not valid","^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"));
        //emailWork = (MaterialEditText) view.findViewById(R.id.editProfileEmailWork);
        phonePersonal = (MaterialEditText) viewRoot.findViewById(R.id.editProfilePhonePersonal);
        phonePersonal.addValidator(new RegexpValidator("Phone number not valid (only digits, starting with country code)","^(\\+|00)[0-9]*$"));
        //phoneWork = (MaterialEditText) view.findViewById(R.id.editProfilePhoneWork);
        location = (MaterialEditText) viewRoot.findViewById(R.id.editProfileLocation);
        // ImageView
        // TODO Load ProfileImage inside
        imageView = (ImageView) viewRoot.findViewById(R.id.editProfileImage);

        // FinishedButton
        finishedButton = (FloatingActionButton) viewRoot.findViewById(R.id.editProfileFinished);
        finishedButton.setOnClickListener(this);

        // EditImage Button
        editButton = (Button) viewRoot.findViewById(R.id.editProfileEditButton);
        editButton.setOnClickListener(this);
        //editButton.setAlpha(.3f);
        name.setText(mPerson.name);
        emailPersonal.setText(mPerson.email);
        phonePersonal.setText(mPerson.phone);
        location.setText(mPerson.city);
        if (mPerson.profileImage != null && mPerson.profileImage != "") {
            Picasso.with(getActivity().getApplicationContext()).load(mPerson.profileImage).into(imageView);
        }
        
        return viewRoot;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editProfileFinished:
                //TODO Sync MaterialEditTextData with Server
                updateUser();
                break;
            case R.id.editProfileEditButton:
                //TODO Open ImageChooser
                choosePic();
                break;
        }
    }
    private void choosePic() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, CHOOSE_PROFILE_PIC);
    }
    private void updateUser() {
        if (! (phonePersonal.validate() && emailPersonal.validate() && location.validate())) {
            return;
        }
        String pho = phonePersonal.getText().toString();
        String city = location.getText().toString();
        String ema = emailPersonal.getText().toString();
        try {
            final UserProtoAddressImInterestImage user = new UserProtoAddressImInterestImage();
            JSONObject im_json = new JSONObject();
            JSONObject phone = new JSONObject();
            phone.put("url", "tel:" + pho);
            phone.put("display", pho);
            JSONObject email = new JSONObject();
            email.put("url", "mailto:" + ema);
            email.put("display", ema);
            /*JSONObject website = new JSONObject();
            website.put("url", appOwner.url);
            website.put("display", appOwner.url);*/
            im_json.put("mail", email);
            im_json.put("phone", phone);
            //im_json.put("web", website);
            Log.d(TAG, im_json.toString());
            user.setIm(im_json.toString());
            if (city != null) {
                user.setAddress(city);
            }
            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (profilePicture != null) {
                        user.setImage(BitMapToString(resizeImageForImageView(profilePicture)));
                    }
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
                            showProfileFragment();
                        }
                    });

                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.d(TAG, "Error", e);
        }

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
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public void showProfileFragment() {
        ProfileFragment nextFrag = new ProfileFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag, null)
                .addToBackStack(null)
                .commit();
        ((MainActivity)getActivity()).mDrawer.setSelection(2, false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { 
        //super.onActivityResult(requestCode, resultCode, data); 

        switch(requestCode) { 
            case CHOOSE_PROFILE_PIC:
                if(resultCode == getActivity().RESULT_OK){  
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        profilePicture = BitmapFactory.decodeStream(imageStream);
                        Picasso.with(getActivity().getApplicationContext()).load(selectedImage).resize(0, ((ImageView)viewRoot.findViewById(R.id.editProfileImage)).getWidth()).into((ImageView)viewRoot.findViewById(R.id.editProfileImage));
                    } catch (Exception e) {
                        Log.d("MainActivity","e",e);
                    }
                    
                }
                break;
        }
    }
    private void disableTextView(MaterialEditText met) {
        met.setFocusable(false);
        met.setFocusableInTouchMode(false);
        met.setClickable(false);
        met.setHideUnderline(true);
    }
}
