package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt

class DxSwipeBackground(
    internal var paddingPx: Int,
    @ColorInt internal val backgroundColor: Int?,
    internal val dxText: DxSwipeText?,
    internal val dxIcon: DxSwipeIcon?
) {
    internal val iconWidthPx = dxIcon?.mIconDrawable?.intrinsicWidth ?: 0
    internal val textWidthPx = dxText?.let { it.mPaint.measureText(it.text.trim()).toInt() } ?: 0
    internal val mHalfIconHeight = (dxIcon?.mIconDrawable?.intrinsicHeight ?: 0) / 2
    internal var backgroundColorDrawable =
        backgroundColor?.let { ColorDrawable(it) } ?: ColorDrawable()

    private var totalWidthToFitPx = textWidthPx + iconWidthPx

    init {
        //if at least one of item/text, add padding on both sides
        if (dxText != null || dxIcon != null)
            totalWidthToFitPx += (2 * paddingPx)
        //if none of text/icon, set paddingPx to 0 so calculations
        //using this variable will not be affected
        else
            paddingPx = 0

        //if both icon and text, add padding between them
        if (dxText != null && dxIcon != null)
            totalWidthToFitPx += paddingPx
    }

    internal fun doesBackgroundFitInSwipeArea() =
        backgroundColorDrawable.bounds.width() >= totalWidthToFitPx
}