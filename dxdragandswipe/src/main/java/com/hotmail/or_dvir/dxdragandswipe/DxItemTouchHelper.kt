package com.hotmail.or_dvir.dxdragandswipe

import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * a convenience wrapper for [ItemTouchHelper] what allows dragging with a handle
 */
open class DxItemTouchHelper<ITEM: IDxBaseItem>(private val itemTouchCallback: DxItemTouchCallback<ITEM>) :
    ItemTouchHelper(itemTouchCallback) {

    /**
     * enabled dragging items with a handle
     * @param handleId the resource id of the handle used to initiate the drag
     */
    fun setDragHandleId(@IdRes handleId: Int) =
        itemTouchCallback.setUpDragWithHandle(handleId, this)
}