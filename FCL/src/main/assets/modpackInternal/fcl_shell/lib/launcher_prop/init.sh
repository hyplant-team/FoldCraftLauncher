#!/system/bin/sh
pkgs="$(pm list packages --uid "$(id -u)")" 2>"/dev/null"
if [[ "$?" != "0" ]]; then
  echo "WARN: Local apps are NOT ALLOWED to access Shell on this device, some features may NOT work."
  export FCL_PATH_INTERNAL="${HOME}"
  echo "Using '/sdcard/Download' as external dir."
  export FCL_PATH_EXTERNAL="/sdcard/Download"
else

  for pkg in ${pkgs};do
    export FCL_CONF_PKGID="${pkg/#package:/}"
    unset pkg
    break
  done

  export FCL_PATH_INTERNAL="/data/data/${FCL_CONF_PKGID}"
  FCL_PATH_PKG="$(pm path ${FCL_CONF_PKGID})"
  export FCL_PATH_PKG="${FCL_PATH_PKG/#package:/}"
  appCfg="$(unzip -p "${FCL_PATH_PKG}" "assets/local.properties")"
  FCL_PATH_EXTERNAL="$(echo -n "${appCfg}" | grep "put-directory=")"
  export FCL_PATH_EXTERNAL="/sdcard/${FCL_PATH_EXTERNAL/#put-directory=/}"
  dirSuffix="$(echo -n "${appCfg}" | grep "put-directory-suffix=")"
  dirSuffix="${dirSuffix/#put-directory-suffix=/}"
  if [[ "${dirSuffix}" == "true" ]]; then
    export FCL_PATH_EXTERNAL="${FCL_PATH_EXTERNAL}/${FCL_CONF_PKGID}"
  fi
  unset dirSuffix
  unset appCfg

  nsCfg="$(unzip -p "${FCL_PATH_PKG}" "assets/resolv.conf")"
  FCL_CONF_NSa="$(echo -n "${nsCfg}" | sed -n "1p")"
  export FCL_CONF_NSa="$(echo -n "${FCL_CONF_NSa}" | tr -d "\n")"
  FCL_CONF_NSb="$(echo -n "${nsCfg}" | sed -n "2p")"
  export FCL_CONF_NSb="$(echo -n "${FCL_CONF_NSb}" | tr -d "\n")"
  unset nsCfg
fi
unset pkgs