#!/system/bin/sh
lang_version_help="用法：version ([参数]) ([运行时])\n\
获取运行时的版本。\n\
[参数]\n\
    -h, --help\n\
        显示该帮助信息，然后退出\n\
    -n\n\
        末尾不输出换行符\n\
        (只应该在 .sh 脚本中使用，无法在 FCL Shell 中输出！)\n\
[运行时]\n\
        可用的组件：参数为空时列出的组件名称。\n\
        为空时：列出所有组件及其版本\n\
        非空时：获取特定组件的版本"
lang_version_notfound="找不到组件。"