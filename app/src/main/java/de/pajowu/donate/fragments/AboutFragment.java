package de.pajowu.donate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.InputStream;

import de.pajowu.donate.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_about, container, false);
        try {
            TextView aboutTextView = (TextView) inflatedView.findViewById(R.id.about);
            InputStream is = getActivity().getAssets().open(getString(R.string.about_filename));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String about_text = new String(buffer, "UTF-8");
            aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
            aboutTextView.setText(Html.fromHtml(about_text));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inflatedView;
    }
}