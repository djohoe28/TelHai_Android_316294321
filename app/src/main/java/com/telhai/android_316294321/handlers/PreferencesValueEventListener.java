package com.telhai.android_316294321.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.telhai.android_316294321.SoundActivity;
import com.telhai.android_316294321.models.UserPreferences;

public class PreferencesValueEventListener implements ValueEventListener {
    //#region Properties
    // Static
    private static final String TAG = "UserValueEventListener";
    // Instance
    private final SoundActivity context;
    //#endregion

    //#region Constructors
    public PreferencesValueEventListener(SoundActivity _context) {
        context = _context;
    }
    //#endregion

    //#region Overrides
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        UserPreferences value = snapshot.getValue(UserPreferences.class);
        context.updatePreferences(value);
        Log.i(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // Failed to read value
        Log.e(TAG, "Failed to read value.", error.toException());
    }
    //#endregion
}
