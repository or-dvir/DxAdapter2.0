# DxSelection
A module adding selection capabilities to your adapter.

# Usage

## import
//todo coming soon

## Your Adapter
```
val myClickFeature = DxFeatureClick<MyItem>(...) //required for selection feature
val mySelectionFeature = DxFeatureSelection<MyItem>(...)

//click feature is automatically added in this case
adapter.addFeature(mySelectionFeature)
```

## Your Items
Your item must implement the interface `IDxItemSelectable`

# Built-in Dependencies:
None.
