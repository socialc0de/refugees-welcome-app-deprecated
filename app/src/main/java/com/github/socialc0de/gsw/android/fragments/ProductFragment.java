package com.github.socialc0de.gsw.android.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.OfferProtoId;
import com.appspot.donate_backend.donate.model.OfferProtoIdTitleSubtitleDescriptionCategoriesImageUrlsLatLonOwnerEndDateOwnerKey;
import com.github.androidprogresslayout.ProgressLayout;
import com.github.socialc0de.gsw.android.MainActivity;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.models.ContactRow;
import com.github.socialc0de.gsw.android.tools.CloudEndpointBuilderHelper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ProductFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private final String TAG = "GSW MainActivity";
    //TODO Get Labels, Sub_labels, categories, images, objects
    Context mContext;
    Long primaryKey;
    OfferProtoIdTitleSubtitleDescriptionCategoriesImageUrlsLatLonOwnerEndDateOwnerKey offer_data;
    Map<String, Object> im;
    String gplus_url = "";
    ArrayList<ContactRow> im_data;
    private View viewRoot;

    public ProductFragment(Context context, Long pk) {
        this.mContext = context;
        this.primaryKey = pk;
        Log.d("GSW MainActivity", new Long(pk).toString());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_product, container, false);

        loadFragmentData();

        return viewRoot;

    }

    @Override
    public void onClick(View v) {
        Log.d("GSW MainActivity", gplus_url);
        Log.d("GSW MainActivity", MainActivity.getMainActivity().gplus_url);
        if (gplus_url.equals(MainActivity.getMainActivity().gplus_url)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(getString(R.string.sure));
            alert.setMessage(getString(R.string.sure_delete_offer));
            alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Builder endpointBuilder = new Donate.Builder(
                                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                                    CloudEndpointBuilderHelper.getRequestInitializer());

                            Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                            try {
                                service.offer().delete(new OfferProtoId().setId(primaryKey)).execute();
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
                                    LocalFragment newFragment = new LocalFragment();
                                    //((MaterialNavigationDrawer) getActivity()).setFragment(newFragment,"Local");
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
                                    MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);
                                }
                            });

                        }
                    };
                    new Thread(runnable).start();
                }
            });
            alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        } else {
            ContactFragment contactFragment = new ContactFragment(mContext, im_data);
            //((MaterialNavigationDrawer) getActivity()).setFragment(contactFragment,"Owner");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).addToBackStack(null).commit();
            MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);
        }

    }

    private void fillLayout() {
        Log.d("GSW MainActivity", MainActivity.getMainActivity().gplus_url);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.fragment_product_mainImage);
        getActivity().findViewById(R.id.fragment_product_messageButton).setOnClickListener(this);
        gplus_url = (String) ((HashMap) im.get("gplus")).get("url");
        if (gplus_url.equals(MainActivity.getMainActivity().gplus_url)) {
            ((FloatingActionButton) getActivity().findViewById(R.id.fragment_product_messageButton)).setImageResource(R.drawable.ic_delete);
        }
        im_data = new ArrayList<ContactRow>();
        for (Map.Entry<String, Object> entry : im.entrySet()) {
            if (entry.getValue().getClass().equals(String.class)) {
                im_data.add(new ContactRow(mContext, (String) entry.getValue(), entry.getKey()));
            }
            if (entry.getValue().getClass().equals(HashMap.class)) {
                try {
                    im_data.add(new ContactRow(mContext, (String) ((HashMap) entry.getValue()).get("display"), entry.getKey(), (String) ((HashMap) entry.getValue()).get("url")));

                } catch (Exception e) {
                    Log.d(TAG, "Error", e);
                }
            }
            Log.d("GSW MainActivity", entry.getValue().getClass().toString());
        }
        //roundedImageView = (RoundedImageView) getActivity().findViewById(R.id.fragment_product_productImage);

        // Log.d("primaryKey would be: ", "" + primaryKey);


        // Setting Main Title Typeface and appearance
        /*mainTitle = (TextView) getActivity().findViewById(R.id.app_bar_title);
        mainTitleTypeface = Typeface.createFromAsset(getAssets(), "fabiolo.otf");
        mainTitle.setTypeface(mainTitleTypeface);*/

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());

        float ht_px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        if (offer_data.getImageUrls() != null) {
            Picasso.with(getActivity().getApplication()).load(offer_data.getImageUrls().get(0) + "=s" + metrics.widthPixels).resize(metrics.widthPixels, (int) ht_px2).into(imageView);
        }
        //TextView offerTextView = (TextView) getActivity().findViewById(R.id.offer);
        TextView titleTextView = (TextView) getActivity().findViewById(R.id.title);
        TextView subtitleTextView = (TextView) getActivity().findViewById(R.id.subtitle);
        TextView offerTextView = (TextView) getActivity().findViewById(R.id.offerText);

        titleTextView.setText(offer_data.getTitle());
        subtitleTextView.setText(offer_data.getSubtitle());
        offerTextView.setText(offer_data.getDescription());
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
                    offer_data = service.offer().get(primaryKey).execute();
                    Log.d("GSW MainActivity", offer_data.getOwner().toString());
                    im = jsonToMap(new JSONObject(offer_data.getOwner().getIm().toString()));
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
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.couldnt_fetch, getString(R.string.offer)));
                        }
                    });
                }


            }
        };
        new Thread(runnable).start();
    }

}

