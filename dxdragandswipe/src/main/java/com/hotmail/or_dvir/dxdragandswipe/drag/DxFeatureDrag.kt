package com.hotmail.or_dvir.dxdragandswipe.drag

import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxdragandswipe.R
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import org.jetbrains.annotations.TestOnly

class DxFeatureDrag(
    internal var dragDirections: Int,
    private val onDragStart: onDragEventListener,
    private val onDragEnd: onDragEventListener,
    internal val onItemMoved: onItemMovedListener,
    internal var dragOnLongClick: Boolean = false
) : IDxBaseFeature {

    //todo when documenting note that in onDragEnd, the parameter adapterPosition is
    // the FINAL position of the item

    @IdRes
    internal var dragHandleId: Int? = null
    internal var itemTouchHelper: ItemTouchHelper? = null

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
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //NOTE:
        //at this point holder.adapterPosition is -1 so we cannot check if the item is IDxItemDraggable
        if (!isDragEnabled || dragHandleId == null) {
            return
        }

        itemView.findViewById<View>(dragHandleId!!).setOnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    notifyDragStart(holder)
                    //signalDragStart is also called from DxItemTouchCallback but
                    //we only want to manually start the drag operation from here
                    itemTouchHelper?.startDrag(holder)
                }
                MotionEvent.ACTION_UP -> notifyDragEnd(holder)
            }

            //allow normal processing to continue
            false
        }
    }

    override fun getFeatureId() =
        R.id.feature_drag

    internal fun notifyDragStart(holder: RecyclerView.ViewHolder) {
        flagIsDragging = true
        onDragStart.invoke(holder.itemView, holder.adapterPosition)
    }

    internal fun notifyDragEnd(holder: RecyclerView.ViewHolder) {
        if (flagIsDragging) {
            flagIsDragging = false
            onDragEnd.invoke(holder.itemView, holder.adapterPosition)
        }
    }
}