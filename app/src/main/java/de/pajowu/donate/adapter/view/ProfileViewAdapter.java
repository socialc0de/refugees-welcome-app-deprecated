package de.pajowu.donate.adapter.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.pajowu.donate.ProfileRowHolder;
import de.pajowu.donate.R;
import de.pajowu.donate.models.ProfileRow;

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
        ProfileRowHolder.textView.setText(String.valueOf(items.getTitle()));
        ProfileRowHolder.imageView.setBackgroundResource(items.getImgIcon());
    }
}