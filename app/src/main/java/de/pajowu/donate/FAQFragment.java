package de.pajowu.donate;



import android.support.v4.app.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.json.Json;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    Map<String, Integer> num;

    public enum IMAGES {
        SHOPPING, HEALTHCARE, FOOD, JOBS, REAL_ESTATES, SOCIAL_HELP, KIDS, EDUCATION, PROPOSALS, LAW, TRANSPORT, BASIC_INFORMATION;

        public int getImage()
        {
            if ( this == SHOPPING )
                return R.drawable.flat;
            else if ( this == HEALTHCARE )
                return R.drawable.topview;
            else if ( this == FOOD )
                return R.drawable.topview;
            else if ( this == JOBS )
                return R.drawable.topview;
            else if ( this == REAL_ESTATES )
                return R.drawable.topview;
            else if ( this == SOCIAL_HELP)
                return R.drawable.topview;
            else if ( this == KIDS )
                return R.drawable.topview;
            else if ( this == EDUCATION )
                return R.drawable.topview;
            else if ( this == PROPOSALS )
                return R.drawable.topview;
            else if ( this == LAW )
                return R.drawable.topview;
            else if ( this == TRANSPORT )
                return R.drawable.topview;
            else if ( this == BASIC_INFORMATION )
                return R.drawable.topview;
            return R.drawable.restaurant;
        }
    }


    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faqfragment, container, false);
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);

        num = new HashMap<String, Integer>();
        num.put("Shopping",R.drawable.topview);
        num.put("Healthcare",R.drawable.restaurant);
        num.put("Food",R.drawable.profile);
        num.put("Jobs",R.drawable.topview);
        num.put("Real_Estates",R.drawable.topview);
        num.put("Social_Help", R.drawable.restaurant);
        num.put("Kids", R.drawable.restaurant);
        num.put("Education", R.drawable.restaurant);
        num.put("Proposals", R.drawable.restaurant);
        num.put("Law", R.drawable.restaurant);
        num.put("Transport", R.drawable.restaurant);
        num.put("Basic_Information", R.drawable.restaurant);

        /*
        final ArrayList<CategoryCardItem> categoryItems = new ArrayList<CategoryCardItem>(){};
        categoryItems.add(new CategoryCardItem("Transport", R.drawable.bike));
        categoryItems.add(new CategoryCardItem("Real Estates", R.drawable.flat));
        categoryItems.add(new CategoryCardItem("Nature", R.drawable.animal));
        categoryItems.add(new CategoryCardItem("Shops", R.drawable.profile));
        categoryItems.add(new CategoryCardItem("Food", R.drawable.restaurant));
        categoryItems.add(new CategoryCardItem("Jobs", R.drawable.topview));
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.93.67.220")
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> repos = service.getDataFromAPI();

        final ArrayList<CategoryCardItem> categoryItems1 = new ArrayList<CategoryCardItem>(){};

        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    Log.d("onResponse: ",response.body().string());

                    ArrayList<JSONObject> arrayListJSON = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (JSONObject temp : arrayListJSON) {

                    }
                    Log.d("Testausgabe","");
                    for (int i = 0; i < jsonArray.length(); i++){
                        Log.d("jsonArrayobject: ", ""+jsonArray.getJSONObject(i).getInt("language"));
                        if (jsonArray.getJSONObject(i).getInt("language") == 0){
                            String categoryName = jsonArray.getJSONObject(i).getString("name");
                            int categoryImage = num.get(jsonArray.getJSONObject(i).getString("name"));
                            if (categoryImage != 0){
                                categoryItems1.add(new CategoryCardItem(categoryName,categoryImage));
                            }
                        }
                    }

                    //categoryItems.add();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure: ", "Connection Failed");
            }
        });



        Log.d("repos.toString = ",""+repos.toString());


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
        RecyclerViewAdapter ca = new RecyclerViewAdapter(categoryItems1);
        recList.setAdapter(ca);
        return v;
    }

}

