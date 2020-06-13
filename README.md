# DxAdapter 2.0
 currently under construction


## DxAdapter
DxAdapter is a `RecyclerView` adapter with several built-in features:
* [Click listeners](https://github.com/or-dvir/DxLibraries/tree/master/dxclick)
* [Dragging and swiping (including text and icon)](https://github.com/or-dvir/DxLibraries/tree/master/dxdragandswipe)
* [Expandable items](https://github.com/or-dvir/DxLibraries/tree/master/dxexpansion)
* [Selection listeners](https://github.com/or-dvir/DxLibraries/tree/master/dxselection)
* [Sticky Headers](https://github.com/or-dvir/DxLibraries/tree/master/dxstickyheader)
* [RecyclerView Scroll and visibility listeners](https://github.com/or-dvir/DxLibraries/tree/master/dxrecyclerview)

Each feature is encapsulated in its own module so you can only use
the ones you actually need.

## Import
Please see the individual modules.

## Your Adapter
Your adapter must extend `DxAdapter`, and you must add each desired feature
using the `addFeature(...)` function.

* Note: some features are automatically added, but it's still recommended
that you add them manually in case this changes in a future update.

Note that there are some functions you should not override directly,
but instead use the library supplied version. Those functions should note
this in their documentation.

See the sample app for examples.

## Your Items
The most basic interface for items in this library is `IDxBaseItem`.

Each feature has its own interface your items need to implement,
which already extends `IDxBaseItem` (so you don't need both).

Please see the individual modules` readme files for details.

## Current Limitations
* Only supports `LinearLayoutManager`
* Only support 1 type of sticky header.

## Some things to note:
* Minimum SDK version 21.
* This library was meant to be used with Kotlin. While it should
  theoretically also work with Java, it was never tested for it.
* While efficiency is definitely an important factor, the priority for
  this library is simplicity and readability of code.
  So if it's important for you to save those few extra milliseconds,
  there's probably some extra code you'll need to write.
* The readme files for each module are only for general information.
  For more in-depth explanations and other important usage notes,
  please see the documentation, the sample app, and the source code.

## License
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
