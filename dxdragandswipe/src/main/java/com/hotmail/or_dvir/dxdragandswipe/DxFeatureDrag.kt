package com.hotmail.or_dvir.dxdragandswipe

import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature

class DxFeatureDrag(
    val onDragStart: onItemDragSwipeInteractionListener,
    val onDragEnd: onItemDragSwipeInteractionListener,
    val onItemMoved: onItemMovedListener,
    val dragOnLongClick: Boolean = false,
    @IdRes val dragHandleId: Int? = null
) : IDxBaseFeature {

    //todo dragging
    // set drag handle
    // write tests

    var isDragEnabled = true
    internal var flagIsDragging = false

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        val item = adapter.getDxAdapterItems()[holder.adapterPosition]
        if (item !is IDxItemDraggable || !isDragEnabled || dragHandleId == null) {
            return
        }

        not actually enough. need to start drag event using ItemTouchHelper????
        check DxAdapter
                
        itemView.findViewById<View>(dragHandleId).setOnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> signalDragStart(itemView, holder.adapterPosition)
                MotionEvent.ACTION_UP -> signalDragEnd(itemView, holder.adapterPosition)
            }

            //allow normal processing to continue
            false
        }
    }

    internal fun signalDragStart(itemView: View, position: Int) {
        flagIsDragging = true
        onDragStart.invoke(itemView, position)
    }

    internal fun signalDragEnd(itemView: View, position: Int) {
        flagIsDragging = false
        onDragEnd.invoke(itemView, position)
    }
}