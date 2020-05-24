package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * convenience class to be used with [DxSwipeBackground].
 *
 * note for [desiredSizePx]: it is recommended to use DP (and not SP) from dimens.xml
 * so it will look similar on different screens.
 * use DP and not SP because it is assumed that your list items are sized
 * using DP and therefore the text size should be calculated by the same scale.
 *
 * if you use SP for text, then the text size setting on the users' device
 * could ruin the proportions of your list items and the text size.
 *
 * @param text the text to show when swiping an item
 * @param desiredSizePx the desired size of the text, in pixels (see note in class description)
 * @param textColor the color of the text
 */
class DxSwipeText(
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