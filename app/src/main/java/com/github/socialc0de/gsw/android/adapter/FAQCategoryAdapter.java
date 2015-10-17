package com.github.socialc0de.gsw.android.adapter;

/**
 * Created by patricebecker on 12/09/15.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.appspot.donate_backend.donate.model.FAQItemProtoQuestionAnswerLanguage;

import java.util.ArrayList;

import com.github.socialc0de.gsw.android.R;

public class FAQCategoryAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater;
    private ArrayList<FAQItemProtoQuestionAnswerLanguage> items;

    public FAQCategoryAdapter(ArrayList<FAQItemProtoQuestionAnswerLanguage> items, Context context) {
        this.items = items;
        Log.d("GSW MainActivity", items.toString());
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TextView textView = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group, null);
        }

        textView = (TextView) convertView.findViewById(R.id.textView1);
        textView.setText(items.get(groupPosition).getAnswer());

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", items.get(groupPosition).getAnswer());
            }
        });

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
        }
        ((CheckedTextView) convertView).setText(items.get(groupPosition).getQuestion());
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}