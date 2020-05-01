package com.hotmail.or_dvir.dxdragandswipe

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import org.jetbrains.annotations.TestOnly

class DxFeatureSwipe(
    //todo change the name of this to include direction?
    private val onSwipeStart: onSwipeEventListener,
    private val onSwipeEnd: onSwipeEventListener,
    internal val onItemSwiped: onItemSwipedListener,
    internal var swipeDirections: Int
) : IDxBaseFeature {

    //todo
    // background
    // add direction to swipe listener
    // add listener for when swipe direction changes (only after passing the zero!!! otherwise ui will look weird)
    // check what happens if the user lifts his finger mid-swipe (should be fine because onSwipeEnd is supposed to be called when the animation ends)
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
    internal var flagIsSwipingRight = false
    internal var flagIsSwipingLeft = false

    @TestOnly
    fun setSwipeDirection(directions: Int) {
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
    internal fun signalSwipeStartRight(
        itemView: View,
        holder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        if(flagIsSwipingRight) {
            flagIsSwipingLeft = false
            flagIsSwipingRight = true
            onSwipeStart.invoke(itemView, holder.adapterPosition, direction)
        }

        flagIsSwipingLeft = false
        flagIsSwipingRight = true
        onSwipeStart.invoke(itemView, holder.adapterPosition, direction)
    }

    internal fun signalSwipeStartLeft(
        itemView: View, holder: RecyclerView.ViewHolder, direction: Int
    ) {
        flagIsSwipingLeft = true
        flagIsSwipingRight = false
        onSwipeStart.invoke(itemView, holder.adapterPosition, direction)
    }

    internal fun signalSwipeEnd(itemView: View, holder: RecyclerView.ViewHolder) {
        if(flagIsSwiping){
            i stopped here
            flagIsSwiping = false
            onSwipeEnd.invoke(itemView, holder.adapterPosition)
        }
    }
}