package com.hotmail.or_dvir.dxdragandswipe

import android.view.View

//region drag
typealias OnDragEventListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit

typealias OnItemMovedListener<ITEM> = (
    draggedView: View, draggedPosition: Int, draggedItem: ITEM,
    targetView: View, targetPosition: Int, targetItem: ITEM
) -> Unit
//endregion

//region swipe
typealias OnSwipeEventListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit

typealias OnItemSwipedListener<ITEM> =
            (view: View, adapterPosition: Int, direction: Int, item: ITEM) -> Unit
//endregion