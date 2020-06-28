# DxSelection
This module adds selection capabilities to your adapter.

Built-in Features:
* Selection listener
* Selection mode listener
* Optional default behaviour (first selection by long-click, and
next selection/deselection by regular click)

For a complete example, please see the sample app.

## Import
in your `build.gradle` file, add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxselection:<latest release>'
}
```

## Your Adapter
```
val myClickFeature = DxFeatureClick<MyItem>(...) //required for selection feature
val mySelectionFeature = DxFeatureSelection<MyItem>(...)

//click feature is automatically added in this case
adapter.addFeature(mySelectionFeature)
```

## Your Items
Your item must implement the interface `IDxItemSelectable`.

## Dependencies Exposed to User
None.

## Depends On
* [DxAdapter](https://github.com/or-dvir/DxAdapterV2/tree/master/dxadapter)
* [DxClick](https://github.com/or-dvir/DxAdapterV2/tree/master/dxclick)



