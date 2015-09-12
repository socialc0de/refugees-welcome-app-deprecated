package de.pajowu.donate;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {
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
        View v = inflater.inflate(R.layout.fragment_faqfragment, container, false);
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);

        ArrayList<CategoryCardItem> categoryItems = new ArrayList<CategoryCardItem>(){};
        categoryItems.add(new CategoryCardItem("Transport", R.drawable.bike));
        categoryItems.add(new CategoryCardItem("Real Estates", R.drawable.flat));
        categoryItems.add(new CategoryCardItem("Nature", R.drawable.animal));
        categoryItems.add(new CategoryCardItem("Shops", R.drawable.profile));
        categoryItems.add(new CategoryCardItem("Food", R.drawable.restaurant));
        categoryItems.add(new CategoryCardItem("Jobs", R.drawable.topview));


        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(v.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("pos = ", position + "");
                        FAQCategoryFragment faqCategoryFragment = new FAQCategoryFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_relative_layout, faqCategoryFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                })
        );

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        RecyclerViewAdapter ca = new RecyclerViewAdapter(categoryItems);
        recList.setAdapter(ca);
        return v;
    }




}
