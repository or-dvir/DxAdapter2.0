package com.hotmail.or_dvir.dxrecyclerview

/**
 * a class containing individual scroll directions listeners (up, down, left, right),
 * with given sensitivities.
 *
 * The higher the sensitivity, the faster the user has to scroll for the listeners to trigger.
 *
 * @param sensitivityUp sensitivity for scrolling up
 * @param sensitivityDown sensitivity for scrolling down
 * @param sensitivityLeft sensitivity for scrolling left
 * @param sensitivityRight sensitivity for scrolling right
 */
class DxScrollListener(
    internal val sensitivityUp: Int,
    internal val sensitivityDown: Int,
    internal val sensitivityLeft: Int,
    internal val sensitivityRight: Int
) {

    /**
     * creates a [DxScrollListener] with the given sensitivity for all directions.
     */
    constructor(sensitivityAll: Int) : this(
        sensitivityAll,
        sensitivityAll,
        sensitivityAll,
        sensitivityAll
    )

    /**
     * a listener to be invoked when the list is scrolled up,
     * if the amount of scroll exceeds [sensitivityUp]
     */
    var onScrollUp: EmptyListener? = null

    /**
     * a listener to be invoked when the list is scrolled down,
     * if the amount of scroll exceeds [sensitivityDown]
     */
    var onScrollDown: EmptyListener? = null

    /**
     * a listener to be invoked when the list is scrolled left,
     * if the amount of scroll exceeds [sensitivityLeft]
     */
    var onScrollLeft: EmptyListener? = null

    /**
     * a listener to be invoked when the list is scrolled right,
     * if the amount of scroll exceeds [sensitivityRight]
     */
    var onScrollRight: EmptyListener? = null

    internal fun atLeastOneListenerSet() =
        onScrollUp != null || onScrollDown != null || onScrollLeft != null || onScrollRight != null
}