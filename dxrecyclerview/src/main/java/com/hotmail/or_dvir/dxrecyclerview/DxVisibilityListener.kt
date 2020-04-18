package com.hotmail.or_dvir.dxrecyclerview

/**
 * a class containing individual visibility items in [DxRecyclerView]
 *
 * @see onFirstItemVisible
 * @see onFirstItemInvisible
 * @see onLastItemVisible
 * @see onLastItemInvisible
 */
class DxVisibilityListener {
    internal var flagNotifiedFirstVisible = false
    internal var flagNotifiedFirstInvisible = false
    internal var flagNotifiedLastVisible = false
    internal var flagNotifiedLastInvisible = false

    /**
     * a listener to be invoked when the FIRST item on your list is VISIBLE.
     *
     * note that this will trigger immediately (assuming your adapter contains at least 1 item).
     */
    var onFirstItemVisible: GenericListener? = null

    /**
     * a listener to be invoked when the FIRST item on your list is INVISIBLE.
     *
     * note that if the entire list fits on the screen, this will NEVER trigger.
     */
    var onFirstItemInvisible: GenericListener? = null

    /**
     * a listener to be invoked when the LAST item on your list is VISIBLE.
     *
     * note that if the entire list fits on the screen, this will trigger immediately
     * (assuming your adapter contains at least 1 item).
     */
    var onLastItemVisible: GenericListener? = null

    /**
     * a listener to be invoked when the LAST item on your list is INVISIBLE.
     *
     * note that if the entire list does NOT fit on the screen, this will trigger immediately
     * (assuming your adapter contains at least 1 item).
     *
     * note that if the entire list DOES fit on the screen, this will NEVER trigger.
     */
    var onLastItemInvisible: GenericListener? = null

    internal fun atLeastOneListenerSet() = atLeastOneListenerFirst() || atLeastOneListenerLast()
    internal fun atLeastOneListenerFirst() =
        onFirstItemInvisible != null || onFirstItemVisible != null

    internal fun atLeastOneListenerLast() = onLastItemInvisible != null || onLastItemVisible != null
}