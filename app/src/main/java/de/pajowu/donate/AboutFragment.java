package de.pajowu.donate;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.SpannableString;
import android.text.Spannable;

import android.widget.TextView;
import java.io.InputStream;
import android.text.Html;
import java.lang.Exception;
import android.text.method.LinkMovementMethod;

public class AboutFragment extends Fragment {

    public AboutFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_about, container, false);
        /*SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(getActivity().getApplicationContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);*/
        try {
            TextView aboutTextView = (TextView)inflatedView.findViewById(R.id.about);
            InputStream is = getActivity().getAssets().open("about.html");
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