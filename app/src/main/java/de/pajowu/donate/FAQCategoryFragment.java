package de.pajowu.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.FAQItemProtoQuestionAnswerLanguage;
import com.appspot.donate_backend.donate.model.FAQItemProtoQuestionAnswerLanguageCollection;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQCategoryFragment extends Fragment {
    FAQItemProtoQuestionAnswerLanguageCollection result;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private View viewRoot;
    private String cat_id;

    public FAQCategoryFragment(String cid) {
        // Required empty public constructor
        cat_id = cid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_faqcategory, container, false);
        loadFragmentData();
        return viewRoot;
    }

    public void fillLayout() {

        if (result.getItems() != null) {
            final ExpandableListView expandableList = (ExpandableListView) viewRoot.findViewById(R.id.list); // you can use (ExpandableListView) findViewById(R.id.list)
            expandableList.setDividerHeight(2);
            expandableList.setGroupIndicator(null);
            expandableList.setClickable(true);
            FAQCategoryAdapter adapter = new FAQCategoryAdapter(new ArrayList<FAQItemProtoQuestionAnswerLanguage>(result.getItems()), getActivity().getApplicationContext());
            expandableList.setAdapter(adapter);
        } else {
            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.no_questions));
        }

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
                    result = service.faqitem().bycat().setCategory(cat_id).execute();
                    Log.d("GSW MainActivity", result.toString());
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
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.couldnt_fetch, getString(R.string.faq_items)));
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}
