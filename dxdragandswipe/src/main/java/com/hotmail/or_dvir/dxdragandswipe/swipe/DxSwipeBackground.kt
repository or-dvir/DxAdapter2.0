package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt

/**
 * a helper class for drawing behind an item that is being swiped
 *
 * @param paddingPx amount of padding (in pixels) between the edge of the item and the text and/or icon.
 * note that if you have both text AND an icon, this is also the amount of space between them.
 * @param backgroundColor the background color of the area behind the item being swiped.
 * pass null to not show a color.
 * @param dxText the text to show behind the item being swiped (should be as short as possible).
 * pass null to not show text.
 * @param dxIcon the icon to show behind the item being swiped.
 * pass null to not show an icon.
 */
open class DxSwipeBackground(
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