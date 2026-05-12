package com.mio.util

import android.content.Context
import com.tungsten.fcl.R

fun getLauncherName(context: Context): String {
    val versionType = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        .getString("custom_launcher_name", "")
    return if (versionType == null) "" else versionType
}