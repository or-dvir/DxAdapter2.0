package com.hotmail.or_dvir.dxdragandswipe

import android.view.View
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeBackground

//region drag
typealias OnDragEventListener = (view: View, adapterPosition: Int) -> Unit

typealias OnItemMovedListener =
            (draggedView: View, draggedPosition: Int, targetView: View, targetPosition: Int) -> Unit
//endregion

//region swipe
typealias OnSwipeEventListener = (view: View, adapterPosition: Int) -> Unit

typealias OnItemSwipedListener = (view: View, adapterPosition: Int, direction: Int) -> Unit
//endregion