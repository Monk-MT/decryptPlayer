package com.ciee.cau.decryptPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ciee.cau.decryptPlayer.R;


public class LauncherFragment extends Fragment {

    public static LauncherFragment newInstance() {
        return new LauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

//        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        Intent intent = new Intent(getActivity(), VideoSelectionActivity.class);
        startActivity(intent);

        return view;
    }

}