# DxExpansion
A module adding expandable items to your adapter.

Built-in Features:
* Expand/collapse listeners.
* Expansion of a single item, or multiple items simultaneously.
* Optional default behaviour (expand/collapse by click).

For a complete example please see the sample app.

## Import
in your `build.gradle` file add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxexpansion:<latest release>'
}
```

## Your Adapter
```
val myClickFeature = DxFeatureClick<MyItem>(...) //required for expansion feature
val myExpandFeature = DxFeatureExpansion<MyItem>(...)

//click feature is automatically added in this case
adapter.addFeature(myExpandFeature)
```

## Your ViewHolder
Your ViewHolder must extend `DxFeatureExpansion.ViewHolder`

## Your Items
Your item must implement the interface `IDxItemExpandable`

## Dependencies Exposed to User
None.

## Depends On
* [DxAdapter](https://github.com/or-dvir/DxAdapterV2/tree/master/dxadapter)
* [DxClick](https://github.com/or-dvir/DxAdapterV2/tree/master/dxaclick)
