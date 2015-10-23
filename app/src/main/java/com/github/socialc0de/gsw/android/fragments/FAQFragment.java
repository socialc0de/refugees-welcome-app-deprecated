package com.github.socialc0de.gsw.android.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.FAQCategory;
import com.appspot.donate_backend.donate.model.FAQCategoryCollection;
import com.github.androidprogresslayout.ProgressLayout;
import com.github.socialc0de.gsw.android.MainActivity;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.adapter.view.RecyclerViewAdapter;
import com.github.socialc0de.gsw.android.listener.RecyclerItemClickListener;
import com.github.socialc0de.gsw.android.models.CategoryCardItem;
import com.github.socialc0de.gsw.android.tools.CloudEndpointBuilderHelper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment implements View.OnClickListener {
    View viewRoot;
    Map<String, Integer> num;
    HashMap<String, FAQCategory> cats;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_faqfragment, container, false);
        if (cats == null) {
            loadFragmentData();
        } else {
            fillLayout();
        }
        return viewRoot;
    }

    public void fillLayout() {
        final ArrayList<CategoryCardItem> list = new ArrayList<CategoryCardItem>();
        final RecyclerView recList = (RecyclerView) viewRoot.findViewById(R.id.cardList);
        for (HashMap.Entry<String, FAQCategory> entry : cats.entrySet()) {
            list.add(new CategoryCardItem(entry.getValue().getName(), entry.getValue().getImageUrl(), entry.getValue().getId()));
        }
        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(viewRoot.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("pos = ", position + "");
                        FAQCategoryFragment faqCategoryFragment = new FAQCategoryFragment(list.get(position).getId());
                        //((MaterialNavigationDrawer) getActivity()).setFragmentChild(faqCategoryFragment,"Answers");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, faqCategoryFragment).addToBackStack(null).commit();
                        MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);
                    }
                })
        );

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(viewRoot.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Log.d("CategoryItems1: ", list + "");
        RecyclerViewAdapter ca = new RecyclerViewAdapter(list);
        recList.setAdapter(ca);
        FloatingActionButton editButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab);
        editButton.setOnClickListener(this);
        editButton.setVisibility(View.VISIBLE);

    }

    public void loadFragmentData() {
        Log.d("GSW MainActivity", "loadFragmentData");
        ((ProgressLayout) viewRoot.findViewById(R.id.faqfragment_progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("GSW MainActivity", "try");
                    Builder endpointBuilder = new Donate.Builder(
                            AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                            CloudEndpointBuilderHelper.getRequestInitializer());

                    Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                    FAQCategoryCollection result;
                    result = service.faqcat().list().execute();
                    cats = new HashMap<String, FAQCategory>();
                    Log.d("GSW MainActivity", result.toString());
                    for (FAQCategory cat : result.getItems()) {
                        cats.put(cat.getId(), cat);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fillLayout();
                            ((ProgressLayout) viewRoot.findViewById(R.id.faqfragment_progress_layout)).showContent();
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
                            ((ProgressLayout) viewRoot.findViewById(R.id.faqfragment_progress_layout)).showErrorText(getString(R.string.couldnt_fetch, getString(R.string.faq_categories)));
                        }
                    });
                }

            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d("GSW MainActivity", "pressed");
                if (MainActivity.getCredential().getSelectedAccountName() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewQuestionFragment(cats)).addToBackStack(null).commit();
                    MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.please_sign_in));
                    alert.setMessage(R.string.cant_create_offer_if_not_signed_in);
                    alert.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().startActivityForResult(MainActivity.getCredential().newChooseAccountIntent(), MainActivity.REQUEST_ACCOUNT_PICKER);
                        }
                    });
                    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }

                //TODO Set Editable = true (search fitting code for it
                //TODO Maybe add lines again to make obvious, that they can be edited
                break;
        }
    }

}

