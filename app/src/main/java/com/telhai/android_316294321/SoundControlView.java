package com.telhai.android_316294321;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class SoundControlView extends CardView {

    //#region Properties
    private static final String TAG = "SoundControlView";
    // View References
    private final SeekBar seekVolume;
    private final ToggleButton toggleMute;
    // Attributes
    private final String soundName;
    private final String soundPath;
    private final String soundIcon;
    // State
    private int volume = 50;
    private boolean isMuted = false;
    //#endregion

    public SoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sound_control, this, true);
        // Initialize attributes
        soundName = "temp"; // attrs.getAttributeValue(R.attr.sound_name);
        soundPath = "temp"; // attrs.getAttributeValue(R.attr.sound_path);
        soundIcon = "temp"; // attrs.getAttributeValue(R.attr.sound_icon);
        Log.w(TAG, String.format("Constructor(name=%s, path=%s, icon=%s)",
                soundName, soundPath, soundIcon));
        // Initialize views
        // Mute ToggleButton
        toggleMute = findViewById(R.id.toggleMute);
        Log.w(TAG, String.format("toggleMute = %s", toggleMute.getText()));
        toggleMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                seekVolume.setProgress(checked ? 25 : 75);
            }
        });
        // Volume SeekBar
        seekVolume = findViewById(R.id.seekVolume);
        seekVolume.setProgress(volume);
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.w(TAG, String.format("onProgressChanged(%s, %d, %s)", soundName, progress, fromUser));
                if (fromUser) {
                    volume = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.w(TAG, String.format("seekVolume.onStartTrackingTouch(%s)", soundName));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.w(TAG, String.format("seekVolume.onStopTrackingTouch(%s)", soundName));
            }
        });


    }

}