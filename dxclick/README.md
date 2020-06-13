# DxClick
A module adding for handling item clicks.

Built-in Features:
* Regular click (tap) listener.
* Long-click listener.

For a complete example please see the sample app.

## Import
in your `build.gradle` file add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxclick:<latest release>'
}
```

## Your Adapter
```
val myClickFeature = DxFeatureClick<MyItem>(...)
adapter.addFeature(myClickFeature)
```

## Your Items
Your item must implement the  interface `IDxItemClickable`

## Dependencies Exposed to User
None.

## Depends On
* [DxAdapter](https://github.com/or-dvir/DxAdapterV2/tree/master/dxadapter)

