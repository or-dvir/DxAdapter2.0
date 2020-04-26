package com.hotmail.or_dvir.dxdragandswipe

import android.util.Log
import androidx.annotation.IdRes
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

    var dragFeature: DxFeatureDrag? = null
        set(value) {
            field = value
            value?.apply {
                mAdapter.addFeature(value)
            }
        }

    //region swipe
//    var onInteractionStartSwipe: onItemDragSwipeInteractionListener? = null
//    var onInteractionEndSwipe: onItemDragSwipeInteractionListener? = null
//    var onItemSwiped: onItemSwipedListener? = null
//    private var flagIsSwiped = false
    //endregion
    //todo swiping
    // copy the way drag is implemented
    // add a single function to enable all swiping options
    // only swipe items that are swipeable
    // add global flag to enable/disable swipe
    // write tests

    internal fun setUpDragWithHandle(@IdRes handleId: Int, touchHelper: ItemTouchHelper) {
        dragFeature?.apply {
            dragHandleId = handleId
            itemTouchHelper = touchHelper
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return dragFeature?.let {
            it.dragOnLongClick && it.isDragEnabled
        } ?: false
    }

    override fun onSelectedChanged(holder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(holder, actionState)

        if (holder == null) {
            Log.e(TAG, "viewHolder is null. cannot invoke drag and swipe listeners")
            return
        }

        val itemView = holder.itemView

        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                dragFeature?.signalDragStart(itemView, holder)
            }

//            ItemTouchHelper.ACTION_STATE_SWIPE -> {
//                flagIsSwiped = true
//                onInteractionStartSwipe?.invoke(itemView, holder)
//            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, holder: ViewHolder) {
        super.clearView(recyclerView, holder)

        val itemView = holder.itemView

        dragFeature?.apply {
            if (flagIsDragging) {
                signalDragEnd(itemView, holder)
            }
        }

//        if (flagIsSwiped) {
//            flagIsSwiped = false
//            onInteractionEndSwipe?.invoke(itemView, holder)
//        }
    }

    override fun getMovementFlags(recycler: RecyclerView, holder: ViewHolder): Int {
        val item = mAdapter.getDxAdapterItems()[holder.adapterPosition]

        val isDragEnabled = dragFeature?.isDragEnabled ?: false
        val dragFlags =
            if (item !is IDxItemDraggable || !isDragEnabled || dragFeature == null) {
                0
            } else {
                dragFeature!!.dragDirections
            }

//        val swipeFlags =
//            if (item !is IDxItemSwipeable || onItemSwiped == null) {
//                0
//            } else {
//                //for sure onItemSwiped is not null because of the "if" above
//                onItemSwiped!!.first
//            }

        //todo add swipe flags when done testing drag!!!!!!!!!!
        return makeMovementFlags(dragFlags, 0)
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

            //NOTE:
            //should call this BEFORE actually moving the items. otherwise draggedPosition
            //and targetPosition would ve reversed (because they have been switched...)
            //todo add this note to documentation
            dragFeature?.onItemMoved?.invoke(
                draggedView,
                draggedPosition,
                targetView,
                targetPosition
            )

            getDxAdapterItems().apply {
                val itemBackup = removeAt(draggedPosition)
                add(targetPosition, itemBackup)
            }

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