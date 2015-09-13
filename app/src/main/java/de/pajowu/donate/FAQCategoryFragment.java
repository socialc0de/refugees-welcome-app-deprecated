package de.pajowu.donate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQCategoryFragment extends Fragment {
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();


    public FAQCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_faqcategory, container, false);
        final ExpandableListView expandableList = (ExpandableListView) v.findViewById(R.id.list); // you can use (ExpandableListView) findViewById(R.id.list)

        Bundle bundle = this.getArguments();
        final String stringFromOldFragment = bundle.getString("recPosition", "test");

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.93.67.220")
                .build();
        APIService2 service = retrofit.create(APIService2.class);
        Call<ResponseBody> repos = service.getDataFromAPI();

        //final ArrayList<CategoryCardItem> categoryItems1 = new ArrayList<CategoryCardItem>() {};
        final List list = Collections.synchronizedList(new ArrayList<CategoryCardItem>());


        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {

                    String myString;
                    myString = ""+response.body().string().replaceAll("\\d+", "\"$0\"")+"";
                    //myString = myString.replaceAll("\"009","");
                    Log.d("myString = ",myString);

                    //String decoded = new String(myString.getBytes("UTF-8"));

                    JSONArray jsonArray = new JSONArray(myString);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Log.d("jsonArrayobject: ", "" + jsonArray.getJSONObject(i).getInt("language"));

                        if (jsonArray.getJSONObject(i).getString("category_name").equals(stringFromOldFragment)) {
                            //String categoryName = jsonArray.getJSONObject(i).getString("name");
                            //Log.d("jsonArrayobject: ", categoryName);
                            //int categoryImage = R.drawable.snackbar_background;

                            String question = jsonArray.getJSONObject(i).getString("question_translation");
                            String answer = jsonArray.getJSONObject(i).getString("question_answer");

                            ArrayList<String> answer_child = new ArrayList<String>();
                            answer_child.add(answer);

                            parentItems.add(question);
                            childItems.add(answer_child);

                            //Log.d("jsonArrayobject: ", categoryImage + "");
                            //list.add(new CategoryCardItem(categoryName, categoryImage));
                            //Log.d("CategoryCardItem: ",categoryItems1+"");

                        }
                    }
                    MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

                    adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
                    expandableList.setAdapter(adapter);

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

        //Log.d("repos.toString = ", "" + repos.toString());





        return v;
    }

    public void setGroupParents() {
        parentItems.add("Where may I get food for little money?");
        parentItems.add("Does german food contain pig flesh?");
        parentItems.add("Is german food expensive?");
        parentItems.add("Where can I find typically Syrian Restaurants?");
    }
/*
    public void setChildData() {

        // Android
        ArrayList<String> child = new ArrayList<String>();
        child.add("Kindertafel");
        childItems.add(child);

        // Core Java
        child = new ArrayList<String>();
        child.add("Yes");
        childItems.add(child);

        // Desktop Java
        child = new ArrayList<String>();
        child.add("Unfortunately, yes.");

        childItems.add(child);

        // Enterprise Java
        child = new ArrayList<String>();
        child.add("EJB3");

        childItems.add(child);
    }
*/

}
