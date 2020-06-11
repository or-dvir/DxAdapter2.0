# DxLibraries
 currently under construction


## DxAdapter

//todo make each bullet point into a link for the appropriate module

DxAdapter is a `RecyclerView` adapter with several built-in features:
* Click listeners
* Dragging
* Swiping (including text and icon)
* Expandable items
* Selection listeners
* Sticky Headers
* RecyclerView Scroll and visibility listeners

Each feature is encapsulated in its own module so you can only use
the ones you actually need.

# Usage

## import
Please see the individual modules.

## Your Adapter
Your adapter must extend `DXAdapter` and implement the required methods.
See the sample app for examples.

## Your Items
Please see the individual modules.

## Current Limitations
* Only supports `LinearLayoutManager`
* Only support 1 type of sticky header.

Note that some of these might work (or work partially), but have not
been fully tested.

## Some things to note:
* This library was meant to be used with Kotlin. While it should
  theoretically also work with Java, it was never tested for it.
* While efficiency is definitely an important factor, the priority for
  this library is simplicity and readability of code.
  So if it's important for you to save those few extra milliseconds,
  there's probably some extra code you'll need to write.
* The readme files for each module are only for general information.
  For more in-depth explanations and other important usage notes,
  please see the documentation, the sample app, and the source code.

