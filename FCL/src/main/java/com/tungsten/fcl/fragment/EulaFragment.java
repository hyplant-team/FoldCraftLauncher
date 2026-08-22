package com.tungsten.fcl.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.SplashActivity;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.ReadTools;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.FCLFragment;
import com.tungsten.fcllibrary.component.theme.ThemeData;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;
import java.io.IOException;

public class EulaFragment extends FCLFragment implements View.OnClickListener {

    public static final String EULA_URL = FCLApplication.Prop.getProperty("eula-url","null://");

    private FCLProgressBar progressBar;
    private FCLTextView eula;
    private FCLTextView eulaTitle;

    private FCLButton next;

    private boolean load = false;
    private boolean online = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eula, container, false);

        progressBar = findViewById(view, R.id.progress);
        eula = findViewById(view, R.id.eula);
        eulaTitle = findViewById(view, R.id.eula_title);

        int textColor = ThemeEngine.getInstance().getTheme().getColor2();
        eula.setTextColor(textColor);
        eulaTitle.setTextColor(textColor);

        next = findViewById(view, R.id.next);
        next.setOnClickListener(this);

        loadEula();

        return view;
    }

    private void loadEula() {
        new Thread(() -> {
            String str = getString(R.string.splash_eula_error);
            try {
                String local_eula = FCLPath.FILES_DIR + "/debug/eula.txt";
                if (new File(local_eula).exists()) {
                    str = ReadTools.readFileTxt(local_eula);
                } else {
                    str = NetworkUtils.doGet(NetworkUtils.toURL(EULA_URL));
                }
                online = true;
                load = true;
            } catch (IOException | IllegalArgumentException e) {
                str = e.toString();
            }
            if (!load) {
                try {
                    str = IOUtils.readFullyAsString(requireActivity().getAssets().open( "local_eula.txt"));
                    load = true;
                } catch (IOException e) {
                    str = e.toString();
                }
            }
            final String s = str;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    eula.setText(s);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == next) {
            if (getActivity() != null) {
                if (!load) return;
                if (online) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("is_first_launch", false);
                    editor.apply();
                }
                ((SplashActivity) getActivity()).checkRuntime();
            }
        }
    }
}
