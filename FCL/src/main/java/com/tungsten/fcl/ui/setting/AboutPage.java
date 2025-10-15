package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class AboutPage extends FCLCommonPage implements View.OnClickListener {

    private FCLLinearLayout about_a;
    private FCLLinearLayout about_b;
    private FCLLinearLayout about_c;
    private FCLLinearLayout community_a;
    private FCLLinearLayout community_b;
    private FCLLinearLayout community_c;

    public AboutPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        about_a = findViewById(R.id.about_a);
        about_b = findViewById(R.id.about_b);
        about_c = findViewById(R.id.about_c);
        about_a.setOnClickListener(this);
        about_b.setOnClickListener(this);
        about_c.setOnClickListener(this);
        community_a = findViewById(R.id.community_a);
        community_b = findViewById(R.id.community_b);
        community_c = findViewById(R.id.community_c);
        community_a.setOnClickListener(this);
        community_b.setOnClickListener(this);
        community_c.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        String url = null;

        if (v == about_a) {
            url = FCLApplication.Prop.getProperty("about-a","null://");
        }
        if (v == about_b) {
            url = FCLApplication.Prop.getProperty("about-b","null://");
        }
        if (v == about_c) {
            url = FCLApplication.Prop.getProperty("about-c","null://");
        }
        if (v == community_a) {
            url = FCLApplication.Prop.getProperty("community-a","null://");
        }
        if (v == community_b) {
            url = FCLApplication.Prop.getProperty("community-b","null://");
        }
        if (v == community_c) {
            url = FCLApplication.Prop.getProperty("community-c","null://");
        }

        if (url != null) {
            AndroidUtils.openLink(getContext(), url);
        }
    }
}
