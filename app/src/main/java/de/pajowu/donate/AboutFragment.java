package de.pajowu.donate;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.SpannableString;
import android.text.Spannable;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import android.widget.TextView;
public class AboutFragment extends Fragment {

    public AboutFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_about, container, false);
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(getActivity().getApplicationContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);
        TextView aboutTextView = (TextView)inflatedView.findViewById(R.id.about);
        aboutTextView.setText("Wichtiges Zeugs, Lizensen und so!");
        return inflatedView;
    }
}