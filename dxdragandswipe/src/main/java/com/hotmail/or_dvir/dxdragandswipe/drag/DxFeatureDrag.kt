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

//NOTE:
//due to the fact that both swipe and drag are handled inside DxItemTouchCallback,
//the generic type CANNOT be IDxItemDraggable (because then we wouldn't be able to handle
//IDxItemSwipeable items in DxItemTouchCallback)
class DxFeatureDrag<ITEM : IDxBaseItem>(
    internal var dragDirections: Int,
    private val onDragStart: OnDragEventListener<ITEM>,
    private val onDragEnd: OnDragEventListener<ITEM>,
    internal val onItemMoved: OnItemMovedListener<ITEM>,
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
        adapter: DxAdapter<*, *>,
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

    override fun getFeatureId() = R.id.feature_drag

    internal fun notifyDragStart(adapter: DxAdapter<*, *>, holder: RecyclerView.ViewHolder) {
        flagIsDragging = true
        holder.apply {
            onDragStart.invoke(itemView, adapterPosition, adapter.getItem(adapterPosition) as ITEM)
        }
    }

    internal fun notifyDragEnd(adapter: DxAdapter<*, *>, holder: RecyclerView.ViewHolder) {
        if (flagIsDragging) {
            flagIsDragging = false
            holder.apply {
                onDragEnd.invoke(itemView, adapterPosition, adapter.getItem(adapterPosition) as ITEM)
            }
        }
    }
}