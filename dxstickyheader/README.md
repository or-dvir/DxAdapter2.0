# DxStickyHeader
A module adding sticky headers to your  adapter.

For a complete example please see the sample app.

## Import
//todo coming soon

## Your Adapter and RecyclerView
1. Extend the class `DxFeatureStickyHeader<>`.
2. Pass it to `DxStickyHeaderItemDecoration()`.
3. Add `DxStickyHeaderItemDecoration()` to your RecyclerView.

```
val featureHeader = MyStickyHeaderFeature(...)
adapter.addFeature(featureHeader)

val decoration = DxStickyHeaderItemDecoration(featureHeader)
recyclerView.addItemDecoration(decoration)
```

## Your Items
1. Create a layout for your header.
2. Your item must implement the interface `IDxItemHeader`.

## Built-in Dependencies:
None.
