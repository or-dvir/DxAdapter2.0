# DxRecyclerView
A module adding a custom `RecyclerView` with scroll and visibility listeners.

# Usage

## import
//todo coming soon

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

# Built-in Dependencies:
* `androidx.recyclerview:recyclerview`
