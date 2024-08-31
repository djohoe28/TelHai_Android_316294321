package com.telhai.android_316294321.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MyValueEventListener implements ValueEventListener {
    //#region Properties
    private static final String TAG = "MyValueEventListener";
    //#endregion

    //#region Constructors
    public MyValueEventListener() {
    }
    //#endregion

    //#region Overrides
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String value = snapshot.getValue(String.class);
        Log.i(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // Failed to read value
        Log.e(TAG, "Failed to read value.", error.toException());
    }
    //#endregion
}
