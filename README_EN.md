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
<summary markdown='1'>📂 <strong>原版FCL功能</strong></summary>

---

###  ✨ Vanilla FCL Features

> ~~I agree with what you say, but「FCL」is a Minecraft Java Edition launcher made by FCL-Team, built upon the core functionalities of [HMCL](https://github.com/HMCL-dev/HMCL) and integrating the [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) and [Boat](https://github.com/AOF-Dev/Boat) backends. The launcher is used in a system called「Android」, where the person selected by the system will be authorized to use 「JVM」, operating the power of 「Java」. You are going to act a mysterious user called「Greenhand」. During the freely use of the launcher, you can install different versions, make various mods work together and find out the reason of crash, discover the truth of「xxException:」.~~  

> **Project Introduction**  
> 「Fold Craft Launcher」 is a Minecraft: Java Edition launcher for Android platforms developed by the FCL Team. Built upon the core functionalities of [HMCL](https://github.com/HMCL-dev/HMCL) and integrating the [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) and [Boat](https://github.com/AOF-Dev/Boat) backends, it enables users to enjoy Java Edition MC on mobile devices, supporting mod loading and operation across all versions.

#### ✅ Full Version Support
- Native support for all Minecraft versions (including latest snapshots)
- Mod loader support: Forge/NeoForge/LiteLoader/OptiFine/Fabric/Quilt...

#### ⚙️ Key Highlights
- Built-in multi-version Java runtimes (Java 8/11/17/21) with custom Java import support
- Virtual mouse and customizable key mapping
- Shaders support (requires VirGL/Zink/MG renderers)
- Dynamic resource management (mods/modpacks/textures/shaders/saves)
- Personalized theme customization (background/color schemes)
- Supports renderer and driver pluginization

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
- Gradle: `gradle-8.9-bin`
  - android-application: `8.7.2`
  - android-library: `8.7.2`
  - kotlin-android: `2.0.21`
- Android SDK
  - platforms: `android-35`
  - build-tools: `34.0.0`
  - ndk: `27.0.12077973`
  - cmake: `3.22.1`
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
- [Vanilla FCL](https://github.com/FCL-Team/FoldCraftLauncher)
  - [HMCL](https://github.com/HMCL-dev/HMCL)
  - [Boat and related projects](https://github.com/AOF-Dev/Boat)
  - [PojavLauncher and related projects](https://github.com/PojavLauncherTeam/PojavLauncher)
  - [authlib-injector](https://github.com/yushijinhun/authlib-injector)
- [root-S7 FCL-Server](https://github.com/root-S7/FoldCraftLauncher)
  - Old commits have been deleted, Some are reserved in [my old repo](https://github.com/hyplant/FoldCraftLauncherModpack-old)
<details>
<summary>📂 About The README.md</summary>

---

### 📝 About The README.md
This document refers to the README.md of the original FCL and root-S7 FCL-Server
</details>

---

</body></html>