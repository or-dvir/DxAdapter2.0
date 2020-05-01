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

    //todo when documenting note that there is no need to add the feature to the adapter
    var dragFeature: DxFeatureDrag? = null
        set(value) {
            val prevField = field
            field = value

            if(value != null) {
                mAdapter.addFeature(value)
            } else if(prevField != null) {
                mAdapter.removeFeature(prevField)
            }
        }

    //todo when documenting note that there is no need to add the feature to the adapter
    var swipeFeature: DxFeatureSwipe? = null
        set(value) {
            val prevField = field
            field = value

            if(value != null) {
                mAdapter.addFeature(value)
            } else if(prevField != null) {
                mAdapter.removeFeature(prevField)
            }
        }

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
            ItemTouchHelper.ACTION_STATE_DRAG -> dragFeature?.signalDragStart(itemView, holder)
            ItemTouchHelper.ACTION_STATE_SWIPE -> swipeFeature?.signalSwipeStart(itemView, holder)
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

        swipeFeature?.apply {
            if (flagIsSwiping) {
                signalSwipeEnd(itemView, holder)
            }
        }
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

        val isSwipeEnabled = swipeFeature?.isSwipeEnabled ?: false
        val swipeFlags =
            if (item !is IDxItemSwipeable || !isSwipeEnabled || swipeFeature == null) {
                0
            } else {
                swipeFeature!!.swipeDirections
            }

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
        viewHolder.apply {
            swipeFeature?.onItemSwiped?.invoke(itemView, adapterPosition, direction)
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return swipeFeature?.let { feature ->
            feature.swipeEscapeVelocityMultiplier?.let {
                it * defaultValue
            } ?: feature.swipeEscapeVelocity
        } ?: super.getSwipeEscapeVelocity(defaultValue)
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder) =
        swipeFeature?.swipeThreshold ?: super.getSwipeThreshold(viewHolder)
}