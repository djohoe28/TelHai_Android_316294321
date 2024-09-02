package com.telhai.android_316294321.handlers;

import android.widget.SeekBar;

import com.telhai.android_316294321.R;
import com.telhai.android_316294321.SoundActivity;
import com.telhai.android_316294321.SoundControlView;

public class PreferenceVolumeChangeListener extends VolumeChangeListener {
    private final SoundActivity context;

    public PreferenceVolumeChangeListener(SoundControlView _view, SoundActivity _context) {
        super(_view);
        context = _context;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (fromUser && context.preferences != null) {
            int viewId = view.getId();
            if (viewId == R.id.soundWater)
                context.preferences.waterVolume = progress;
            else if (viewId == R.id.soundEarth)
                context.preferences.earthVolume = progress;
            else if (viewId == R.id.soundFire)
                context.preferences.fireVolume = progress;
            else if (viewId == R.id.soundAir)
                context.preferences.airVolume = progress;
            context.dbRef.setValue(context.preferences);
        }
    }
}
