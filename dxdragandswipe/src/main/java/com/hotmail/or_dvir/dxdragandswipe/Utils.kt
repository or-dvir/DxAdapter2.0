package com.hotmail.or_dvir.dxdragandswipe

import android.view.View

//region drag
typealias onDragEventListener = (view: View, adapterPosition: Int) -> Unit

typealias onItemMovedListener =
            (draggedView: View, draggedPosition: Int, targetView: View, targetPosition: Int) -> Unit
//endregion

//region swipe
//NOTE: even though its the same signature as onItemSwipedListener, keep them separate!
// this will help prevent future bugs
typealias onSwipeEventListener = (view: View, adapterPosition: Int, direction: Int) -> Unit
//typealias onSwipeEventListener = onDragEventListener //NOT A MISTAKE! same signature

typealias onItemSwipedListener = (view: View, adapterPosition: Int, direction: Int) -> Unit
//endregion