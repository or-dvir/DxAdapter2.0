package com.hotmail.or_dvir.dxdragandswipe

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hotmail.or_dvir.dxadapter.DxAdapter

class DxItemTouchCallback(private val mAdapter: DxAdapter<*>) : ItemTouchHelper.Callback() {

    companion object {
        private const val TAG = "DxItemTouchCallback"
    }

    //todo add global flag isDragEnabled and isSwipeEnabled
    // there might be situations where the user wants to temporarily disable drag (like selection mode)

    //todo only swipe items that are swipeable

    //todo add all features from dx adapter

    enable swiping and dragging independently
    only drag items which are draggable

    var dragOnLongClick = false
    var onInteractionStartDrag: onItemDragSwipeInteractionListener? = null
    var onInterActionEndDrag: onItemDragSwipeInteractionListener? = null
    var onItemMoved: onItemMovedListener? = null

    var onInteractionStartSwipe: onItemDragSwipeInteractionListener? = null
    var onInteractionEndSwipe: onItemDragSwipeInteractionListener? = null
    var onItemSwiped: onItemSwipedListener? = null

    private var flagIsDragged = false
    private var flagIsSwiped = false

    override fun isLongPressDragEnabled() = dragOnLongClick

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (viewHolder == null) {
            Log.e(TAG, "viewHolder is null. cannot call drag and swipe listeners")
            return
        }

        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition

        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                flagIsDragged = true
                onInteractionStartDrag?.invoke(itemView, position)
            }

            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                flagIsSwiped = true
                onInteractionStartSwipe?.invoke(itemView, position)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition

        if (flagIsDragged) {
            flagIsDragged = false
            onInterActionEndDrag?.invoke(itemView, position)
        }

        if (flagIsSwiped) {
            flagIsSwiped = false
            onInteractionEndSwipe?.invoke(itemView, position)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        TODO("not implemented")
    }

    override fun onMove(
        recyclerView: RecyclerView,
        dragged: ViewHolder,
        target: ViewHolder
    ): Boolean {
        val draggedView = dragged.itemView
        val draggedPosition = dragged.adapterPosition

        val targetView = target.itemView
        val targetPosition = target.adapterPosition

        mAdapter.apply {
            getDxAdapterItems().apply {
                removeAt(draggedPosition)
                add(targetPosition, getDxAdapterItem(draggedPosition))
            }

            //todo when documenting, note that this is called AFTER the item has been moved
            onItemMoved?.invoke(draggedView, draggedPosition, targetView, targetPosition)

            notifyItemMoved(draggedPosition, targetPosition)
        }

        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition
        onItemSwiped?.invoke(itemView, position, direction)
    }
}