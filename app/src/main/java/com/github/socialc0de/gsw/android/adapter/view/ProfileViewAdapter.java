package com.github.socialc0de.gsw.android.adapter.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.models.ProfileRow;
import com.github.socialc0de.gsw.android.models.holder.ProfileRowHolder;

import java.util.ArrayList;

public class ProfileViewAdapter extends RecyclerView.Adapter<ProfileRowHolder> {
    Context context;
    ArrayList<ProfileRow> itemsList;

    public ProfileViewAdapter(Context context, ArrayList<ProfileRow> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }

    @Override
    public ProfileRowHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.profile_row, null);
        ProfileRowHolder viewHolder = new ProfileRowHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileRowHolder ProfileRowHolder, int position) {
        ProfileRow items = itemsList.get(position);
        ProfileRowHolder.getTextView().setText(String.valueOf(items.getTitle()));
        ProfileRowHolder.getImageView().setBackgroundResource(items.getImgIcon());
    }
}