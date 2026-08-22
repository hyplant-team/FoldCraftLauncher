package com.tungsten.fcllibrary.component.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color;
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcllibrary.util.ConvertUtils;

/** 主题持久化数据（DataStore，替代原 SharedPreferences("theme")） */
@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class ThemePreference(
    val color: Int = Color.parseColor(FCLPath.Prop.getProperty("default-theme-first-color", "#7F7797CF")),
    val color2: Int = Color.parseColor(FCLPath.Prop.getProperty("default-theme-second-color", "#FF7F7F7F")),
    val color2Dark: Int = Color.parseColor(FCLPath.Prop.getProperty("default-theme-second-color-dark", "#FF7F7F7F")),
    val fullscreen: Boolean = FCLPath.Prop.getProperty("default-fullscreen", "false").equals("true"),
    val closeSkinModel: Boolean = FCLPath.Prop.getProperty("default-close-skin-model", "false").equals("true"),
    val animationSpeed: Int = ConvertUtils.getIntFromStr(FCLPath.Prop.getProperty("default-animation-speed", "0"))
)

val Context.themeDataStore: DataStore<ThemePreference> by dataStore(
    fileName = "theme.json",
    serializer = ThemePreferenceSerializer
)

object ThemePreferenceSerializer : Serializer<ThemePreference> {
    override val defaultValue: ThemePreference = ThemePreference()

    override suspend fun readFrom(input: InputStream): ThemePreference {
        return try {
            Json.decodeFromString<ThemePreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ThemePreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
