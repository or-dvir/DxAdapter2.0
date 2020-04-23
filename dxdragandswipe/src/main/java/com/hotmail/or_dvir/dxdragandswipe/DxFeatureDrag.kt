package com.hotmail.or_dvir.dxdragandswipe

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature

class DxFeatureDrag(
    var dragOnLongClick: Boolean = false,
    var onInteractionStartDrag: onItemDragSwipeInteractionListener,
    var onInterActionEndDrag: onItemDragSwipeInteractionListener,
    var onItemMoved: onItemMovedListener
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
        copy behaviour from dx adapter library
    }
}