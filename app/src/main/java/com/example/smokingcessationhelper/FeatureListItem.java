package com.example.smokingcessationhelper;

import android.graphics.drawable.Drawable;

public class FeatureListItem {
    private Drawable icon;
    private String feature;

    public Drawable getIcon() { return icon; }
    public String getFeature() { return feature; }

    public void setIcon(Drawable icon) { this.icon = icon; }
    public void setFeature(String feature) { this.feature = feature; }
}
