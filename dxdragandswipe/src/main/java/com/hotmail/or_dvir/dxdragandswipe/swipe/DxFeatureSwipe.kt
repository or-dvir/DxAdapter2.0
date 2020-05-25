package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxdragandswipe.OnItemSwipedListener
import com.hotmail.or_dvir.dxdragandswipe.OnSwipeEventListener
import com.hotmail.or_dvir.dxdragandswipe.R
import org.jetbrains.annotations.TestOnly

//NOTE:
//due to the fact that both swipe and drag are handled inside DxItemTouchCallback,
//the generic type CANNOT be IDxItemSwipeable (because then we wouldn't be able to handle
//IDxItemDraggable items in DxItemTouchCallback)
open class DxFeatureSwipe<ITEM : IDxBaseItem>(
    internal var swipeDirections: Int,
    private val onSwipeStart: OnSwipeEventListener<ITEM>,
    private val onSwipeEnd: OnSwipeEventListener<ITEM>,
    internal var onItemSwiped: OnItemSwipedListener<ITEM>
) : IDxBaseFeature {

    //region
    //optional user fields
    /**
     * see [ItemTouchHelper.Callback.getSwipeThreshold] for details
     */
    open var swipeThreshold: Float? = null

    /**
     * see [ItemTouchHelper.Callback.getSwipeEscapeVelocity] for more details.
     *
     * this value is overridden by [swipeEscapeVelocityMultiplier] (if set).
     */
    open var swipeEscapeVelocity: Float? = null

    /**
     * sets a value for the swipe escape velocity as a multiplier
     * of the device's default value.
     *
     * this value overrides [swipeEscapeVelocity].
     *
     *  see [ItemTouchHelper.Callback.getSwipeEscapeVelocity] for more details.
     */
    open var swipeEscapeVelocityMultiplier: Float? = null

    open fun getSwipeBackgroundLeft(itemView: View, adapterPosition: Int, item: ITEM)
            : DxSwipeBackground? = null

    open fun getSwipeBackgroundRight(itemView: View, adapterPosition: Int, item: ITEM)
            : DxSwipeBackground? = null

    var isSwipeEnabled = true
    //endregion

    private var flagIsSwiping = false

    @TestOnly
    fun setSwipeDirections(directions: Int) {
        swipeDirections = directions
    }

    @TestOnly
    fun setOnItemSwipedListener(listener: OnItemSwipedListener<ITEM>) {
        onItemSwiped = listener
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_swipe

    internal fun notifySwipeStart(adapter: DxAdapter<ITEM, *>, holder: RecyclerView.ViewHolder) {
        flagIsSwiping = true

        holder.apply {
            //NOTE:
            //if the item is not swipeable, this function will not be called
            onSwipeStart.invoke(
                itemView,
                adapterPosition,
                adapter.getItem(adapterPosition)
            )
        }
    }

    internal fun notifySwipeEnd(adapter: DxAdapter<ITEM, *>, holder: RecyclerView.ViewHolder) {
        if (flagIsSwiping) {
            flagIsSwiping = false

            //todo add this note to documentation:
            // if item is removed, onSwipeEnd will NOT be called
            //this can happen if the item is removed from the adapter after the swipe.
            //due to the way ItemTouchCallback works, onSwiped is called BEFORE this method is invoked

            holder.apply {
                if (adapterPosition != -1) {
                    //NOTE:
                    //if the item is not swipeable, this function will not be called
                    onSwipeEnd.invoke(
                        itemView,
                        adapterPosition,
                        adapter.getItem(adapterPosition)
                    )
                }
            }
        }
    }
}