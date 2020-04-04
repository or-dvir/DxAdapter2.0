package com.hotmail.or_dvir.dxrecyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener.ScrollDirection
import kotlin.math.abs

/**
 * a wrapper for RecyclerView with built-in listeners
 * @see [onItemsVisibilityListener]
 * @see onScrollListener
 */
class DxRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    /**
     * a visibility listener for items in this [DxRecyclerView].
     * the listener may be triggered when set, and when the recycler view is scrolled.
     *
     * * the listener is tied only to the [DxRecyclerView] and NOT to it's adapter.
     * this means that it will NOT be triggered when the adapter updates.
     * * only the most recently set listener will be active.
     * * its possible for the listener (more precisely it's inner listeners) to trigger immediately or
     * not at all. see [DxVisibilityListener] for details.
     *
     * @see [DxVisibilityListener]
     */
    var onItemsVisibilityListener: DxVisibilityListener? = null
        set(value) {
            field = value
            invokeVisibilityListeners()
        }

    /**
     * a listener that will be invoked when this [DxRecyclerView] is scrolled.
     *
     * @see DxScrollListener
     */
    var onScrollListener: DxScrollListener? = null

    //todo add support for other types of layout managers
    // note that grid layout manager extends linear layout manager.
    // staggered grid though is separate

    override fun onDetachedFromWindow() {
        clearOnScrollListeners()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addOnScrollListener(
            object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    onScrollListener?.apply {
                        when {
                            dx > 0 -> invokeScrollListener(dx, ScrollDirection.RIGHT)
                            dx < 0 -> invokeScrollListener(dx, ScrollDirection.LEFT)

                            dy > 0 -> invokeScrollListener(dy, ScrollDirection.DOWN)
                            dy < 0 -> invokeScrollListener(dy, ScrollDirection.UP)
                        }
                    }

                    invokeVisibilityListeners()
                }
            })
    }

    private fun invokeScrollListener(scrollValue: Int, direction: ScrollDirection) {
        val absVal = abs(scrollValue)

        onScrollListener?.apply {
            when (direction) {
                ScrollDirection.UP -> if (absVal > sensitivityUp) onScrollUp?.invoke()
                ScrollDirection.DOWN -> if (absVal > sensitivityDown) onScrollDown?.invoke()
                ScrollDirection.LEFT -> if (absVal > sensitivityLeft) onScrollLeft?.invoke()
                ScrollDirection.RIGHT -> if (absVal > sensitivityRight) onScrollRight?.invoke()
            }
        }
    }

    private fun invokeVisibilityListeners() {
        if (onItemsVisibilityListener == null ||
            !onItemsVisibilityListener!!.atLeastOneListenerSet()
        ) {
            return
        }

        layoutManager?.let { layMan ->
            if (layMan !is LinearLayoutManager)
                return@let

            var visiblePos: Int

            onItemsVisibilityListener?.apply {
                if (atLeastOneListenerFirst()) {
                    visiblePos = layMan.findFirstVisibleItemPosition()

                    when {
                        visiblePos == NO_POSITION -> { /*do nothing*/
                        }
                        visiblePos == 0 -> {
                            if (!flagNotifiedFirstVisible) {
                                onFirstItemVisible?.invoke()
                                flagNotifiedFirstVisible = true
                                flagNotifiedFirstInvisible = false
                            }
                        }

                        //if we get here, visiblePos is NOT 0
                        !flagNotifiedFirstInvisible -> {
                            onFirstItemInvisible?.invoke()
                            flagNotifiedFirstVisible = false
                            flagNotifiedFirstInvisible = true
                        }
                    }
                }

                if (atLeastOneListenerLast()) {
                    visiblePos = layMan.findLastVisibleItemPosition()
                    val numItems = adapter?.itemCount

                    when {
                        visiblePos == NO_POSITION || numItems == null -> { /*do nothing*/
                        }
                        visiblePos == (numItems - 1) -> {
                            if (!flagNotifiedLastVisible) {
                                onLastItemVisible?.invoke()
                                flagNotifiedLastVisible = true
                                flagNotifiedLastInvisible = false
                            }
                        }

                        //if we get here, lastPos is NOT (numItems -1)
                        !flagNotifiedLastInvisible -> {
                            onLastItemInvisible?.invoke()
                            flagNotifiedLastVisible = false
                            flagNotifiedLastInvisible = true
                        }
                    }
                }
            }
        }
    }
}