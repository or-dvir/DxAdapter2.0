# DxExpansion
A module adding expandable items to your adapter.

# Usage

## import
//todo coming soon

## Your Adapter
```
val myClickFeature = DxFeatureClick<MyItem>(...) //required for expansion feature
val myExpandFeature = DxFeatureExpansion<MyItem>(...)

//click feature is automatically added in this case
adapter.addFeature(myExpandFeature)
```

## Your Items
Your item must implement the interface `IDxItemExpandable`

# Built-in Dependencies:
None.
