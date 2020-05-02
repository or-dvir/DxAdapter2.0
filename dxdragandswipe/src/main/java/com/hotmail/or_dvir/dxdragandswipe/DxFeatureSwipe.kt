package com.hotmail.or_dvir.dxdragandswipe

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import org.jetbrains.annotations.TestOnly

class DxFeatureSwipe(
    internal var swipeDirections: DxDirection,
    private val onStartDirectionalSwipe: onSwipeStartListener,
    private val onEndSwipeInteraction: onSwipeEndListener,
    internal val onItemSwiped: onItemSwipedListener
) : IDxBaseFeature {

    //todo change dragging to use DxDirection!!!!!

    //todo
    // background
    // post answer to this question:
    // https://stackoverflow.com/questions/57334276/how-do-i-add-two-different-backgrounds-for-swiping-left-and-right-respectively

    //todo how to do background!
    // for static background
    //      set desired layout in ui and do nothing in listeners
    // for dynamic background which depends on item state
    //      set as many backgrounds as needed and set them to invisible
    //      change visibility according to item state in proper listener

    /**
     * see [ItemTouchHelper.Callback.getSwipeThreshold] for details
     */
    var swipeThreshold: Float? = null

    /**
     * see [ItemTouchHelper.Callback.getSwipeEscapeVelocity] for more details.
     *
     * this value is overridden by [swipeEscapeVelocityMultiplier] (if set).
     */
    var swipeEscapeVelocity: Float? = null

    /**
     * sets a value for the swipe escape velocity as a multiplier
     * of the device's default value.
     *
     * this value overrides [swipeEscapeVelocity].
     *
     *  see [ItemTouchHelper.Callback.getSwipeEscapeVelocity] for more details.
     */
    var swipeEscapeVelocityMultiplier: Float? = null


    var isSwipeEnabled = true
    private var flagIsSwiping = false
    internal var flagNotifiedSwipingRight = false
    internal var flagNotifiedSwipingLeft = false

    @TestOnly
    fun setSwipeDirections(directions: DxDirection) {
        swipeDirections = directions
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_swipe

    //    internal fun signalSwipeStart(itemView: View, holder: RecyclerView.ViewHolder) {
//        flagIsSwiping = true
//        onSwipeStart.invoke(itemView, holder.adapterPosition)
//    }
    internal fun notifySwipeRight(holder: RecyclerView.ViewHolder) {
        //todo could probably be moved into the if.
        // is it even still needed?
        flagIsSwiping = true

        if (!flagNotifiedSwipingRight) {
            flagNotifiedSwipingLeft = false
            flagNotifiedSwipingRight = true
            onStartDirectionalSwipe.invoke(holder.itemView, holder.adapterPosition, DxDirection.RIGHT)
        }
    }

    internal fun notifySwipeLeft(holder: RecyclerView.ViewHolder) {
        //todo could probably be moved into the if.
        // is it even still needed?
        flagIsSwiping = true

        if (!flagNotifiedSwipingLeft) {
            flagNotifiedSwipingLeft = true
            flagNotifiedSwipingRight = false
            onStartDirectionalSwipe.invoke(holder.itemView, holder.adapterPosition, DxDirection.LEFT)
        }
    }

    private fun resetSwipeFlags() {
        flagIsSwiping = false
        flagNotifiedSwipingLeft = false
        flagNotifiedSwipingRight = false
    }

    internal fun notifySwipeEnd(holder: RecyclerView.ViewHolder) {
        if (flagIsSwiping) {
            resetSwipeFlags()
            onEndSwipeInteraction.invoke(holder.itemView, holder.adapterPosition)
        }
    }
}