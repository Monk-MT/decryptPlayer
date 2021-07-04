package com.ciee.cau.decryptPlayer;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class PlayerActivity extends SingleFragmentActivity {


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return PlayerFragment.newInstance();
    }
}