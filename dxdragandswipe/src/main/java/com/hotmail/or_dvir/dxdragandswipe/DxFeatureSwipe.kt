package com.hotmail.or_dvir.dxdragandswipe

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import org.jetbrains.annotations.TestOnly

class DxFeatureSwipe(
    private val onSwipeStart: onSwipeEventListener,
    private val onSwipeEnd: onSwipeEventListener,
    internal val onItemSwiped: onItemSwipedListener,
    internal var swipeDirections: Int
) : IDxBaseFeature {

    //todo
    // background

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
    internal var flagIsSwiping = false

    @TestOnly
    fun setSwipeDirection(directions: Int) {
        swipeDirections = directions
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //NOTE:
        //at this point holder.adapterPosition is -1 so we cannot check if the item is IDxItemSwipeable
        if (!isSwipeEnabled) {
            return
        }

        //todo what do i need to do here?
//        itemView.findViewById<View>(SwipeHandleId!!).setOnTouchListener { view, motionEvent ->
//            when (motionEvent.actionMasked) {
//                MotionEvent.ACTION_DOWN -> {
//                    signalSwipeStart(itemView, holder)
//                    //signalSwipeStart is also called from DxItemTouchCallback but
//                    //we only want to manually start the Swipe operation from here
//                    itemTouchHelper?.startSwipe(holder)
//                }
//                MotionEvent.ACTION_UP -> signalSwipeEnd(itemView, holder)
//            }
//
//            //allow normal processing to continue
//            false
//        }
    }

    internal fun signalSwipeStart(itemView: View, holder: RecyclerView.ViewHolder) {
        flagIsSwiping = true
        onSwipeStart.invoke(itemView, holder.adapterPosition)
    }

    internal fun signalSwipeEnd(itemView: View, holder: RecyclerView.ViewHolder) {
        flagIsSwiping = false
        onSwipeEnd.invoke(itemView, holder.adapterPosition)
    }
}