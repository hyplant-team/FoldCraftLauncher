<!DOCTYPE text/markdown>
<html lang="zh-CN" style="">
<head><meta charset="UTF-8"/></head>
<body>
<br/><br/>
<div align="center">
    <img width="75" src="/FCL/src/main/res/drawable/img_app.png"></img>
</div>
<br/>

🌍 **语言/Languages**  
<br/>
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

###  ✨ 原版FCL功能

> ~~你说得对，但是「FCL」是由FCL-Team基于[HMCL](https://github.com/HMCL-dev/HMCL)的核心功能，结合[PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher)和[Boat](https://github.com/AOF-Dev/Boat)后端开发的Minecraft Java版启动器。启动器运行在一个被称作「安卓」的系统，在这里，被系统选中的人将被授予「JVM」，导引「Java」之力。你将扮演一位名为「小白」的神秘用户，在自由的使用中安装不同版本、各有千秋独特的模组，和它们一起运行，找出崩溃闪退的原因，同时逐步发掘「xxException:」的真相。~~  

> **项目简介**  
> 「Fold Craft Launcher」是由FCL团队开发的Android平台Minecraft: Java Edition启动器。基于[HMCL](https://github.com/HMCL-dev/HMCL)核心功能，结合[PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher)后端和[Boat](https://github.com/AOF-Dev/Boat)后端，让您能在移动设备上畅玩Java版MC，支持模组加载与全版本运行。

#### ✅ 全版本支持
- 原生支持 Minecraft 全版本（包括远古版和最新快照）
- 模组加载器支持：Forge/NeoForge/LiteLoader/OptiFine/Fabric/Quilt...

#### ⚙️ 功能亮点
- 内置多版本 Java 运行时（Java 8/11/17/21）同时支持导入Java
- 虚拟鼠标与自定义按键映射
- 光影支持（需VirGL/Zink/MG渲染器）
- 动态资源管理（模组/整合包/材质/光影/存档）
- 个性化主题定制（背景/颜色方案）
- 支持渲染器、驱动插件化

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
- Gradle: `gradle-8.9-bin`
  - android-application: `8.7.2`
  - android-library: `8.7.2`
  - kotlin-android: `2.0.21`
- Android SDK
  - platforms: `android-35`
  - build-tools: `34.0.0`
  - ndk: `27.0.12077973`
  - cmake: `3.22.1`
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
- [原版 FCL](https://github.com/FCL-Team/FoldCraftLauncher)
  - [HMCL](https://github.com/HMCL-dev/HMCL)
  - [Boat 及其相关项目](https://github.com/AOF-Dev/Boat)
  - [PojavLauncher 及其相关项目](https://github.com/PojavLauncherTeam/PojavLauncher)
  - [authlib-injector](https://github.com/yushijinhun/authlib-injector)
- [root-S7 FCL服务器直装版](https://github.com/root-S7/FoldCraftLauncher)
  - 旧的提交已经删除，[我的旧版仓库](https://github.com/hyplant/FoldCraftLauncherModpack-old)保留了部分提交
<details>
<summary>📂 关于此 README.md</summary>

---

### 📝 关于此 README.md
此文档参考了 原版FCL 和 root-S7修改版 的 README.md
</details>

---

</body></html>