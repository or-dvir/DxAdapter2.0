# DxLibraries
 currently under construction


## DxAdapter
DxAdapter is a `RecyclerView` adapter with several built-in features:
* [Click listeners](https://github.com/or-dvir/DxLibraries/tree/develop/dxclick)
* [Dragging and swiping (including text and icon)](https://github.com/or-dvir/DxLibraries/tree/develop/dxdragandswipe)
* [Expandable items](https://github.com/or-dvir/DxLibraries/tree/develop/dxexpansion)
* [Selection listeners](https://github.com/or-dvir/DxLibraries/tree/develop/dxselection)
* [Sticky Headers](https://github.com/or-dvir/DxLibraries/tree/develop/dxstickyheader)
* [RecyclerView Scroll and visibility listeners](https://github.com/or-dvir/DxLibraries/tree/develop/dxrecyclerview)

Each feature is encapsulated in its own module so you can only use
the ones you actually need.

## Import
Please see the individual modules.

## Your Adapter
Your adapter must extend `DXAdapter` and implement the required methods.
See the sample app for examples.

## Your Items
Please see the individual modules.

## Current Limitations
* Only supports `LinearLayoutManager`
* Only support 1 type of sticky header.

## Some things to note:
* Minimum SDK version 21.
* While some features do not need to be manually added to the adapter,
it is still recommended to do so for future updates.
* This library was meant to be used with Kotlin. While it should
  theoretically also work with Java, it was never tested for it.
* While efficiency is definitely an important factor, the priority for
  this library is simplicity and readability of code.
  So if it's important for you to save those few extra milliseconds,
  there's probably some extra code you'll need to write.
* The readme files for each module are only for general information.
  For more in-depth explanations and other important usage notes,
  please see the documentation, the sample app, and the source code.

