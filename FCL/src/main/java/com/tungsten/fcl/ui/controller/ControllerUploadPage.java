package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.download.ControllerIndex;
import com.tungsten.fcl.control.download.ControllerVersion;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.ui.ProgressDialog;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.Zipper;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;

public class ControllerUploadPage extends FCLTempPage implements View.OnClickListener {

    private final Controller controller;

    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView description;

    private FCLButton community;
    private FCLButton share;

    public ControllerUploadPage(Context context, int id, FCLUILayout parent, int resId, Controller controller) {
        super(context, id, parent, resId);
        this.controller = controller;
        create();
    }

    public void create() {
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        description = findViewById(R.id.intro);

        name.setText(controller.getName());
        tag.setText(controller.getVersion());
        description.setText(controller.getDescription());

        community = findViewById(R.id.community);
        share = findViewById(R.id.share);
        community.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View view) {
        if (view == community) {
            joinCommunity();
        }
        if (view == share) {
            ControllerUploadDialog dialog = new ControllerUploadDialog(getContext(), getActivity(), controller, this::share);
            dialog.show();
        }
    }

    private void share(String name, String author, String intro, String description, String lang, ArrayList<Integer> devices, ArrayList<String> screenshots, String iconPath) {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.show();
        Task.supplyAsync(() -> {
            FileUtils.deleteDirectoryQuietly(new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId()));
            ControllerIndex index = new ControllerIndex(controller.getId(), lang, name, intro, devices, new ArrayList<>());
            ControllerVersion version = new ControllerVersion(screenshots.size(), description, author, new ControllerVersion.VersionInfo(controller.getVersionCode(), controller.getVersion()), new ArrayList<>());
            File indexFile = File.createTempFile("index", ".json");
            Files.write(indexFile.toPath(), JsonUtils.GSON.toJson(index).getBytes(StandardCharsets.UTF_8));
            File versionFile = File.createTempFile("version", ".json");
            Files.write(versionFile.toPath(), JsonUtils.GSON.toJson(version).getBytes(StandardCharsets.UTF_8));

            FileUtils.copyFile(indexFile, new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + "/index.json"));
            FileUtils.copyFile(versionFile, new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + "/version.json"));
            for (int i = 1; i <= screenshots.size(); i++) {
                String sp = screenshots.get(i - 1);
                String num = Integer.toString(i).length() == 1 ? "0" + i : i + "";
                FileUtils.copyFile(new File(sp), new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + "/screenshots/" + num + ".png"));
            }
            if (iconPath != null)
                FileUtils.copyFile(new File(iconPath), new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + "/icon.png"));
            FileUtils.copyFile(new File(FCLPath.CONTROLLER_DIR + "/" + controller.getFileName()), new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + "/versions/" + controller.getVersionCode() + ".json"));
            Path zip = new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + ".zip").toPath();
            try (Zipper zipper = new Zipper(zip)) {
                zipper.putDirectory(new File(FCLPath.CACHE_DIR + "/control/upload/" + controller.getId()).toPath(), controller.getId());
            }
            return FCLPath.CACHE_DIR + "/control/upload/" + controller.getId() + ".zip";
        }).thenAcceptAsync(Schedulers.androidUIThread(), (ExceptionalConsumer<String, Exception>)s -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", new File(s));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(Intent.createChooser(intent, getContext().getString(com.tungsten.fcllibrary.R.string.crash_reporter_share)));
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            dialog.dismiss();
            if (exception != null) {
                Logging.LOG.log(Level.SEVERE, "Failed to export controller and its info!", exception.getMessage());
            }
        }).start();
    }


    public boolean joinCommunity() {
        Uri uri = Uri.parse(FCLApplication.Prop.getProperty("community-controller","null://"));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            getContext().startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
