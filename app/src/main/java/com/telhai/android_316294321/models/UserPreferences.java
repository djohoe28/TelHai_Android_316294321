package com.telhai.android_316294321.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserPreferences {
    public int airVolume;
    public int waterVolume;
    public int earthVolume;
    public int fireVolume;

    public UserPreferences() {
        // Default constructor required for Firebase
    }

    public UserPreferences(int airVolume, int waterVolume, int earthVolume, int fireVolume) {
        this.airVolume = airVolume;
        this.waterVolume = waterVolume;
        this.earthVolume = earthVolume;
        this.fireVolume = fireVolume;
    }
}