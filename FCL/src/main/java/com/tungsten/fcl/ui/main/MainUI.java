package com.tungsten.fcl.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.gson.Gson;
import com.mio.skin.AnimationDialog;
import com.mio.skin.SkinAnimations;
import com.mio.skin.SkinRenderer;
import com.mio.skin.SkinTextureLoader;
import com.mio.skin.SkinViewer;

import static com.mio.skin.SkinAnimationsKt.restoreSkinSettings;
import static com.mio.skin.SkinAnimationsKt.saveSkinSettings;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.util.ReadTools;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
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
    private SkinTextureLoader skinLoader;

    /** 选中账户变化时重载皮肤（attach 时注册、detach 时注销） */
    private final InvalidationListener accountListener = o ->
            skinLoader.load(Accounts.getSelectedAccount(), false);

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
        skinLoader = new SkinTextureLoader(renderer);
        skinLoader.load(Accounts.getSelectedAccount(), false);
        // 恢复上次选择的动画与 3D 皮肤层开关
        restoreSkinSettings(getContext(), renderer);
        // 双击模型弹出皮肤模型设置窗口
        skinViewer.setOnDoubleClick(() -> {
            Context context = getContext();
            if (context instanceof Activity && !((Activity) context).isDestroyed() && !((Activity) context).isFinishing()) {
                new AnimationDialog(context, renderer.getAnimationId(), renderer.getSolidLayerEnabled(), renderer.getUpperBodySeparated(), clipId -> {
                    renderer.playAnimation(clipId);
                    saveSkinSettings(context, renderer);
                }, enabled -> {
                    renderer.setSolidLayerEnabled(enabled);
                    saveSkinSettings(context, renderer);
                }, separated -> {
                    renderer.setUpperBodySeparated(separated);
                    saveSkinSettings(context, renderer);
                }).show();
            }
        });

        // 皮肤渲染随页面挂载/回收恢复与暂停（替代原 onStart/onStop 生命周期）
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                checkAnnouncement();
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
                checkSkinDisplay(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (skinViewer != null && skinViewer.getVisibility() == View.VISIBLE) {
            skinViewer.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (skinViewer != null && skinViewer.getVisibility() == View.VISIBLE) {
            skinViewer.onResume();
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    private void checkAnnouncement() {
        if(!FCLPath.Prop.getProperty("enable-announcement-component","false").equals("true")){
            announcementContainer.setVisibility(View.GONE);
            checkSkinDisplay(true);
            return;
        }
        isChecking = true;
        AtomicReference<String> remoteDataRef = new AtomicReference<>();
        AtomicReference<Announcement> announcementDataRef = new AtomicReference<>();
        title.setText(getContext().getString(R.string.announcement));
        announcementView.setText(getContext().getString(R.string.announcement_loading));
        date.setText(new String(ANNOUNCEMENT_URL));
        checkSkinDisplay(false);
        announcementContainer.setVisibility(View.VISIBLE);
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
    }

    private void hideAnnouncement() {
        announcementContainer.setVisibility(View.GONE);
        if (announcement != null) {
            announcement.hide(getContext());
        }
        checkSkinDisplay(true);
    }

    private void checkSkinDisplay(boolean visible) {
        if (skinLoader != null && visible) {
            // detach 期间选中的账户可能已切换（监听已注销），重新对齐一次
            Accounts.selectedAccountProperty().addListener(accountListener);
            skinLoader.load(Accounts.getSelectedAccount(), false);
        }
        if (skinViewer != null) {
            if (visible && !ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
                skinViewer.setVisibility(View.VISIBLE);
                skinViewer.onResume();
                // 纹理由渲染线程 onSurfaceCreated 从 renderer.texture 自行重建，
                // 此处不重喂纹理；同账户已在 onCreate 加载过，load 会自动跳过
            } else {
                skinViewer.onPause();
                skinViewer.setVisibility(View.GONE);
            }
        }
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (skinLoader != null) {
                skinLoader.load(account, true);
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