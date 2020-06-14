# DxDragAndSwipe
A module adding dragging and swiping capability to your adapter.

Built-in Features:
* Setting drag/swipe directions.
* User interaction listeners (drag/swipe event start/end).
* Flag for enabling/disabling drag/swipe at any time.
* Item dragged/swiped listeners.  
  Please carefully read the documentation for these listeners for
  some important notes.
* Dragging by long-click or by handle.
* Swipe threshold.
* Swipe escape velocity.
* Swipe backgrounds with color, text, and icon (may be static or dynamic according to item state).

For a complete example please see the sample app.

## Import
in your `build.gradle` file add:

```
repositories {
    //if already added, skip this one
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.or-dvir.DxAdapterV2:dxdragandswipe:<latest release>'
}
```

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

## Dependencies Exposed to User
* `androidx.recyclerview:recyclerview`

## Depends On
* [DxAdapter](https://github.com/or-dvir/DxAdapterV2/tree/master/dxadapter)

