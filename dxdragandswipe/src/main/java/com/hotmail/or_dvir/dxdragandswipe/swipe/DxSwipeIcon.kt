package com.hotmail.or_dvir.dxdragandswipe.swipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * convenience class to be used with [DxSwipeBackground].
 *
 * note that the provided icon will be scaled according to [desiredHeightPx] while
 * maintaining aspect ratio. it is therefore recommended that [iconRes] will be large
 * rather than small because scaling down is preferable to scaling up
 * (scaling up can reduce image quality).
 *
 * @param iconRes the resources id of the icon to be drawn when swiping an item
 * @param desiredHeightPx the desired height of the icon, in pixels (recommended to use
 * DP from dimens.xml)
 */
class DxSwipeIcon(
    @DrawableRes val iconRes: Int,
    private val context: Context,
    private val desiredHeightPx: Int
) {
    internal val mIconDrawable: Drawable

    init {
        context.apply {
            val bitmap = getBitmap(iconRes)
            val ratio = bitmap.width.toFloat() / bitmap.height
            val scaledWidth = (desiredHeightPx * ratio).toInt()

            val bitmapResized = Bitmap.createScaledBitmap(
                bitmap,
                scaledWidth,
                desiredHeightPx,
                true
            )

            mIconDrawable = BitmapDrawable(resources, bitmapResized)
        }
    }

    private fun getBitmap(drawableId: Int): Bitmap {
        ContextCompat.getDrawable(context, drawableId)?.apply {
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)

            return bitmap
        }

        throw NullPointerException("could not create bitmap (bitmap was null)")
    }
}