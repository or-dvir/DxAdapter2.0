package com.hotmail.or_dvir.dxrecyclerview

/**
 * a class containing individual visibility listeners for the first and last items in [DxRecyclerView]
 */
class DxVisibilityListener {

    /**
     * a listener to be invoked when the FIRST item on your list is VISIBLE.
     *
     * note that this will trigger immediately (assuming your adapter contains at least 1 item).
     */
    var onFirstItemVisible: EmptyListener? = null

    /**
     * a listener to be invoked when the FIRST item on your list is INVISIBLE.
     *
     * note that if the entire list fits in the [DxRecyclerView], this will NEVER trigger.
     */
    var onFirstItemInvisible: EmptyListener? = null

    /**
     * a listener to be invoked when the LAST item on your list is VISIBLE.
     *
     * note that if the entire list fits in the [DxRecyclerView], this will trigger immediately
     * (assuming your adapter contains at least 1 item).
     */
    var onLastItemVisible: EmptyListener? = null

    /**
     * a listener to be invoked when the LAST item on your list is INVISIBLE.
     *
     * note that if the entire list does NOT fit in the [DxRecyclerView], this will trigger immediately
     * (assuming your adapter contains at least 1 item).
     *
     * note that if the entire list DOES fit in the [DxRecyclerView], this will NEVER trigger.
     */
    var onLastItemInvisible: EmptyListener? = null

    internal fun atLeastOneListenerSet() = atLeastOneListenerFirst() || atLeastOneListenerLast()
    internal fun atLeastOneListenerFirst() = onFirstItemInvisible != null || onFirstItemVisible != null
    internal fun atLeastOneListenerLast() = onLastItemInvisible != null || onLastItemVisible != null
}