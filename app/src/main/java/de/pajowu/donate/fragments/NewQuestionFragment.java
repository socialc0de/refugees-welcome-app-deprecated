package de.pajowu.donate.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.FAQCategory;
import com.appspot.donate_backend.donate.model.FAQItem;
import com.appspot.donate_backend.donate.model.FAQItemProtoQuestionAnswerLanguageAnsweredCategory;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;

import de.pajowu.donate.MainActivity;
import de.pajowu.donate.R;
import de.pajowu.donate.tools.CloudEndpointBuilderHelper;

//import de.pajowu.donate.*;

public class NewQuestionFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "GSW MainActivity";
    Context mContext;
    HashMap<String, FAQCategory> catMap;
    String cat;
    ArrayList<String> langs = new ArrayList<String>();
    HashMap<String, String> cats = new HashMap<String, String>();
    private View viewRoot;

    public NewQuestionFragment(HashMap<String, FAQCategory> catMap) {
        this.catMap = catMap;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_new_item, container, false);
        //Implementation of custom Toolbar
        Button submitButton = (Button) viewRoot.findViewById(R.id.submit);
        submitButton.setOnClickListener(this);
        Spinner spinner = (Spinner) viewRoot.findViewById(R.id.categories);
        // Create an ArrayAdapter using the string array and a default spinner layout

        for (HashMap.Entry<String, FAQCategory> cat : catMap.entrySet()) {
            cats.put(cat.getValue().getName(), cat.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>(cats.keySet()));
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        langs.add("en");
        langs.add("de");
        spinner = (Spinner) viewRoot.findViewById(R.id.language);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, langs);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        return viewRoot;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                Log.d("GSW MainActivity", "pressed");
                submit();
                break;
        }
    }

    private void submit() {
        String question = ((MaterialEditText) viewRoot.findViewById(R.id.question)).getText().toString();
        String answer = ((MaterialEditText) viewRoot.findViewById(R.id.answer)).getText().toString();
        cat = cats.get(((Spinner) viewRoot.findViewById(R.id.categories)).getSelectedItem().toString());
        String lang = ((Spinner) viewRoot.findViewById(R.id.language)).getSelectedItem().toString();
        final FAQItemProtoQuestionAnswerLanguageAnsweredCategory new_item = new FAQItemProtoQuestionAnswerLanguageAnsweredCategory();
        new_item.setQuestion(question);
        new_item.setAnswer(answer);
        new_item.setCategory(cat);
        new_item.setLanguage(lang);
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                FAQItem result;
                try {
                    result = service.faqitem().create(new_item).execute();
                    Log.d("GSW MainActivity", result.toString());
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle(getString(R.string.coulnd_create_item));
                            alert.setMessage(getString(R.string.couldnt_create_item_errortext));
                            alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                        }
                    });

                    // Connect to NewQuestionFragment
                    //MaterialSection matSec = newSection("NewQuestionFragment", new NewQuestionFragment(getApplicationContext(), arrayList));
                    //addSection(matSec);


                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                        FAQFragment newFragment = new FAQFragment();
                        //((MaterialNavigationDrawer) getActivity()).setFragment(newFragment, "Local");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
                        MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);

                    }
                });

            }
        };
        new Thread(runnable).start();

    }
}

