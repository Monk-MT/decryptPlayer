package com.ciee.cau.decryptPlayer;

import androidx.fragment.app.Fragment;

public class VideoSelectionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return VideoSelectionFragment.newInstance();
    }
}