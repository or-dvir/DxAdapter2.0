package com.hotmail.or_dvir.dxrecyclerview

/**
 * a convenience class containing individual directional scroll listeners (up, down, left, right),
 * with the sensitivities.
 *
 * The higher the sensitivity, the faster the user has to scroll for the listeners to trigger.
 *
 * @param sensitivityUp sensitivity for scrolling up
 * @param sensitivityDown sensitivity for scrolling down
 * @param sensitivityLeft sensitivity for scrolling left
 * @param sensitivityRight sensitivity for scrolling right
 */
open class DxScrollListener(
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

    //region optional variables
    /**
     * a listener to be invoked when the list is scrolled up,
     * if the amount of scroll exceeds [sensitivityUp]
     */
    var onScrollUp: GenericListener? = null

    /**
     * a listener to be invoked when the list is scrolled down,
     * if the amount of scroll exceeds [sensitivityDown]
     */
    var onScrollDown: GenericListener? = null

    /**
     * a listener to be invoked when the list is scrolled left,
     * if the amount of scroll exceeds [sensitivityLeft]
     */
    var onScrollLeft: GenericListener? = null

    /**
     * a listener to be invoked when the list is scrolled right,
     * if the amount of scroll exceeds [sensitivityRight]
     */
    var onScrollRight: GenericListener? = null
    //endregion

    internal fun atLeastOneListenerSet() =
        onScrollUp != null || onScrollDown != null || onScrollLeft != null || onScrollRight != null
}