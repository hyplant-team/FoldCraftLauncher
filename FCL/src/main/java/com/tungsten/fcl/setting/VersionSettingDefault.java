package com.tungsten.fcl.setting;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.platform.MemoryUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class VersionSettingDefault {
    private static String javaArgs = "";
    private static String minecraftArgs = "";
    private static int maxMemory = MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT);
    private static boolean autoMemory = true;
    private static String permSize = "";
    private static String serverIp = "";
    private static String java = "Auto";
    private static int newScaleFactor = 100;
    private static boolean notCheckGame = false;
    private static boolean notCheckJVM = true;
    private static boolean beGesture = false;
    private static boolean vulkanDriverSystem = false;
    private static String controller = "00000000";
    private static String renderer = "18d93f17-ff53-a319-fa61-58709a77bf87";
    private static String driver = "Turnip";
    private static boolean isolateGameDir = false;
    private static boolean pojavBigCore = false;

    static {
        loadDefaultConfig();
    }

    public static String getJavaArgs() {
        return javaArgs;
    }
    public static String getMinecraftArgs() {
        return minecraftArgs;
    }
    public static int getMaxMemory() {
        return maxMemory;
    }
    public static boolean getAutoMemory() {
        return autoMemory;
    }
    public static String getPermSize() {
        return permSize;
    }
    public static String getServerIp() {
        return serverIp;
    }
    public static String getJava() {
        return java;
    }
    public static int getNewScaleFactor() {
        return newScaleFactor;
    }
    public static boolean getNotCheckGame() {
        return notCheckGame;
    }
    public static boolean getNotCheckJVM() {
        return notCheckJVM;
    }
    public static boolean getBeGesture() {
        return beGesture;
    }
    public static boolean getVulkanDriverSystem() {
        return vulkanDriverSystem;
    }
    public static String getController() {
        return controller;
    }
    public static String getRenderer() {
        return renderer;
    }
    public static String getDriver() {
        return driver;
    }
    public static boolean getIsolateGameDir() {
        return isolateGameDir;
    }
    public static boolean getPojavBigCore() {
        return pojavBigCore;
    }

    private static void loadDefaultConfig() {
        File configFile = new File(FCLPath.FILES_DIR + "/default_config.json");
        if (!configFile.exists()) return;
        try (FileReader configFileReader = new FileReader(configFile)) {
            JsonObject defaultConfig = JsonParser.parseReader(configFileReader).getAsJsonObject();
            javaArgs = defaultConfig.has("javaArgs") ? defaultConfig.get("javaArgs").getAsString() : javaArgs;
            minecraftArgs = defaultConfig.has("minecraftArgs") ? defaultConfig.get("minecraftArgs").getAsString() : minecraftArgs;
            maxMemory = defaultConfig.has("maxMemory") ? defaultConfig.get("maxMemory").getAsInt() : maxMemory;
            autoMemory = defaultConfig.has("autoMemory") ? defaultConfig.get("autoMemory").getAsBoolean() : autoMemory;
            permSize = defaultConfig.has("permSize") ? defaultConfig.get("permSize").getAsString() : permSize;
            serverIp = defaultConfig.has("serverIp") ? defaultConfig.get("serverIp").getAsString() : serverIp;
            java = defaultConfig.has("java") ? defaultConfig.get("java").getAsString() : java;
            newScaleFactor = defaultConfig.has("newScaleFactor") ? defaultConfig.get("newScaleFactor").getAsInt() : newScaleFactor;
            notCheckGame = defaultConfig.has("notCheckGame") ? defaultConfig.get("notCheckGame").getAsBoolean() : notCheckGame;
            notCheckJVM = defaultConfig.has("notCheckJVM") ? defaultConfig.get("notCheckJVM").getAsBoolean() : notCheckJVM;
            beGesture = defaultConfig.has("beGesture") ? defaultConfig.get("beGesture").getAsBoolean() : beGesture;
            vulkanDriverSystem = defaultConfig.has("vulkanDriverSystem") ? defaultConfig.get("vulkanDriverSystem").getAsBoolean() : vulkanDriverSystem;
            controller = defaultConfig.has("controller") ? defaultConfig.get("controller").getAsString() : controller;
            renderer = defaultConfig.has("renderer") ? defaultConfig.get("renderer").getAsString() : renderer;
            driver = defaultConfig.has("driver") ? defaultConfig.get("driver").getAsString() : driver;
            isolateGameDir = defaultConfig.has("isolateGameDir") ? defaultConfig.get("isolateGameDir").getAsBoolean() : isolateGameDir;
            pojavBigCore = defaultConfig.has("pojavBigCore") ? defaultConfig.get("pojavBigCore").getAsBoolean() : pojavBigCore;
        } catch (Exception e) {
            Logging.LOG.log(Level.SEVERE, "Failed to load default_config.json", e);
        }
    }

}
