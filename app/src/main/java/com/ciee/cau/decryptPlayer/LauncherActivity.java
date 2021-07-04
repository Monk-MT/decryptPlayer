package com.ciee.cau.decryptPlayer;

import androidx.fragment.app.Fragment;

public class LauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LauncherFragment.newInstance();
    }
}