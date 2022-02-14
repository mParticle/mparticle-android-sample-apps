<img src="https://static.mparticle.com/sdk/mp_logo_black.svg" width="280"><br>

# The Higgs Shop Sample App

The Higgs Shop is an example app that implements the mParticle Android SDK to highlight the features and implementation details of the respective SDKs.

The purpose of the app is to highlight the following features:

-   Creating an instance of the mParticle Android SDK
-   Setting up an optimal mParticle Configuration with debugging
-   Sending events and custom attributes to mParticle

## Getting Started

1. Open this repository in Android Studio after cloning

2. Update the `HIGGS_SHOP_SAMPLE_APP_KEY` & `HIGGS_SHOP_SAMPLE_APP_SECRET` variable with your mParticle Android API Key inside app/build.gradle.kts file

-   Visit your [mParticle Workspace](https://app.mparticle.com/setup/inputs/apps) to generate API Credentials

3. Run the Higgs Shop sample app project (inside core-sdk-samples folder) in Android emulator.  This will open the Higgs Shop Sample App

### API Credentials

**NOTE** These Sample Apps require a mParticle account with an API key and Secret.

While the code might run and build without mParticle credentials, the SDKs will not upload events to our servers and will generate errors.

Please visit https://docs.mparticle.com/ for more details on setting up an API Key.

## Support

<support@mparticle.com>

## License

The mParticle Web SDK is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0). See the LICENSE file for more info.