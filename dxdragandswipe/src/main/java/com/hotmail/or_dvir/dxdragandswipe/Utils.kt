package com.hotmail.or_dvir.dxdragandswipe

import android.view.View

//region drag
typealias onDragEventListener = (view: View, adapterPosition: Int) -> Unit

typealias onItemMovedListener =
            (draggedView: View, draggedPosition: Int, targetView: View, targetPosition: Int) -> Unit
//endregion

//region swipe
typealias onSwipeEventListener = (view: View, adapterPosition: Int) -> Unit

typealias onItemSwipedListener = (view: View, adapterPosition: Int, direction: Int) -> Unit
//endregion