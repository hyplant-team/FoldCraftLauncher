package com.tungsten.fcl.upgrade;

import android.annotation.SuppressLint;
import android.content.*;
import android.content.pm.*;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.*;
import com.tungsten.fcl.util.ReadTools;
import com.tungsten.fclcore.task.*;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclauncher.utils.FCLPath;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class UpdateChecker {

    public static final String UPDATE_CHECK_URL = FCLPath.Prop.getProperty("update-detection-url","null://");

    private static UpdateChecker instance;

    public static UpdateChecker getInstance() {
        if (instance == null) {
            instance = new UpdateChecker();
        }
        return instance;
    }

    private boolean isChecking = false;

    public boolean isChecking() {
        return isChecking;
    }

    public UpdateChecker() {

    }

    public Task<?> checkManually(Context context) {
        return check(context, true, true);
    }

    public Task<?> checkAuto(Context context) {
        return check(context, false, false);
    }

    public Task<?> check(Context context, boolean showBeta, boolean showAlert) {
        return Task.runAsync(() -> {
            isChecking = true;
            if (showAlert) {
                Schedulers.androidUIThread().execute(() -> Toast.makeText(context, context.getString(R.string.update_checking), Toast.LENGTH_SHORT).show());
            }
            try {
                String local_version_map = FCLPath.FILES_DIR + "/debug/version_map.json";
                String res;
                if (new File(local_version_map).exists()) {
                    res = ReadTools.readFileTxt(local_version_map);
                } else {
                    res = NetworkUtils.doGet(NetworkUtils.toURL(UPDATE_CHECK_URL));
                }
                ArrayList<RemoteVersion> versions = JsonUtils.GSON.fromJson(res, new TypeToken<ArrayList<RemoteVersion>>(){}.getType());
                isChecking = false;
                for (RemoteVersion version : versions) {
                    if (version.getVersionCode() > getCurrentVersionCode(context)) {
                        if (showBeta || !version.isBeta()) {
                            if (showBeta || !isIgnore(context, version.getVersionCode())) {
                                showUpdateDialog(context, version);
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logging.LOG.log(Level.SEVERE, "Unable to check update", e);
            }
            isChecking = false;
            if (showAlert) {
                Schedulers.androidUIThread().execute(() -> Toast.makeText(context, context.getString(R.string.update_not_exist), Toast.LENGTH_SHORT).show());
            }
        });
    }

    public static int getCurrentVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("无法获取当前应用版本信息，请确保包管理服务未被篡改！");
        }
    }

    private void showUpdateDialog(Context context, RemoteVersion version) {
        Schedulers.androidUIThread().execute(() -> {
            UpdateDialog dialog = new UpdateDialog(context, version);
            dialog.show();
        });
    }

    public static boolean isIgnore(Context context, int code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ignore_update", -1) == code;
    }

    public static void setIgnore(Context context, int code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ignore_update", code);
        editor.apply();
    }

}
