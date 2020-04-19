package com.hotmail.or_dvir.dxdragandswipe

import android.view.View

typealias onItemDragSwipeInteractionListener = (view: View, adapterPosition: Int) -> Unit

typealias onItemSwipedListener = (view: View, adapterPosition: Int, direction: Int) -> Unit

typealias onItemMovedListener =
            (draggedView: View, draggedPosition: Int, targetView: View, targetPosition: Int) -> Unit

