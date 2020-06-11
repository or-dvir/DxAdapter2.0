package com.hotmail.or_dvir.dxrecyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxidlingresource.DxCountingIdlingResource
import kotlin.math.abs

/**
 * a wrapper for [RecyclerView] with built-in listeners.
 *
 * @see onItemsVisibilityListener
 * @see onScrollListener
 * @see idlingResource
 */
open class DxRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    companion object {
        val TAG = DxRecyclerView::class.java.simpleName
        val IDLING_RESOURCE_NAME = "${TAG}IdlingResource"
    }

    /**
     * if you use [DxRecyclerView] in your app and wish to perform some automated UI tests,
     * you can use this already existing [DxCountingIdlingResource].
     *
     * note that you need to register this idling resource in your tests.
     */
    val idlingResource = DxCountingIdlingResource(IDLING_RESOURCE_NAME)

    //region optional listeners
    /**
     * a visibility listener for items in this [DxRecyclerView].
     * the listener may be invoked when set, and when the recycler view is scrolled.
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
     * a listener to be invoked when this [DxRecyclerView] is scrolled.
     *
     * @see DxScrollListener
     */
    var onScrollListener: DxScrollListener? = null
    //endregion

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

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    //IMPORTANT NOTE!!!
                    //there are 2 states related to dragging and only 1 state related to
                    //stopping the drag event, and its therefore possible for the
                    //idling resource to be incremented twice but only decremented once,
                    //which would lead to UI tests getting stuck.
                    //in addition, SCROLL_STATE_SETTLING is not always called - for example if
                    //the recycler view is perfectly still when the user lifts their finger off
                    //the screen.
                    //the SCROLL_STATE_DRAGGING however, is fired only once when the user
                    //starts dragging, and is therefore the chosen state for incrementing
                    //the idling resource.

                    when (newState) {
                        //increment idlingResource until scroll event finished so UI tests don't fail
                        SCROLL_STATE_DRAGGING ->
                            idlingResource.increment("${TAG}.onScrollStateChanged()")
                        //scrolling event finished - decrement idlingResource so UI tests can continue
                        SCROLL_STATE_IDLE ->
                            idlingResource.decrement("${TAG}.onScrollStateChanged()")
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    invokeScrollListener(dx, dy)
                    invokeVisibilityListeners()
                }
            })
    }

    private fun invokeScrollListener(dx: Int, dy: Int) {
        if (onScrollListener == null || !onScrollListener!!.atLeastOneListenerSet()) {
            return
        }

        onScrollListener!!.apply {
            when {
                //up
                dy < 0 -> {
                    if (abs(dy) > sensitivityUp) {
                        onScrollUp?.invoke()
                    }
                }
                //down
                dy > 0 -> {
                    if (abs(dy) > sensitivityDown) onScrollDown?.invoke()
                }
                //left
                dx < 0 -> {
                    if (abs(dx) > sensitivityLeft) onScrollLeft?.invoke()
                }
                //right
                dx > 0 -> {
                    if (abs(dx) > sensitivityRight) onScrollRight?.invoke()
                }
            }
        }
    }

    private fun invokeVisibilityListeners() {
        if (onItemsVisibilityListener == null || !onItemsVisibilityListener!!.atLeastOneListenerSet()) {
            return
        }

        layoutManager?.let { layMan ->
            if (layMan !is LinearLayoutManager) {
                return@let
            }

            onItemsVisibilityListener?.apply {
                //notes:
                //-all of the following logic depends on layMan.findFirstVisibleItemPosition()
                //which does NOT take into account changes since the last layout pass.
                //therefore we must make sure to wait for the layout pass in order to receive
                //the most up-to-date information
                //
                //-since there is some delay until the next layout pass, we increment idlingResource
                //so UI tests don't fail.
                idlingResource.increment("${TAG}.invokeVisibilityListeners()")
                post {
                    var visiblePos: Int

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

                    //post{} block finished - decrement idlingResource so UI tests can continue
                    idlingResource.decrement("${TAG}.invokeVisibilityListeners()")
                }
            }
        }
    }
}