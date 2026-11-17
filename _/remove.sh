#!/data/data/com.termux/files/usr/bin/bash
cd "$(dirname "$(readlink -f "${0}")")"
export ScriptDir="$(pwd)"
cd "$(dirname "$ScriptDir")"
export ResDir="${ScriptDir}/assets"
export RepoDir="$(pwd)/controllerRepo/repo_json"
echo -e "\e[96m工作目录：\e[37m$(pwd)\n\e[96m正在读取配置...\e[90m"
mapfile -d ""$'\n' -t iconFiles <"$ScriptDir/listIcon.txt"
mapfile -d ""$'\n' -t screenshotFiles <"$ScriptDir/listScreenshot.txt"

echo -e "\e[93m开始替换图标\e[0m"
for iconFile in ${iconFiles[@]}; do
  iconFileR="${ResDir}/icon.png"
  iconHint=""
  iconFileT="${RepoDir}/${iconFile}/icon.png"
  iconFileS="${ResDir}/${iconFile}/icon.png"
  if [ -f "${iconFileS}" ]; then
    iconFileR=${iconFileS}
    iconHint="(专用文件)"
  fi
  echo -en "\e[90m"
  cp -f "${iconFileR}" "${iconFileT}"
  if [ $? -eq 0 ]; then
    echo -e "\e[92m已覆盖\e[35m${iconHint}\e[96m：\e[0m${iconFile}"
  else
    echo -e "\e[91m覆盖失败\e[35m${iconHint}\e[96m：\e[0m${iconFile}"
  fi
done

echo -e "\e[93m开始替换截图\e[0m"
for screenshotFile in ${screenshotFiles[@]}; do
  screenshotFileR="${ResDir}/screenshot.png"
  screenshotHint=""
  screenshotFileT="${RepoDir}/${screenshotFile//\//\/screenshots\/}.png"
  screenshotFileS="${ResDir}/${screenshotFile}.png"
  if [ -f "${screenshotFileS}" ]; then
    screenshotFileR=${screenshotFileS}
    screenshotHint="(专用文件)"
  fi
  echo -en "\e[90m"
  cp -f "${screenshotFileR}" "${screenshotFileT}"
  if [ $? -eq 0 ]; then
    echo -e "\e[92m已覆盖\e[35m${screenshotHint}\e[96m：\e[0m${screenshotFile}"
  else
    echo -e "\e[91m覆盖失败\e[35m${screenshotHint}\e[96m：\e[0m${screenshotFile}"
  fi
done