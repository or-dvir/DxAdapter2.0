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

/**
 * a feature that adds swiping functionality to your adapter.
 *
 * @param swipeDirections the direction of allowed swiping. one or more of:
 * [ItemTouchHelper.LEFT], [ItemTouchHelper.RIGHT], [ItemTouchHelper.START], [ItemTouchHelper.END],
 * [ItemTouchHelper.updateDxDy], [ItemTouchHelper.DOWN].
 * @param onSwipeStart a listener to be invoked when a swipe operation has started.
 * @param onSwipeEnd a listener to be invoked when the user interaction of the swipe operation
 * has finished.
 *
 * note that this listener is for the end of the user interaction with the swiped item, and does not
 * necessarily mean that the item has been fully swiped.
 *
 * Also note that if the swiped item is being removed from the adapter, this listener will NOT trigger.
 *
 * @param onItemSwiped a listener to be invoked when an item has been fully swiped.
 *
 * @see isSwipeEnabled
 * @see swipeThreshold
 * @see swipeEscapeVelocity
 * @see swipeEscapeVelocityMultiplier
 */
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
     * a flag indicating whether swiping is currently allowed (defaults to TRUE)
     */
    var isSwipeEnabled = true

    /**
     * sets a value for the swipe escape velocity as a multiplier
     * of the device's default value.
     *
     * this value overrides [swipeEscapeVelocity].
     *
     *  see [ItemTouchHelper.Callback.getSwipeEscapeVelocity] for more details.
     */
    open var swipeEscapeVelocityMultiplier: Float? = null

    /**
     * a helper function to get the background to be shown behind an item that is being swiped
     * to the left
     *
     * @param itemView the root view of the [RecyclerView.ViewHolder] being swiped.
     * @param adapterPosition the position of the item being swiped
     * @param item the item being swiped (you can use this to get a different background
     * depending on the item state)
     */
    open fun getSwipeBackgroundLeft(itemView: View, adapterPosition: Int, item: ITEM)
            : DxSwipeBackground? = null

    /**
     * a helper function to get the background to be shown behind an item that is being swiped
     * to the right
     *
     * @param itemView the root view of the [RecyclerView.ViewHolder] being swiped.
     * @param adapterPosition the position of the item being swiped
     * @param item the item being swiped (you can use this to get a different background
     * depending on the item state)
     */
    open fun getSwipeBackgroundRight(itemView: View, adapterPosition: Int, item: ITEM)
            : DxSwipeBackground? = null
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

    override fun onBindViewHolder(
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

            holder.apply {
                //adapterPosition may be -1 if the item is removed from the adapter after the swipe.
                //due to the way ItemTouchCallback works, onSwiped is called BEFORE this method is invoked
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