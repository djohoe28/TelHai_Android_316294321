package com.telhai.android_316294321.handlers;

import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;

import com.telhai.android_316294321.R;
import com.telhai.android_316294321.SoundActivity;
import com.telhai.android_316294321.SoundControlView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriActivityResultCallback implements ActivityResultCallback<Uri> {
    //#region Properties
    // Static
    private static final String TAG = "UriActivityResultCallback";
    private static final Pattern FILENAME_PATTERN = Pattern.compile(".*/(?<name>.*)");
    // Instance
    private final SoundActivity mActivity;
    //#endregion

    public UriActivityResultCallback(SoundActivity _mActivity) {
        mActivity = _mActivity;
    }

    @Override
    public void onActivityResult(Uri o) {
        Log.i(TAG, String.format("onActivityResult(%s)", o));
        if (o != null) {
            // Extract filename from URI
            String segment = o.getLastPathSegment() == null ? o.toString() : o.getLastPathSegment();
            Matcher matcher = FILENAME_PATTERN.matcher(segment);
            String name = matcher.find() ? matcher.group("name") : segment;
            // Create view
            SoundControlView _view = new SoundControlView(
                    mActivity,
                    o,
                    name,
                    R.drawable.baseline_audio_file_24);
            mActivity.addSoundView(_view);
        }
    }
}
