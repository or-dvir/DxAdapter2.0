package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * convenience class to be used with [DxSwipeBackground].
 *
 * @param text the text to show behind the item being swiped.
 * @param desiredSizePx the desired size of the text (in pixels).
 *
 * it is recommended to use DP (and not SP) from dimens.xml, because it is assumed that your
 * list items are sized using DP and therefore the text size should be calculated by the same scale.
 * @param textColor the color of the text
 */
open class DxSwipeText(
    internal val text: String,
    private val desiredSizePx: Float,
    @ColorInt internal val textColor: Int
) {
    internal val mPaint = Paint().apply {
        //todo check if this alignment works for RTL languages
        textAlign = Paint.Align.LEFT
        textSize = desiredSizePx
        color = textColor
    }
}