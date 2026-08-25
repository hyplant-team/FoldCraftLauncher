package com.tungsten.fcl.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.util.ReadTools;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.skin.SkinRenderer;
import com.tungsten.fcllibrary.skin.SkinViewer;
import com.tungsten.fcllibrary.util.LocaleUtils;
import com.tungsten.fclauncher.utils.FCLPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MainUI extends FCLCommonUI implements View.OnClickListener {

    public static final String ANNOUNCEMENT_URL = FCLPath.Prop.getProperty("announcement-url","null://");

    private LinearLayoutCompat announcementContainer;
    private LinearLayoutCompat announcementLayout;
    private FCLTextView title;
    private FCLTextView announcementView;
    private FCLTextView date;
    private FCLButton hide;
    private Announcement announcement = null;
    private boolean isChecking = false;

    private SkinViewer skinViewer;
    private SkinRenderer renderer;

    private ObjectProperty<Account> currentAccount;

    public MainUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        announcementContainer = findViewById(R.id.announcement_container);
        announcementLayout = findViewById(R.id.announcement_layout);
        title = findViewById(R.id.title);
        announcementView = findViewById(R.id.announcement);
        date = findViewById(R.id.date);
        hide = findViewById(R.id.hide);
        ThemeEngine.getInstance().registerEvent(announcementLayout, () -> announcementLayout.getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor()));
        hide.setOnClickListener(this);

        skinViewer = findViewById(R.id.skin_viewer);
        renderer = new SkinRenderer(getContext());
        skinViewer.setRenderer(renderer, 5f);

        setupSkinDisplay();

        // 皮肤渲染随页面挂载/回收恢复与暂停（替代原 onStart/onStop 生命周期）
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                // if (skinViewer != null) {
                //     if (!ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
                //         skinViewer.setVisibility(View.VISIBLE);
                //         skinViewer.onResume();
                //         renderer.updateTexture(renderer.getTexture()[0], renderer.getTexture()[1]);
                //     } else {
                //         skinViewer.onPause();
                //         skinViewer.setVisibility(View.GONE);
                //     }
                // }
                checkAnnouncement();
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
                // if (skinViewer != null) {
                //     skinViewer.onPause();
                //     skinViewer.setVisibility(View.GONE);
                // }
                checkSkinDisplay(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (skinViewer != null) {
            skinViewer.onPause();
            // skinViewer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (skinViewer != null && isShowing() && !ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
            if (skinViewer.getVisibility() == View.VISIBLE) {
                skinViewer.onResume();
            }
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    private void checkAnnouncement() {
        if(FCLPath.Prop.getProperty("enable-announcement-component","false").equals("true")){
            isChecking = true;
            AtomicReference<String> remoteDataRef = new AtomicReference<>();
            AtomicReference<Announcement> announcementDataRef = new AtomicReference<>();
            title.setText(getContext().getString(R.string.announcement));
            announcementView.setText(getContext().getString(R.string.announcement_loading));
            date.setText(new String(ANNOUNCEMENT_URL));
            announcementContainer.setVisibility(View.VISIBLE);
            checkSkinDisplay(false);
            CompletableFuture<Announcement> future = CompletableFuture.supplyAsync(() -> {
                try {
                    String remoteData;
                    String local_announcement = FCLPath.FILES_DIR + "/debug/announcement.json";
                    if (new File(local_announcement).exists()) {
                        remoteData = ReadTools.readFileTxt(local_announcement);
                    } else {
                        remoteData = NetworkUtils.doGet(NetworkUtils.toURL(ANNOUNCEMENT_URL));
                    }
                    remoteDataRef.set(remoteData);
                }catch (Exception e) {
                    Logging.LOG.log(Level.WARNING, "Unable to load online announcement", e);
                    return new Announcement(
                        -1, true, false, -1, -1, new ArrayList<>(),
                        new ArrayList<>(Collections.singletonList(new Announcement.Content(null, getContext().getString(R.string.announcement_error_network)))),
                        new String(ANNOUNCEMENT_URL),
                        new ArrayList<>(Collections.singletonList(new Announcement.Content(null, getContext().getString(R.string.announcement_error_network_content) + "\n" + ANNOUNCEMENT_URL)))
                    );
                }
                try {
                    Announcement announcementData = new Gson().fromJson(remoteDataRef.get(), Announcement.class);
                    announcementDataRef.set(announcementData);
                }catch (Exception e) {
                    Logging.LOG.log(Level.WARNING, "Failed to process JSON file", e);
                    return new Announcement(
                        -1, true, false, -1, -1, new ArrayList<>(),
                        new ArrayList<>(Collections.singletonList(new Announcement.Content(null, getContext().getString(R.string.announcement_error_format)))),
                        new String(ANNOUNCEMENT_URL),
                        new ArrayList<>(Collections.singletonList(new Announcement.Content(null, getContext().getString(R.string.announcement_error_format_content) + "\n" + remoteDataRef.get())))
                    );
                }
                return announcementDataRef.get();
            });
            future.thenAccept(announcement -> new Handler(Looper.getMainLooper()).post(() -> {
                this.announcement = announcement;
                try {
                    if (!announcement.shouldDisplay(getContext())) {
                        announcementContainer.setVisibility(View.GONE);
                        checkSkinDisplay(true);
                        isChecking = false;
                        return;
                    }
                    title.setText(this.announcement.getDisplayTitle(getContext()));
                    announcementView.setText(this.announcement.getDisplayContent(getContext()));
                    date.setText(this.announcement.getDate());
                }catch(Exception e) {
                    Logging.LOG.log(Level.WARNING, "Failed to process announcement data", e);
                    title.setText(getContext().getString(R.string.announcement_error_data));
                    announcementView.setText(ANNOUNCEMENT_URL);
                    date.setText(getContext().getString(R.string.announcement_error_data_content) + "\n" + remoteDataRef.get());
                }
                isChecking = false;
            }));
        } else {
            checkSkinDisplay(false);
        }
    }

    private void hideAnnouncement() {
        announcementContainer.setVisibility(View.GONE);
        if (announcement != null) {
            announcement.hide(getContext());
        }
        checkSkinDisplay(true);
    }

    private void setupSkinDisplay() {
        currentAccount = new SimpleObjectProperty<>() {

            @Override
            protected void invalidated() {
                Account account = get();
                renderer.textureProperty().unbind();
                if (account == null) {
                    renderer.updateTexture(BitmapFactory.decodeStream(MainUI.class.getResourceAsStream("/assets/img/alex.png")), null);
                } else {
                    renderer.textureProperty().bind(TexturesLoader.textureBinding(account));
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }

    private void checkSkinDisplay(boolean visible) {
        if (skinViewer != null) {
            if (visible && !ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
                skinViewer.setVisibility(View.VISIBLE);
                skinViewer.onResume();
                renderer.updateTexture(renderer.getTexture()[0], renderer.getTexture()[1]);
            } else {
                skinViewer.onPause();
                skinViewer.setVisibility(View.GONE);
            }
        }
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (currentAccount.get() == account) {
                renderer.textureProperty().unbind();
                renderer.textureProperty().bind(TexturesLoader.textureBinding(currentAccount.get()));
            }
        });
    }

    public void onBackPressed() {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
        builder.setCancelable(false);
        builder.setMessage(getContext().getString(R.string.menu_settings_force_exit_msg));
        builder.setPositiveButton(getContext().getString(R.string.dialog_negative), null);
        builder.setNegativeButton(getContext().getString(R.string.dialog_positive), () -> {
            getActivity().finish();
            System.exit(0);
        });
        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        if (view == hide) {
            if (announcement != null && (isChecking || announcement.isSignificant())) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setCancelable(true);
                builder.setMessage(getContext().getString(R.string.announcement_significant));
                builder.setPositiveButton(null, null);
                builder.setNegativeButton(getContext().getString(R.string.dialog_positive), null);
                builder.create().show();
            } else {
                hideAnnouncement();
            }
        }
    }
}