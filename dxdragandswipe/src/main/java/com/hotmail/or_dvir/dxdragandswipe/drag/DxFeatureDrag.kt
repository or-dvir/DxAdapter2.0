package com.hotmail.or_dvir.dxdragandswipe.drag

import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxdragandswipe.OnDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.OnItemMovedListener
import com.hotmail.or_dvir.dxdragandswipe.R
import org.jetbrains.annotations.TestOnly

/**
 * a feature that adds dragging functionality to your adapter.
 *
 * @param dragDirections the direction of allowed dragging. one or more of:
 * [ItemTouchHelper.LEFT], [ItemTouchHelper.RIGHT], [ItemTouchHelper.START], [ItemTouchHelper.END],
 * [ItemTouchHelper.updateDxDy], [ItemTouchHelper.DOWN].
 * @param onDragStart a listener to be invoked when a drag operation has started. you can use this
 * to highlight the item being dragged.
 * @param onDragEnd a listener to be invoked when the user interaction of the drag operation has
 * finished. you can use this to undo changes you made in [onDragStart].
 *
 * note that this listener is for the end of the user interaction with the dragged item, and does not
 * necessarily mean that the item has been moved.
 * @param onItemMoved a listener to be invoked when an item is about to be switched with another item.
 * note that the parameters are BEFORE the switching.
 * @param dragOnLongClick (optional) whether or not long-clicking an item should start a drag operation
 *
 * @see isDragEnabled
 */
open class DxFeatureDrag<ITEM : IDxBaseItem>(
    internal var dragDirections: Int,
    private val onDragStart: OnDragEventListener<ITEM>,
    private val onDragEnd: OnDragEventListener<ITEM>,
    internal val onItemMoved: OnItemMovedListener<ITEM>,
    internal var dragOnLongClick: Boolean = false
) : IDxBaseFeature {

    @IdRes
    internal var dragHandleId: Int? = null
    internal var itemTouchHelper: ItemTouchHelper? = null

    /**
     * a flag indicating whether dragging is currently allowed (defaults to TRUE)
     */
    var isDragEnabled = true

    private var flagIsDragging = false

    @TestOnly
    fun setDragDirection(directions: Int) {
        dragDirections = directions
    }

    @TestOnly
    fun setDragOnLongClick(dragOnLongClick: Boolean) {
        this.dragOnLongClick = dragOnLongClick
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //NOTE:
        //at this point holder.adapterPosition is -1 so we cannot check if the item is IDxItemDraggable
        if (!isDragEnabled || dragHandleId == null) {
            return
        }

        //todo fix this warning
        itemView.findViewById<View>(dragHandleId!!).setOnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    notifyDragStart(adapter, holder)
                    //notifyDragStart is also called from DxItemTouchCallback but
                    //we only want to manually start the drag operation from here
                    itemTouchHelper?.startDrag(holder)
                }
                MotionEvent.ACTION_UP -> notifyDragEnd(adapter, holder)
            }

            //allow normal processing to continue
            false
        }
    }

    override fun onBindViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_drag

    internal fun notifyDragStart(adapter: DxAdapter<*, *>, holder: RecyclerView.ViewHolder) {
        flagIsDragging = true
        holder.apply {
            //NOTE:
            //if the item is not draggable, this function will not be called
            onDragStart.invoke(itemView, adapterPosition, adapter.getItem(adapterPosition) as ITEM)
        }
    }

    internal fun notifyDragEnd(adapter: DxAdapter<*, *>, holder: RecyclerView.ViewHolder) {
        if (flagIsDragging) {
            flagIsDragging = false
            holder.apply {
                //NOTE:
                //if the item is not draggable, this function will not be called
                onDragEnd.invoke(itemView, adapterPosition, adapter.getItem(adapterPosition) as ITEM)
            }
        }
    }
}