# DxRecyclerView
This module adds a custom `RecyclerView`.

Built-in Features:
* Scroll listeners with given sensitivity
* Visibility listeners for first and last items

For a complete example, please see the sample app.

## Import
in your `build.gradle` file, add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxrecyclerview:<latest release>'
}
```

## Your Layout
```
<com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
        ... 
/>
```

## Your Activity/Fragment

```
myRecyclerView.onItemsVisibilityListener = DxVisibilityListener(...)

myRecyclerView.onScrollListener = DxScrollListener(...).apply {
    onScrollUp = { ... } //optional
    onScrollDown = { ... } //optional
    onScrollLeft = { ... } //optional
    onScrollRight = { ... } //optional
}
```

## Dependencies Exposed to User
* `androidx.recyclerview:recyclerview`

## Depends On
None.