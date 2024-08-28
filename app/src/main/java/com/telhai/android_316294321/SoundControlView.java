package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class SoundControlView extends CardView {

    //#region Properties
    private static final String TAG = "SoundControlView";
    private final ImageButton imageButtonMute;
    private final SeekBar seekVolume;
    private MediaPlayer mediaPlayer;
    // Attributes
    private String soundName;
    private String soundPath;
    private String soundIcon;
    // State
    private int volume = 50;
    private boolean isMuted = false;
    //#endregion

    public SoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sound_control, this, true);
        // Initialize attributes
        try (TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SoundControlView, 0, 0)) {
            soundName = a.getString(R.styleable.SoundControlView_soundName);
            soundIcon = a.getString(R.styleable.SoundControlView_soundIcon);
            soundPath = a.getString(R.styleable.SoundControlView_soundPath);
            // TODO: Create with soundPath - maybe split built-in (resourceId) and custom (path) ?
            mediaPlayer = MediaPlayer.create(context, R.raw.fire_pixabay_fireplace_6160);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
            mediaPlayer.start();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage() != null ? ex.getMessage() : "attrs Exception");
        } // TODO: finally { a.recycle(); }
        Log.d(TAG, String.format("Constructor(name=%s, path=%s, icon=%s)",
                soundName, soundPath, soundIcon));
        //#region Initialize views
        // Name TextView
        TextView textName = findViewById(R.id.textName);
        textName.setText(soundName);
        // Mute ImageButton
        imageButtonMute = findViewById(R.id.imageButtonMute);
        imageButtonMute.setOnClickListener(view -> {
            Log.d(TAG, String.format("toggleMute.onCheckedChanged(%s, %s)", soundName, isMuted));
            setIsMuted(!isMuted);
        });
        // Volume SeekBar
        seekVolume = findViewById(R.id.seekVolume);
        seekVolume.setProgress(volume); // NOTE: Doing this before adding listener prevents call.
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, String.format("seekVolume.onProgressChanged(%s, %d, %s)",
                        soundName, progress, fromUser));
                if (fromUser) setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, String.format("seekVolume.onStartTrackingTouch(%s)", soundName));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, String.format("seekVolume.onStopTrackingTouch(%s)", soundName));
            }
        });
    }

    //#endregion
    //#region Setters
    public void setVolume(int value) {
        volume = value;
        seekVolume.setProgress(volume);
        mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
    }

    public void setIsMuted(boolean value) {
        isMuted = value;
        imageButtonMute.setImageResource(isMuted
                ? R.drawable.baseline_volume_off_24
                : R.drawable.baseline_volume_up_24);
        // NOTE: This is technically not muting but pausing; However, I think it's cute.
        if (isMuted) mediaPlayer.pause();
        else mediaPlayer.start();

    }
    //#endregion
}