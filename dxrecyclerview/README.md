# DxRecyclerView
A module containing a custom `RecyclerView`.

Built-in Features:
* Scroll listeners with given sensitivity.
* Visibility listeners for first and last items.

For a complete example please see the sample app.

## Import
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

## Built-in Dependencies:
* `androidx.recyclerview:recyclerview`
