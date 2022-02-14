#!/usr/bin/env bash
: ${1?"Version missing - usage: $0 x.y.z"}

#output current directory
pwd
ls -al

#update build.gradle
sed -i '' "s/versionName = \".*-SNAPSHOT/versionName = \"$1-SNAPSHOT/g" core-sdk-samples/higgs-shop-sample-app/app/build.gradle.kts

#update README.md

#commit the version bump, tag, and push to private and public
git add :/*.kts
#git add README.md

#create and rename release APK for Github
./gradlew assembleDebug
mkdir dist
mv core-sdk-samples/higgs-shop-sample-app/app/build/outputs/apk/debug/app-debug.apk "dist/HiggsShopSampleApp-$1.apk"