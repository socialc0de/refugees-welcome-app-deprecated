package com.github.socialc0de.gsw.android.fragments;


import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import com.github.socialc0de.gsw.android.R;

public class PhraseDetailFragment extends android.support.v4.app.Fragment {
    private final String TAG = "GSW MainActivity";
    //TODO Get Labels, Sub_labels, categories, images, objects
    HashMap<String, String> translations;
    private View viewRoot;

    public PhraseDetailFragment(HashMap<String, String> trnsltns) {
        translations = trnsltns;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_phrase_detail, container, false);
        LinearLayout ll = (LinearLayout) viewRoot.findViewById(R.id.linearlayout);
        for (HashMap.Entry<String, String> lang : translations.entrySet()) {
            TextView tv = new TextView(getActivity().getApplicationContext());
            tv.setText(lang.getValue());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
            tv.setTextColor(R.color.accentColor);
            ll.addView(tv);
        }

        return viewRoot;

    }
}

