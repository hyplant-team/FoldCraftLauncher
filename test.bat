@echo off
gradlew.bat assembledebug "-Darch=arm64" "-DpkgName=com.tungsten.fcl.modpack_test" "-DappName=FCL test"

timeout /T 3