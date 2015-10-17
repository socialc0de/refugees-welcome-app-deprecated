package com.github.socialc0de.gsw.android.adapter.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.models.CategoryCardItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ContactViewHolder> {

    private List<CategoryCardItem> contactList;
    private Context mContext;

    public RecyclerViewAdapter(List<CategoryCardItem> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CategoryCardItem ci = contactList.get(i);
        contactViewHolder.vCategory.setText(ci.getCategory());
        if (ci.getImage() != "") {
            Picasso.with(mContext).load(ci.getImage()).fit().into(contactViewHolder.vImage);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.faq_layout, viewGroup, false);


        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCategory;
        protected ImageView vImage;


        public ContactViewHolder(View v) {
            super(v);
            vImage = (ImageView) v.findViewById(R.id.headImage);
            vCategory = (TextView) v.findViewById(R.id.txtName);
        }
    }


}
