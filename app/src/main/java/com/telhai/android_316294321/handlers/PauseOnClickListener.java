package com.telhai.android_316294321.handlers;

import android.util.Log;
import android.view.View;

import com.telhai.android_316294321.SoundControlView;

public class PauseOnClickListener implements View.OnClickListener {
    //#region Properties
    // Static
    private static final String TAG = "PauseOnClickListener";
    // Instance
    private final SoundControlView view;
    //#endregion

    //#region Constructors
    public PauseOnClickListener(SoundControlView _view) {
        view = _view;
    }
    //#endregion

    //#region Overrides
    @Override
    public void onClick(View view) {
        Log.i(TAG, String.format("onCheckedChanged(%s)", this.view.getSoundName()));
        this.view.togglePaused(); // .setIsPaused(!isPaused);
    }
    //#endregion
}
