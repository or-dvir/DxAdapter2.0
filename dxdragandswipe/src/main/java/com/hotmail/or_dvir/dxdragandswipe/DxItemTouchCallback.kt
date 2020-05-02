package com.hotmail.or_dvir.dxdragandswipe

import android.graphics.Canvas
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.drag.IDxItemDraggable
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxFeatureSwipe
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeBackground
import com.hotmail.or_dvir.dxdragandswipe.swipe.IDxItemSwipeable

class DxItemTouchCallback(private val mAdapter: DxAdapter<*>) : ItemTouchHelper.Callback() {

    //region
    //fields for onChildDraw() for better performance (onChildDraw() will be called many times)
    private var mSwipeBackgroundForDrawing: DxSwipeBackground? = null

    //endregion

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
    // even though nothing will happen as the map in DxAdapter will override it
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

        if (holder != null) {
            when (actionState) {
                ItemTouchHelper.ACTION_STATE_DRAG -> dragFeature?.notifyDragStart(holder)
                ItemTouchHelper.ACTION_STATE_SWIPE -> swipeFeature?.notifySwipeStart(holder)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, holder: ViewHolder) {
        super.clearView(recyclerView, holder)

        //NOTE:
        //there is no bug here - in case of drag the listener for swipe will not be called
        //(and vice-versa). there are flags inside each feature to prevent this
        dragFeature?.notifyDragEnd(holder)
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

    override fun onSwiped(holder: ViewHolder, direction: Int) {
        swipeFeature?.onItemSwiped?.invoke(holder.itemView, holder.adapterPosition, direction)
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
        //dx will be 0 when not swiping, or swiping but item is exactly in the middle.
        //adapter position will be -1 if the item is being removed from the adapter.
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE ||
            dx == 0f ||
            holder.adapterPosition == -1
        ) {
            return
        }

        //todo do something

        super.onChildDraw(c, recyclerView, holder, dx, dy, actionState, isCurrentlyActive)
    }
}