package com.hotmail.or_dvir.dxlibraries.swipeable

import android.content.Context
import android.graphics.Color
import android.view.View
import com.hotmail.or_dvir.dxdragandswipe.OnItemSwipedListener
import com.hotmail.or_dvir.dxdragandswipe.OnSwipeEventListener
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxFeatureSwipe
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeBackground
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeIcon
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeText
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.R

class MySwipeFeature(
    context: Context,
    swipeDirections: Int,
    onSwipeStart: OnSwipeEventListener,
    onSwipeEnd: OnSwipeEventListener,
    onItemSwiped: OnItemSwipedListener
) : DxFeatureSwipe<BaseItem>(swipeDirections, onSwipeStart, onSwipeEnd, onItemSwiped) {

    //region
    //possible values to override
//    override var swipeThreshold: Float? = 0.7f
//    override var swipeEscapeVelocity: Float? = 200f
//    override var swipeEscapeVelocityMultiplier: Float? = 1.5f
    //endregion

    //IMPORTANT: read documentation for DxSwipeIcon and DxSwipeText
    private val backgroundRightOptionA =
        DxSwipeBackground(
            30, Color.MAGENTA, DxSwipeText("right swipe A", 60f, Color.BLACK),
            DxSwipeIcon(R.drawable.ic_arrow_up, context, 96)
        )

    private val backgroundRightOptionB =
        DxSwipeBackground(
            30, Color.LTGRAY, DxSwipeText("right swipe B", 60f, Color.BLACK),
            DxSwipeIcon(R.drawable.ic_arrow_down, context, 96)
        )

    private val backgroundLeft =
        DxSwipeBackground(
            30, Color.RED, DxSwipeText("left swipe", 60f, Color.BLACK),
            DxSwipeIcon(R.drawable.ic_arrow_left, context, 96)
        )

    override fun getSwipeBackgroundLeft(itemView: View, adapterPosition: Int) = backgroundLeft

    override fun getSwipeBackgroundRight(itemView: View, adapterPosition: Int): DxSwipeBackground? {
        return if (adapterPosition % 2 == 0) {
            backgroundRightOptionA
        } else {
            backgroundRightOptionB
        }
    }
}