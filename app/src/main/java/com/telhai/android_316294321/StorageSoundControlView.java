package com.telhai.android_316294321;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

public class StorageSoundControlView extends SoundControlView {
    //#region Properties
    // Static
    private static final String TAG = "StorageSoundControlView";
    //#endregion

    //#region Constructors
    public StorageSoundControlView(@NonNull Context context, String _soundName, int _soundIcon, Uri _soundPath) {
        super(context, _soundName, _soundIcon);
        setMedia(_soundPath);
        Log.i(TAG, String.format("ParamConstructor(name=%s icon=%s path=%s)", _soundName, _soundIcon, _soundPath));
    }

    public StorageSoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "AttrConstructor()");
    }
    //#endregion

    //#region Overrides
    @Override
    public int getLayout() {
        return R.layout.sound_control;
    }
    //#endregion
}
