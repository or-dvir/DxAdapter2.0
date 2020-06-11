# DxDrag and DxSwipe
These modules add dragging and swiping capability to your adapter.

# Usage

## import
//todo coming soon

## Your Adapter
```
val myDragFeature = DxFeatureDrag<MyItem>()
val mySwipeFeature = DxFeatureSwipe<MyItem>()

adapter.addFeature(myDragFeature)
adapter.addFeature(mySwipeFeature)

val touchCallBack = DxItemTouchCallback(adapter).apply {
            dragFeature = myDragFeature
            swipeFeature = mySwipeFeature
        }
        
DxItemTouchHelper(touchCallBack).apply {
        //setDragHandleId(...) //optional
        attachToRecyclerView(myRecyclerView)
    }
```

## Your Items
Your item must implement the interface `IDxItemDraggable`
and/or `IDxItemSwipeable`.

See sample app for full usage example, including features like
swipe background, text, and icon.

# Built-in Dependencies:
* `androidx.recyclerview:recyclerview`
