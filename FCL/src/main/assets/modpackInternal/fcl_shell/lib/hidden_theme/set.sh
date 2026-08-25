#!/system/bin/sh
${fclshell_load_lang} hidden_theme

export FCL_CONF_THEME_THEME_COLOR="-436242312"
export FCL_CONF_THEME_THEME_COLOR2="-8392705"
export FCL_CONF_THEME_FULLSCREEN="false"
export FCL_CONF_THEME_CLOSE_SKIN_MODEL="true"
export FCL_CONF_THEME_ANIMATION_SPEED="8"

export FCL_CONF_THEME="{"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"color\": ${FCL_CONF_THEME_THEME_COLOR},"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"color2\": ${FCL_CONF_THEME_THEME_COLOR2},"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"color2Dark\": ${FCL_CONF_THEME_THEME_COLOR2},"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"fullscreen\": ${FCL_CONF_THEME_FULLSCREEN},"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"closeSkinModel\": ${FCL_CONF_THEME_CLOSE_SKIN_MODEL},"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n    \"animationSpeed\": ${FCL_CONF_THEME_ANIMATION_SPEED}"
export FCL_CONF_THEME="${FCL_CONF_THEME}\n}"
echo -e "${FCL_CONF_THEME}"

mkdir -p "${FCL_PATH_INTERNAL}/files/datastore" >"/dev/null" 2>&1
echo -en "${FCL_CONF_THEME}" >"${FCL_PATH_INTERNAL}/files/datastore/theme.json"


rm -rf "${FCL_PATH_INTERNAL}/files/background" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/cursor.png" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/menu_icon.png" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/menu_icon.gif" >"/dev/null" 2>&1
mkdir -p "${FCL_PATH_INTERNAL}/files/background" >"/dev/null" 2>&1
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/background.png" "${FCL_PATH_INTERNAL}/files/background/lt.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/background.png" "${FCL_PATH_INTERNAL}/files/background/dk.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/cursor.png" "${FCL_PATH_INTERNAL}/files/cursor.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/menu_icon.gif" "${FCL_PATH_INTERNAL}/files/menu_icon.gif"

echo "${lang_hidden_theme_enabled}"
sleep 1s
exec killall -q -2 "${FCL_CONF_PKGID}"
