package com.github.socialc0de.gsw.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.socialc0de.gsw.android.R;

public class DonateFragment extends Fragment {

    public DonateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_donate, container, false);



        return inflatedView;
    }
}