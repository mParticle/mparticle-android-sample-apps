<img src="https://static.mparticle.com/sdk/mp_logo_black.svg" width="280"><br>

# The Higgs Shop Sample App

The Higgs Shop is an example app that implements the mParticle Android SDK to highlight the features and implementation details of the respective SDKs.

The purpose of the app is to highlight the following features:

-   Creating an instance of the mParticle Android SDK
-   Setting up an optimal mParticle Configuration with debugging
-   Sending events and custom attributes to mParticle

## Getting Started

1. Open this repository in Android Studio after cloning

2. Update the `HIGGS_SHOP_SAMPLE_APP_KEY` and `HIGGS_SHOP_SAMPLE_APP_SECRET` variable with your mParticle Android API Key inside app/build.gradle.kts file

-   Visit your [mParticle Workspace](https://app.mparticle.com/setup/inputs/apps) to generate API Credentials

3. Run the Higgs Shop sample app project (inside core-sdk-samples folder) in Android emulator.  This will open the Higgs Shop Sample App

### API Credentials

**NOTE** These Sample Apps require a mParticle account with an API key and secret.

While the code might run and build without mParticle credentials, the SDKs will not upload events to our servers and will generate errors.

Please visit https://docs.mparticle.com/ for more details on setting up an API Key.

## Events used in this app

To make things simple yet declarative, this application has been built in such a way to keep event tracking close to the components that might logically trigger them rather than a fully DRY implementation. We've opted to be more repetitive so examples are consise and documented as necessary.

Please feel free to also visit our [Doc Site](https://docs.mparticle.com/) to gain more familiarity with some of the more advanced features of mParticle.

### Screen Views

In cases where it is necessary to track visitors as they navigate your Android Application, mParticle offers [Screen View Tracking](https://docs.mparticle.com/developers/sdk/android/screen-tracking/).

For example

```kotlin
val screenInfo = HashMap<String, String>()
screenInfo["rating"] = "5"
screenInfo["property_type"] = "hotel"

MParticle.getInstance().logScreen("Destination Details", screenInfo)
```

In some cases, we fire a _Commerce Event_ instead of a _Page View_ to track more e-Commerce related attributes.

### Custom Events

Most often, you will need to use [Custom Events](https://docs.mparticle.com/developers/sdk/android/event-tracking/#custom-events) to track events in a way that is unique to your use case. mParticle provides types of _Custom Events_ ranging from Navigation Events to Social Media Engagement and are mostly used to organize your data in a way that makes sense to you.

Many of our components in `/src/components` make use of these events, particularly the `NavigationMenuItem` Component.

### Commerce Events

This Sample App emulates a simple e-Commerce application and makes heavy use of mParticle's [Commerce Events](https://docs.mparticle.com/developers/sdk/android/commerce-tracking/).

Some events used in this application:

-   Add To Cart
-   Remove From Cart
-   Product Detail
-   Product Impression
-   Checkout
-   Purchase

Most _Commerce Events_ follow a similar pattern, requiring that you first generate an **mParticle Product** Object, which then gets passed into the `logEvent` method.

You should map your own product attributes to be consistent with your [Data Plan](https://docs.mparticle.com/guides/data-master/introduction/) if you are leveraging that feature. Using Data Plans ensures data consistency within an app and across devices.

```kotlin
// 1. Create the products
val product = Product.Builder("Double Room - Econ Rate", "econ-1", 100.00)
    .quantity(4.0)
    .build()

// 2. Summarize the transaction
val attributes = TransactionAttributes("foo-transaction-id")
    .setRevenue(430.00)
    .setTax(30.00)

// 3. Log the purchase event
val event = CommerceEvent.Builder(Product.PURCHASE, product)
    .transactionAttributes(attributes)
    .build()
MParticle.getInstance().logEvent(event)
```

## Discovering Events

As a developer, sometimes the best way to learn is to just dig into the code or your debugging tools. To that end, this sample app ships with a verbose logger that you can view details of what our SDK is doing within Android Studio's logcat.

### Live Stream

To verify that your events have arrived at mParticle's servers, or to compare your Android Events from that of our other SDKs, you can also visit our [Live Stream](https://docs.mparticle.com/guides/platform-guide/live-stream/).

This will not only show your data as it enters mParticle, but also as your data is forwarded to our various partner services and integrations (if enabled).

### Integrating kits and core SDK locally in the sample app

For information on how to integrate a kit or the core SDK for local configurations refer to [ONBOARDING.md](./ONBOARDING.md)

## Development Notes

This project is built in Kotlin and follows MVVM design patterns and uses Retrofit (networking), Room (ORM/database), & Glide (image loading)

## Support

<support@mparticle.com>

## License

The mParticle Android SDK is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0). See the LICENSE file for more info.
