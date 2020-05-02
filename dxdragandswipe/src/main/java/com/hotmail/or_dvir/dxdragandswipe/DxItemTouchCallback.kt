package com.hotmail.or_dvir.dxdragandswipe

import android.graphics.Canvas
import android.util.Log
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

class DxItemTouchCallback(private val mAdapter: DxAdapter<*>) : ItemTouchHelper.Callback() {

    companion object {
        private const val TAG = "DxItemTouchCallback"
    }

    //todo when documenting note that there is no need to add the feature to the adapter
    var dragFeature: DxFeatureDrag? = null
        set(value) {
            val prevField = field
            field = value

            if (value != null) {
                mAdapter.addFeature(value)
            } else if (prevField != null) {
                mAdapter.removeFeature(prevField)
            }
        }

    //todo when documenting note that there is no need to add the feature to the adapter
    var swipeFeature: DxFeatureSwipe? = null
        set(value) {
            val prevField = field
            field = value

            if (value != null) {
                mAdapter.addFeature(value)
            } else if (prevField != null) {
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
            //todo if onChildDraw() works with the listernes, this line is not needed
//            ItemTouchHelper.ACTION_STATE_SWIPE -> swipeFeature?.signalSwipeStart(itemView, holder)
        }
    }

    override fun clearView(recyclerView: RecyclerView, holder: ViewHolder) {
        super.clearView(recyclerView, holder)

        val itemView = holder.itemView

        //NOTE:
        //there is no bug here - in case of drag the listener for swipe will not be called
        //(or vice-versa). there are flags inside each feature to prevent this

        dragFeature?.apply {
            //todo let the drag feature handle whether or not
            // to activate the listener
            if (flagIsDragging) {
                signalDragEnd(itemView, holder)
            }
        }

        swipeFeature?.notifySwipeEnd(holder)
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
                swipeFeature!!.swipeDirections.itemTouchHelperInt
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
            val dxDirection =
                if (direction == ItemTouchHelper.LEFT) {
                    DxDirection.LEFT
                } else {
                    DxDirection.RIGHT
                }

            swipeFeature?.onItemSwiped?.invoke(itemView, adapterPosition, dxDirection)
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

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        holder: ViewHolder,
        dx: Float,
        dy: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, holder, dx, dy, actionState, isCurrentlyActive)
        //dx will be 0 when not swiping, or swiping but item is exactly in the middle.
        //adapter position will be -1 if the item is being removed from the adapter.
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE ||
            dx == 0f ||
            holder.adapterPosition == -1
        ) {
            return
        }

        if (dx < 0) {
            swipeFeature?.notifySwipeLeft(holder)
        } else {
            swipeFeature?.notifySwipeRight(holder)
        }

        //todo when documenting note that this only supports left and right swipes
        //todo add support for up/down swipe
    }
}