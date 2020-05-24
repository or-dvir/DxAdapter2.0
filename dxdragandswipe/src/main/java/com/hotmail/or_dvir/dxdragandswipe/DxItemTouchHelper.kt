package com.hotmail.or_dvir.dxdragandswipe

import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper

/**
 * a convenience wrapper for [ItemTouchHelper] what allows dragging with a handle
 */
class DxItemTouchHelper(private val itemTouchCallback: DxItemTouchCallback) :
    ItemTouchHelper(itemTouchCallback) {

    /**
     * enabled dragging items with a handle
     * @param handleId the resource id of the handle used to initiate the drag
     */
    fun setDragHandleId(@IdRes handleId: Int) =
        itemTouchCallback.setUpDragWithHandle(handleId, this)
}