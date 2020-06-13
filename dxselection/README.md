# DxSelection
A module adding selection capabilities to your adapter.

Built-in Features:
* Selection listener.
* Selection mode listener.
* Optional default behaviour (first selection by long-click, and
next selection/deselection by regular click).

For a complete example please see the sample app.

## Import
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

## Built-in Dependencies:
None.