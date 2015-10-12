package de.pajowu.donate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.util.ArrayList;

import com.github.androidprogresslayout.ProgressLayout;

import org.json.JSONObject;
import java.util.Map;
import java.util.List;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Iterator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.melnykov.fab.FloatingActionButton;
import android.util.TypedValue;
public class PhraseDetailFragment extends android.support.v4.app.Fragment{
    private final String TAG = "MainActivity";
    private View viewRoot;
    //TODO Get Labels, Sub_labels, categories, images, objects
    HashMap<String,String> translations;
    public PhraseDetailFragment(HashMap<String,String> trnsltns) {
        translations = trnsltns;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_phrase_detail, container, false);
        LinearLayout ll = (LinearLayout) viewRoot.findViewById(R.id.linearlayout);
        for (HashMap.Entry<String,String> lang: translations.entrySet()) {
            TextView tv = new TextView(getActivity().getApplicationContext());
            tv.setText(lang.getValue());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
            tv.setTextColor(R.color.accentColor);
            ll.addView(tv);
        }

        return viewRoot;

    }
}

