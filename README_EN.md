<!DOCTYPE text/markdown>
<html lang="zh-CN" style="">
<head><meta charset="UTF-8"/></head>
<body>
<br/><br/>
<div align="center">
    <img width="75" src="/FCL/src/main/res/drawable/img_app.png"></img>
</div>
<br/>

🌍 **Languages**  
<br/>
[简体中文](./README.md) | **English**  
<br/>

---

<h1 align="center">FCL Modpack Edition</h1>

> Based on the old version of FCL-Server (before 1.1.9.8) by [root-S7](https://github.com/root-S7)  
> More customization features, mainly targeting default settings overriding.  
> [→ Vanilla FCL Repo](https://github.com/FCL-Team/FoldCraftLauncher)  

- **Package the modpack into the launcher**
  - Makes it can be used without additional configuration after installation
  - Rich customization features that override the default settings of the launcher and adapt to your modpack
  - Supports almost all game versions, most mods, and some shaders

---

## 🚀 Core Features

### 🧩 Modpack Edition Features
- [x] Embed configured clients
- [x] Modify the default theme and launcher settings
- [x] Modify the default controller and replace the controller repository
- [x] Modify the default game settings
- [x] Replace the default downloading server
- [x] Replace the launcher update and announcement server
- [x] Integrate features into FCL shell using sh scripts

<details>
<summary markdown='1'>📂 <strong>Vanilla FCL Features</strong></summary>

---

###  🎮 Vanilla FCL Features

> ~~I agree with what you said, but「FCL」is a Minecraft Java Edition launcher made by FCL-Team, built upon the core functionalities of [HMCL](https://github.com/HMCL-dev/HMCL) and integrating the [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) backends. The launcher is used in a system called「Android」, where the person selected by the system will be authorized to use 「JVM」, operating the power of 「Java」. You are going to act a mysterious user called「Greenhand」. During the freely use of the launcher, you can install different versions, make various mods work together and find out the reason of crash, discover the truth of「xxException:」.~~  

#### ✨ Project Introduction  
「Fold Craft Launcher」 is a Minecraft: Java Edition launcher for Android platforms developed by the FCL Team. Built upon the core functionalities of [HMCL](https://github.com/HMCL-dev/HMCL) and using the [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android) backend, it enables users to enjoy Java Edition MC on mobile devices, supporting mod loading and operation across all versions.

#### ✅ Full Version Support  
- Native support for all Minecraft versions (including latest snapshots)
- Mod loader support: Forge/NeoForge/LiteLoader/OptiFine/Fabric/Quilt/Cleanroom...

#### ⚙️ Key Highlights  
- Built-in multi-version Java runtimes (Java 8/17/21/25) with custom Java import support
- Virtual mouse and customizable key mapping
- FCL↔ZalithLauncher2 control layout conversion, importing a ZL2 layout converts it automatically
- Shaders support (requires VirGL/Zink/MG renderers)
- Dynamic resource management (mods/modpacks/textures/shaders/saves)
- Personalized theme customization (background/color schemes)
- Supports [renderer pluginization](https://github.com/ShirosakiMio/FCLRendererPlugin)

</details>

---

## 🤝 Build and Use
### ⬇️ Download to Use
You can download the automatic builds from Actions in the repository
- It is recommended to use workflows without `test`
- It is recommended to use the `main` branch
- It is recommended to use [MT Manager](https://mt2.cn/) to modify the apk
  - Embed your client in `assets/.minecraft`
  - Modify your configurations in `assets/local.properties`
  - More information can be found in [Documents](https://github.com/hyplant-team/FoldCraftLauncher/tree/doc)

<details>
<summary markdown='1'>📂 <strong>Build Guide</strong></summary>

---

### 📦 Build Guide
You can use Android Studio to automate configuration and build

#### 🛠️ Setup the Environment
- Gradle: `gradle-8.13-bin`
  - android-application: `8.13.2`
  - android-library: `8.13.2`
  - kotlin-android: `2.0.21`
- Android SDK
  - platforms: `android-35`
  - build-tools: `34.0.0`
  - ndk: `27.0.12077973`
  - cmake: `3.22.1`
- More...
  - See [libs version file](gradle/libs.versions.toml) for details.  
  > Differs to the vanilla version, `targetSdk` has been set to `28` to implement the FCL Shell extension
  > ```Toml
  > compileSdk = "35"
  > minSdk = "26"
  > targetSdk = "28"
  > ```

#### 🪛 Command-line Flags

- `"-Darch=all"`: Compiled architecture, affecting libs and built-in JRE
  - `all`: all of the following, and the package will be large
  - `arm`: only `armeabi-v7a`, for old phones
  - `arm64`: only `arm64-v8a`, for most phones
  - `x86`: only `x86`, for old computers with Android
  - `x86_64`: only `x64`, for most computers with Android
  
- `"-DpkgName=com.tungsten.fcl.modpack"`: Customize the package name
  - Makes it possible to install different modpacks editions and official/official debug versions together
  - A package name of some popular large game may active Game Mode on some devices, and may improve game performance
  - Default value: `com.tungsten.fcl.modpack`
  
- `"-DappName=FCL modpack"`: Customize the application name
  - App name will show bellow app icon on desktop, in app details and FCL main page hint text
  - Default value: `FCL modpack`
</details>

---

## 📜 License

Vanilla FCL is licensed under **[GPL-3.0 License](https://www.gnu.org/licenses/gpl-3.0.html)**

### 🔗 Related Projects
- **[Vanilla FCL](https://github.com/FCL-Team/FoldCraftLauncher)**
  - [HMCL](https://github.com/HMCL-dev/HMCL): source of core functionality (fclcore ported from `org.jackhuang.hmcl`)
  - [Boat and related projects](https://github.com/AOF-Dev/Boat)
  - [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android) (PojavLauncher's Android fork): JVM launching and rendering backend
  - [authlib-injector](https://github.com/yushijinhun/authlib-injector)
  - [EasyTier](https://github.com/EasyTier/EasyTier): underlying mesh networking for LAN play
  - [Terracotta](https://github.com/burningtnt/Terracotta): LAN play solution based on EasyTier (Terracotta module JNI wrapper)
  - [TouchController](https://github.com/TouchController/TouchController): touch controller dependency
  - [NG-GL4ES](https://github.com/ShirosakiMio/NG-GL4ES): gl4es fork renderer
  - [FCLRendererPlugin](https://github.com/ShirosakiMio/FCLRendererPlugin): renderer plugin extension
  - [FCLDriverPlugin](https://github.com/FCL-Team/FCLDriverPlugin): driver (Turnip etc.) plugin extension
- **[root-S7 FCL-Server](https://github.com/root-S7/FoldCraftLauncher)**
  - Old commits have been deleted, Some are reserved in [my old repo](https://github.com/hyplant/FoldCraftLauncherModpack-old)
- **Other Dependencies**

<ul>
<details close>
<summary>📂 open source libraries used by upstream project</summary>
<ul>

| Library | Copyright | License | Official Link |
|---|---|---|---|
| Amethyst-Android (Android fork of PojavLauncher) | AngelAuraMC | GPL-3.0 | https://github.com/AngelAuraMC/Amethyst-Android |
| AndroidX (appcompat / constraintlayout / core-ktx / core-splashscreen / datastore / lifecycle / material / palette / recyclerview / viewpager2 / room) | The Android Open Source Project | Apache-2.0 | https://developer.android.com/jetpack |
| ANGLE | The ANGLE Project Authors | BSD-3-Clause | https://chromium.googlesource.com/angle/angle |
| Apache Commons (IO / Compress) | The Apache Software Foundation | Apache-2.0 | https://commons.apache.org/ |
| bhook (ByteHook, exit code trapping) | ByteDance | MIT | https://github.com/bytedance/bhook |
| Caciocavallo / cacio-tta (AWT support) | FCL-Team | GPL-2.0 + Classpath Exception | https://github.com/FCL-Team/caciocavallo17 |
| chardet (charset detection) | Glavo | MPL-1.1 / GPL-3.0 / LGPL-3.0 | https://github.com/Glavo/java-chardet |
| constant-pool-scanner | Jenkins community | CDDL-1.0 / GPL-2.0 | https://github.com/jenkinsci/constant-pool-scanner |
| control-converter (semantics baseline of the FCL↔ZL2 control layout converter) | NingZeStudio | MIT | https://github.com/anylyralabs/control-converter |
| discord-rpc | Discord Inc. | MIT | https://github.com/discord/discord-rpc |
| EasyTier (networking layer embedded in Terracotta) | EasyTier Authors | LGPL-3.0 | https://github.com/EasyTier/EasyTier |
| Gamepad Remapper | Mathias-Boulay | LGPL-3.0 | https://github.com/Mathias-Boulay/android_gamepad_remapper |
| Glide | Google Inc. | Apache-2.0 | https://github.com/bumptech/glide |
| Gson | Google Inc. | Apache-2.0 | https://github.com/google/gson |
| jelf | fornwall | MIT | https://github.com/fornwall/jelf |
| JNA (Java Native Access) | JNA contributors | Apache-2.0 / LGPL-2.1 | https://github.com/java-native-access/jna |
| jsoup | Jonathan Hedley | MIT | https://github.com/jhy/jsoup |
| junrar | junrar contributors | UnRAR freeware license | https://github.com/junrar/junrar |
| Kotlin (kotlin-stdlib / kotlinx-coroutines / kotlinx-serialization) | JetBrains s.r.o. | Apache-2.0 | https://github.com/JetBrains/kotlin |
| LWJGL 3 (official jars + Android source patches) | Lightweight Java Game Library Project | BSD-3-Clause | https://github.com/LWJGL/lwjgl3 |
| lz4-java | Glavo | Apache-2.0 | https://github.com/Glavo/lz4-java |
| Mesa 3D (OSMesa / Kopper-Zink) | The Mesa Authors | MIT | https://gitlab.freedesktop.org/mesa/mesa |
| MobileGlues | MobileGL-Dev | LGPL-2.1 | https://github.com/MobileGL-Dev/MobileGlues |
| NanoHTTPD | NanoHTTPD contributors | BSD-3-Clause | https://github.com/nanohttpd/nanohttpd |
| NG-GL4ES (gl4es fork renderer) | BZLZHH / ShirosakiMio | MIT | https://github.com/ShirosakiMio/NG-GL4ES |
| oboe | Google Inc. | Apache-2.0 | https://github.com/google/oboe |
| OpenAL Soft | Chris Robinson | LGPL-2.1 | https://github.com/kcat/openal-soft |
| OpenJDK (multi-arch runtimes, built and released by FCL-Team) | Oracle / OpenJDK contributors | GPL-2.0 + Classpath Exception | https://github.com/FCL-Team/Android-OpenJDK-Build |
| OpenNBT | Steveice10 (GeyserMC) | MIT | https://github.com/GeyserMC/OpenNBT |
| pffft (embedded in OpenAL Soft) | Julien Pommier | All rights reserved (ARR) | https://bitbucket.org/jpommier/pffft/src/master/ |
| SDL3 / SDL2 | Sam Lantinga | Zlib | https://github.com/libsdl-org/SDL |
| SimpleFPEWrapper | MobileGL-Dev | LGPL-3.0 | https://github.com/MobileGL-Dev/SimpleFPEWrapper |
| SPIRV-Cross (SPIR-V reflection/translation, natives aar) | Khronos Group | Apache-2.0 | https://github.com/KhronosGroup/SPIRV-Cross |
| Terracotta (JNI wrapper of the multiplayer module) | burningtnt | AGPL-3.0 | https://github.com/burningtnt/Terracotta |
| TouchController (touch controller proxy-client-android) | fifthlight | LGPL-3.0 | https://github.com/TouchController/TouchController |
| tomlj | tomlj contributors | Apache-2.0 | https://github.com/tomlj/tomlj |
| virglrenderer | The Virgl Project | MIT | https://gitlab.freedesktop.org/virgl/virglrenderer |
| XZ for Java | Lasse Collin | 0BSD | https://tukaani.org/xz/java.html |
| ZipFileSystem (port of OpenJDK zipfs) | OpenJDK contributors | GPL-2.0 + Classpath Exception | https://github.com/openjdk/jdk |

</ul>
</details>
</ul>

<details>
<summary>📂 About The README.md</summary>

---

### 📝 About The README_EN.md
This document refers to the README.md of the original FCL and root-S7 FCL-Server  
Some parts are translated by AI or online translators
</details>

---

</body></html>
