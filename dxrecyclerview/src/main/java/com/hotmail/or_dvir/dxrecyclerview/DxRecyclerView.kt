package com.hotmail.or_dvir.dxrecyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener.*
import kotlin.math.abs

/**
 * a wrapper for RecyclerView with built-in listeners
 */
class DxRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    /**
     * a listener to be invoked when this [DxRecyclerView] is scrolled.
     */
    var onItemsVisibilityListener: DxVisibilityListener? = null

    /**
     * a listener that will be invoked when this [DxRecyclerView] is scrolled.
     */
    var onScrollListener: DxScrollListener? = null

    private var notifiedFirstVisible = false
    private var notifiedFirstInvisible = false
    private var notifiedLastVisible = false
    private var notifiedLastInvisible = false

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

                    onItemsVisibilityListener?.apply {
                        if (atLeastOneListenerSet()) {
                            triggerVisibilityListeners()
                        }
                    }
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

    private fun triggerVisibilityListeners() {
        layoutManager?.let { layMan ->

            if (layMan !is LinearLayoutManager)
                return@let

            var visiblePos: Int

            onItemsVisibilityListener?.let {

                if (it.atLeastOneListenerFirst()) {
                    visiblePos = layMan.findFirstVisibleItemPosition()

                    when {
                        visiblePos == NO_POSITION -> { /*do nothing*/
                        }
                        visiblePos == 0 -> {
                            if (!notifiedFirstVisible) {
                                it.onFirstItemVisible?.invoke()
                                notifiedFirstVisible = true
                                notifiedFirstInvisible = false
                            }
                        }

                        //if we get here, visiblePos is NOT 0
                        !notifiedFirstInvisible -> {
                            it.onFirstItemInvisible?.invoke()
                            notifiedFirstVisible = false
                            notifiedFirstInvisible = true
                        }
                    }
                }

                if (it.atLeastOneListenerLast()) {
                    visiblePos = layMan.findLastVisibleItemPosition()
                    val numItems = adapter?.itemCount

                    when {
                        visiblePos == NO_POSITION || numItems == null -> { /*do nothing*/
                        }
                        visiblePos == (numItems - 1) -> {
                            if (!notifiedLastVisible) {
                                it.onLastItemVisible?.invoke()
                                notifiedLastVisible = true
                                notifiedLastInvisible = false
                            }
                        }

                        //if we get here, lastPos is NOT (numItems -1)
                        !notifiedLastInvisible -> {
                            it.onLastItemInvisible?.invoke()
                            notifiedLastVisible = false
                            notifiedLastInvisible = true
                        }
                    }
                }
            }
        }
    }
}