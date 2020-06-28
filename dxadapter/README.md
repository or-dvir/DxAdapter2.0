# DxAdapter
The base module for the entire library, which includes all the basic
classes and interfaces.

## Import
in your `build.gradle`, add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxadapter:<latest release>'
}
```

## Your Adapter
Simply extend `DxAdapter`.

```
MyAdapter(
    var mItems: MutableList<MyItem>
    //possible other variabels
) : DxAdapter<ITEM, VH>() {

    //override required functions
}
```

## Your Items
If you are using only this module, your items must implement the
`IDxBaseItem` interface.

If you use any other module, the required interfaces already extend it.

## Dependencies Exposed to User
* `androidx.recyclerview:recyclerview`.

## Depends On
None.

