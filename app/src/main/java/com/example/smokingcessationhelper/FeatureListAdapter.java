package com.example.smokingcessationhelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FeatureListAdapter extends BaseAdapter {

    private ArrayList<FeatureListItem> featureListItems = new ArrayList<>();

    FeatureListAdapter() {}

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.feature_list_item, viewGroup, false);
        }

        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvFeature = view.findViewById(R.id.tvFeature);

        FeatureListItem featureListItem = featureListItems.get(position);

        ivIcon.setImageDrawable(featureListItem.getIcon());
        tvFeature.setText(featureListItem.getFeature());

        return view;
    }

    @Override
    public int getCount() {
        return featureListItems.size();
    }

    @Override
    public FeatureListItem getItem(int position) {
        return featureListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Drawable icon, String feature) {
        FeatureListItem featureListItem = new FeatureListItem();

        featureListItem.setIcon(icon);
        featureListItem.setFeature(feature);

        featureListItems.add(featureListItem);
    }

    public void removeItem(int position) {
        featureListItems.remove(position);
    }
}
