package com.telhai.android_316294321.handlers;

import android.util.Log;
import android.widget.SeekBar;

import com.telhai.android_316294321.SoundControlView;

public class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {
    //#region Properties
    // Static
    private static final String TAG = "VolumeChangeListener";
    // Instance
    private final SoundControlView view;
    //#endregion

    //#region Constructors
    public VolumeChangeListener(SoundControlView _view) {
        Log.v(TAG, "Constructor()");
        view = _view;
    }
    //#endregion

    //#region Overrides
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, String.format("onProgressChanged(%s, %d, %s)",
                view.getSoundName(), progress, fromUser));
        if (fromUser) view.setVolume(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.v(TAG, String.format("onStartTrackingTouch(%s)", view.getSoundName()));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.v(TAG, String.format("onStopTrackingTouch(%s)", view.getSoundName()));
    }
    //#endregion
}
