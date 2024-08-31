package com.telhai.android_316294321.handlers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FabOnClickListener implements View.OnClickListener {
    //#region Properties
    // Static
    private static final String TAG = "FabOnClickListener";
    private static final int REQUEST_CODE_PERMISSION = 1;
    // Instance
    private final Activity mActivity;
    private final ActivityResultLauncher<String> mGetContent;
    //#endregion

    public FabOnClickListener(Activity _activity, ActivityResultLauncher<String> _getContent) {
        mActivity = _activity;
        mGetContent = _getContent;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick()");

        // Check Permission
        if (ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted; Request permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Tiramisu or newer (Android 33+)
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                        REQUEST_CODE_PERMISSION);
            } else {
                // Older than Tiramisu (Android 32-)
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            }
        } else {
            // Permission granted; Request audio file.
            mGetContent.launch("audio/*");
        }
    }
}
