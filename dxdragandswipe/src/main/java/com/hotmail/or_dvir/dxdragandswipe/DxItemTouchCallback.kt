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

    //todo add all features from dx adapter
    // make sure all methods are implemented the same

    //todo write tests
    var dragFeature: DxFeatureDrag? = null
        set(value) {
            field = value
            value?.apply {
                mAdapter.addFeature(value)
            }
        }


    copy behaviour of drag
    //region swipe
//    var onInteractionStartSwipe: onItemDragSwipeInteractionListener? = null
//    var onInteractionEndSwipe: onItemDragSwipeInteractionListener? = null
//    var onItemSwiped: onItemSwipedListener? = null
//    private var flagIsSwiped = false
    //endregion
    //todo swiping
    // add a single function to enable all swiping options
    // only swipe items that are swipeable
    // add global flag to enable/disable swipe
    // write tests

    override fun isLongPressDragEnabled(): Boolean {
        return dragFeature?.let {
            it.dragOnLongClick && it.isDragEnabled
        } ?: false
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (viewHolder == null) {
            Log.e(TAG, "viewHolder is null. cannot invoke drag and swipe listeners")
            return
        }

        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition

        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                dragFeature?.apply {
                    flagIsDragging = true
                    onInteractionStartDrag.invoke(itemView, position)
                }
            }

//            ItemTouchHelper.ACTION_STATE_SWIPE -> {
//                flagIsSwiped = true
//                onInteractionStartSwipe?.invoke(itemView, position)
//            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition

        dragFeature?.apply {
            if (flagIsDragging) {
                flagIsDragging = false
                onInterActionEndDrag.invoke(itemView, position)
            }
        }

//        if (flagIsSwiped) {
//            flagIsSwiped = false
//            onInteractionEndSwipe?.invoke(itemView, position)
//        }
    }

    override fun getMovementFlags(recycler: RecyclerView, holder: ViewHolder): Int {
        val item = mAdapter.getDxAdapterItems()[holder.adapterPosition]

        val isDragEnabled = dragFeature?.isDragEnabled ?: false
        val dragFlags =
            if (item !is IDxItemDraggable || !isDragEnabled) {
                0
            } else {
                //todo add support for left and right drag
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }

//        val swipeFlags =
//            if (item !is IDxItemSwipeable || onItemSwiped == null) {
//                0
//            } else {
//                //for sure onItemSwiped is not null because of the "if" above
//                onItemSwiped!!.first
//            }

        return makeMovementFlags(dragFlags, swipeFlags)
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
                //must reference the item before removing it
                val itemBackup = get(draggedPosition)
                removeAt(draggedPosition)
                add(targetPosition, itemBackup)
            }

            //todo when documenting, note that this is called AFTER the item has been moved
            dragFeature?.onItemMoved?.invoke(
                draggedView,
                draggedPosition,
                targetView,
                targetPosition
            )
            notifyItemMoved(draggedPosition, targetPosition)
        }

        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        val itemView = viewHolder.itemView
        val position = viewHolder.adapterPosition
//        onItemSwiped?.invoke(itemView, position, direction)
    }
}