<!DOCTYPE text/markdown>
<html lang="zh-CN" style="">
<head><meta charset="UTF-8"/></head>
<body>
<br/><br/>
<div align="center">
    <img width="75" src="/FCL/src/main/res/drawable/img_app.png"></img>
</div>
<br/>

🌍 **语言/Languages：**  
**简体中文** | [English](./README_EN.md)  
<br/>

---

<h1 align="center">FCL 整合包版</h1>

> 基于曾经的 [root-S7](https://github.com/root-S7) 服务端直装版（1.1.9.8及之前）进行修改  
> 加入更多自定义功能，主要针对默认设置覆盖。  
> [→ 查看原版FCL仓库](https://github.com/FCL-Team/FoldCraftLauncher)  

- **把整合包打包进启动器**
  - 用户安装后无需额外的配置即可启动
  - 丰富的自定义功能，覆盖启动器的默认设置，适配您的整合包
  - 支持几乎所有游戏版本、绝大多数模组和部分光影

## 🚀 核心特性

### 🧩 整合包直装功能
- [x] 可嵌入配置好的客户端
- [x] 可修改默认主题、启动器设置
- [x] 可修改默认控制器、替换控制器仓库
- [x] 可修改默认游戏设置
- [x] 可替换默认下载服务器
- [x] 可替换启动器更新、公告服务器
- [x] 可使用 sh 脚本在 FCL Shell 中集成很多功能

<details>
<summary markdown='1'>📂 <strong>原版FCL功能</strong></summary>

---

###  🎮 原版FCL功能

> ~~你说得对，但是[「FoldCraft Launcher」](https://github.com/FCL-Team/FoldCraftLauncher)是由[FCL-Team](https://github.com/FCL-Team)基于[HMCL](https://github.com/HMCL-dev/HMCL)的核心功能，使用[PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher)后端开发的[Minecraft](https://www.minecraft.net/) [Java](https://baike.baidu.com/item/Java%E5%B9%B3%E5%8F%B0/3793459)版启动器。启动器运行在一个被称作[「安卓」](https://baike.baidu.com/item/android/60243)的系统，在这里，被系统选中的人将被授予「JVM」，导引[Java](https://baike.baidu.com/item/Java%E5%B9%B3%E5%8F%B0/3793459)之力。你将扮演一位名为「小白」的神秘用户，在自由的使用中安装不同版本、各有千秋独特的[模组](https://baike.baidu.com/item/%E6%A8%A1%E7%BB%84/58377440)，和它们一起运行，找出崩溃闪退的原因，同时逐步发掘「xxException:」的真相。~~

#### ✨ 项目简介  
「Fold Craft Launcher」是由FCL团队开发的Android平台Minecraft: Java Edition启动器。基于[HMCL](https://github.com/HMCL-dev/HMCL)的核心功能，使用[Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android)后端，让您能在移动设备上畅玩Java版MC，支持模组加载与全版本运行。

#### ✅ 全版本支持  
- 原生支持 Minecraft 全版本（包括最新快照）
- 模组加载器支持：Forge/NeoForge/LiteLoader/OptiFine/Fabric/Quilt/Cleanroom...

#### ⚙️ 功能亮点  
- 内置多版本 Java 运行时（Java 8/17/21/25）同时支持导入Java
- 虚拟鼠标与自定义按键映射
- 支持 FCL/ZalithLauncher2 控制布局互转，导入 ZL2 布局自动转换
- 光影支持（需VirGL/Zink/MG渲染器）
- 动态资源管理（模组/整合包/材质/光影/存档）
- 个性化主题定制（背景/颜色方案）
- 支持[渲染器插件化](https://github.com/ShirosakiMio/FCLRendererPlugin)

</details>

---

## 🤝 构建与使用
### ⬇️ 下载使用
可以从该仓库的 Actions 中下载自动构建版
- 建议使用不带`test`的工作流
- 建议使用`main`分支
- 建议使用 [MT管理器](https://mt2.cn/) 修改 apk
  - 在`assets/.minecraft`嵌入您的客户端
  - 在`assets/local.properties`修改您的配置
  - 更多信息详见[文档](https://github.com/hyplant-team/FoldCraftLauncher/tree/doc)

<details>
<summary markdown='1'>📂 <strong>构建指南</strong></summary>

---

### 📦 构建指南
您可以使用 Android Studio 自动化配置和构建

#### 🛠️ 配置环境
- Gradle: `gradle-8.13-bin`
  - android-application: `8.13.2`
  - android-library: `8.13.2`
  - kotlin-android: `2.0.21`
- Android SDK
  - platforms: `android-35`
  - build-tools: `34.0.0`
  - ndk: `27.0.12077973`
  - cmake: `3.22.1`
- 更多信息...
  - 查看 [依赖库版本文件](gradle/libs.versions.toml) 以了解详情。
  > 与官方版不同，`targetSdk`设为`28`以实现 FCL Shell 功能扩展
  > ```Toml
  > compileSdk = "35"
  > minSdk = "26"
  > targetSdk = "28"
  > ```

#### 🪛 命令行参数

- `"-Darch=all"`：编译的架构，会影响libs和内置jre
  - `all`：以下全部保留，安装包体积较大
  - `arm`：仅保留`armeabi-v7a`，适用于较旧的手机
  - `arm64`：仅保留`arm64-v8a`，适用于大多数手机
  - `x86`：仅保留`x86`，适用于装有Android的老电脑
  - `x86_64`：仅保留`x64`，适用于装有Android的大多数电脑
  
- `"-DpkgName=com.tungsten.fcl.modpack"`：自定义安装包的包名
  - 用于实现不同整合包版和官方版/官方调试版共存
  - 在部分手机上，使用某些主流大型游戏的包名，可激发手机的游戏模式，提升性能
  - 留空时使用的默认值：`com.tungsten.fcl.modpack`
  
- `"-DappName=FCL modpack"`：自定义安装包的包名
  - 自定义应用程序的名称，会在桌面图标、应用程序详情、 FCL主页提示文本中显示
  - 留空时使用的默认值：`FCL modpack`
</details>

---

## 📜 开源协议

原版 FCL 采用 **[GPL-3.0 License](https://www.gnu.org/licenses/gpl-3.0.html)** 授权

### 🔗 相关项目

- **[原版 FCL](https://github.com/FCL-Team/FoldCraftLauncher)**
  - [HMCL](https://github.com/HMCL-dev/HMCL)：核心功能来源（fclcore 移植自 `org.jackhuang.hmcl`）
  - [Boat 及其相关项目](https://github.com/AOF-Dev/Boat)
  - [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android)（PojavLauncher Android fork）：JVM 启动与渲染后端
  - [authlib-injector](https://github.com/yushijinhun/authlib-injector)
  - [EasyTier](https://github.com/EasyTier/EasyTier)：局域网联机组网底层
  - [Terracotta](https://github.com/burningtnt/Terracotta)：基于 EasyTier 的联机方案（Terracotta 模块 JNI 封装）
  - [TouchController](https://github.com/TouchController/TouchController)：触摸控制器依赖
  - [NG-GL4ES](https://github.com/ShirosakiMio/NG-GL4ES)：gl4es fork 渲染器
  - [FCLRendererPlugin](https://github.com/ShirosakiMio/FCLRendererPlugin)：渲染器插件扩展
  - [FCLDriverPlugin](https://github.com/FCL-Team/FCLDriverPlugin)：驱动（Turnip 等）插件扩展
- **[root-S7 FCL服务器直装版](https://github.com/root-S7/FoldCraftLauncher)**
  - 旧的提交已经删除，[我的旧版仓库](https://github.com/hyplant/FoldCraftLauncherModpack-old)保留了部分提交
- **其他依赖**

<ul>
<details close>
<summary>📂 上游项目使用的其他开源库</summary>
<ul>

| 库 | 版权 | 许可证 | 官方链接 |
|---|---|---|---|
| Amethyst-Android（PojavLauncher Android fork） | AngelAuraMC | GPL-3.0 | https://github.com/AngelAuraMC/Amethyst-Android |
| AndroidX（appcompat / constraintlayout / core-ktx / core-splashscreen / datastore / lifecycle / material / palette / recyclerview / viewpager2 / room） | The Android Open Source Project | Apache-2.0 | https://developer.android.com/jetpack |
| ANGLE | The ANGLE Project Authors | BSD-3-Clause | https://chromium.googlesource.com/angle/angle |
| Apache Commons（IO / Compress） | The Apache Software Foundation | Apache-2.0 | https://commons.apache.org/ |
| bhook（ByteHook，exit code 捕获） | ByteDance | MIT | https://github.com/bytedance/bhook |
| Caciocavallo / cacio-tta（AWT 支持） | FCL-Team | GPL-2.0 + Classpath Exception | https://github.com/FCL-Team/caciocavallo17 |
| chardet（编码探测） | Glavo | MPL-1.1 / GPL-3.0 / LGPL-3.0 | https://github.com/Glavo/java-chardet |
| constant-pool-scanner | Jenkins 社区 | CDDL-1.0 / GPL-2.0 | https://github.com/jenkinsci/constant-pool-scanner |
| control-converter（FCL↔ZL2 控制布局转换语义基准） | NingZeStudio | MIT | https://github.com/anylyralabs/control-converter |
| discord-rpc | Discord Inc. | MIT | https://github.com/discord/discord-rpc |
| EasyTier（Terracotta 内嵌组网底层） | EasyTier Authors | LGPL-3.0 | https://github.com/EasyTier/EasyTier |
| Gamepad Remapper | Mathias-Boulay | LGPL-3.0 | https://github.com/Mathias-Boulay/android_gamepad_remapper |
| Glide | Google Inc. | Apache-2.0 | https://github.com/bumptech/glide |
| Gson | Google Inc. | Apache-2.0 | https://github.com/google/gson |
| jelf | fornwall | MIT | https://github.com/fornwall/jelf |
| JNA（Java Native Access） | JNA contributors | Apache-2.0 / LGPL-2.1 | https://github.com/java-native-access/jna |
| jsoup | Jonathan Hedley | MIT | https://github.com/jhy/jsoup |
| junrar | junrar contributors | UnRAR freeware license | https://github.com/junrar/junrar |
| Kotlin（kotlin-stdlib / kotlinx-coroutines / kotlinx-serialization） | JetBrains s.r.o. | Apache-2.0 | https://github.com/JetBrains/kotlin |
| LWJGL 3（官方 jar + Android 源码补丁） | Lightweight Java Game Library Project | BSD-3-Clause | https://github.com/LWJGL/lwjgl3 |
| lz4-java | Glavo | Apache-2.0 | https://github.com/Glavo/lz4-java |
| Mesa 3D（OSMesa / Kopper-Zink） | The Mesa Authors | MIT | https://gitlab.freedesktop.org/mesa/mesa |
| MobileGlues | MobileGL-Dev | LGPL-2.1 | https://github.com/MobileGL-Dev/MobileGlues |
| NanoHTTPD | NanoHTTPD contributors | BSD-3-Clause | https://github.com/nanohttpd/nanohttpd |
| NG-GL4ES（gl4es fork 渲染器） | BZLZHH / ShirosakiMio | MIT | https://github.com/ShirosakiMio/NG-GL4ES |
| oboe | Google Inc. | Apache-2.0 | https://github.com/google/oboe |
| OpenAL Soft | Chris Robinson | LGPL-2.1 | https://github.com/kcat/openal-soft |
| OpenJDK（多架构运行时，FCL-Team 自建发布） | Oracle / OpenJDK contributors | GPL-2.0 + Classpath Exception | https://github.com/FCL-Team/Android-OpenJDK-Build |
| OpenNBT | Steveice10（GeyserMC） | MIT | https://github.com/GeyserMC/OpenNBT |
| pffft（OpenAL Soft 内嵌） | Julien Pommier | 保留所有权利（ARR） | https://bitbucket.org/jpommier/pffft/src/master/ |
| SDL3 / SDL2 | Sam Lantinga | Zlib | https://github.com/libsdl-org/SDL |
| SimpleFPEWrapper | MobileGL-Dev | LGPL-3.0 | https://github.com/MobileGL-Dev/SimpleFPEWrapper |
| SPIRV-Cross（SPIR-V 反射/转换，natives aar） | Khronos Group | Apache-2.0 | https://github.com/KhronosGroup/SPIRV-Cross |
| Terracotta（联机模块 JNI 封装） | burningtnt | AGPL-3.0 | https://github.com/burningtnt/Terracotta |
| TouchController（触摸控制器 proxy-client-android） | fifthlight | LGPL-3.0 | https://github.com/TouchController/TouchController |
| tomlj | tomlj contributors | Apache-2.0 | https://github.com/tomlj/tomlj |
| virglrenderer | The Virgl Project | MIT | https://gitlab.freedesktop.org/virgl/virglrenderer |
| XZ for Java | Lasse Collin | 0BSD | https://tukaani.org/xz/java.html |
| ZipFileSystem（OpenJDK zipfs 移植模块） | OpenJDK contributors | GPL-2.0 + Classpath Exception | https://github.com/openjdk/jdk |

</ul>
</details>
</ul>

<details>
<summary>📂 关于此 README.md</summary>

---

### 📝 关于此 README.md
此文档参考了 原版FCL 和 root-S7修改版 的 README.md
</details>

---

</body></html>
