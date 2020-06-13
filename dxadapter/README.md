# DxAdapter
The base module for the entire library. It includes the most basic
classes and interfaces.

## Import
//todo coming soon

## Your Adapter
Simply extend `DxAdapter`

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

