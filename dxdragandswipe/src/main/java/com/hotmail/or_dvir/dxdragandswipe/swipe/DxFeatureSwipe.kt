package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxdragandswipe.R
import com.hotmail.or_dvir.dxdragandswipe.onItemSwipedListener
import com.hotmail.or_dvir.dxdragandswipe.onSwipeEventListener
import org.jetbrains.annotations.TestOnly

open class DxFeatureSwipe(
    internal var swipeDirections: Int,
    private val onSwipeStart: onSwipeEventListener,
    private val onSwipeEnd: onSwipeEventListener,
    internal val onItemSwiped: onItemSwipedListener
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

    open fun getSwipeBackgroundLeft(itemView: View, adapterPosition: Int): DxSwipeBackground? = null
    open fun getSwipeBackgroundRight(itemView: View, adapterPosition: Int): DxSwipeBackground? =
        null

    var isSwipeEnabled = true
    //endregion

    private var flagIsSwiping = false

    @TestOnly
    fun setSwipeDirections(directions: Int) {
        swipeDirections = directions
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() =
        R.id.feature_swipe

    internal fun notifySwipeStart(holder: RecyclerView.ViewHolder) {
        flagIsSwiping = true
        onSwipeStart.invoke(holder.itemView, holder.adapterPosition)
    }

    internal fun notifySwipeEnd(holder: RecyclerView.ViewHolder) {
        if (flagIsSwiping) {
            flagIsSwiping = false
            onSwipeEnd.invoke(holder.itemView, holder.adapterPosition)
        }
    }
}