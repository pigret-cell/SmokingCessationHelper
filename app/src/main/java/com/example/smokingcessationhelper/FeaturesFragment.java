package com.example.smokingcessationhelper;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class FeaturesFragment extends Fragment {
    private FeatureListAdapter featureListAdapter = new FeatureListAdapter();

    public FeaturesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_features, container, false);

        final ListView lvFeatures = (ListView) view.findViewById(R.id.FeaturesFragment_lvFeatures);
        lvFeatures.setAdapter(featureListAdapter);

        featureListAdapter.addItem(getResources().getDrawable(R.drawable.contact), "연락처");
        featureListAdapter.notifyDataSetChanged();

        lvFeatures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), ContactActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}