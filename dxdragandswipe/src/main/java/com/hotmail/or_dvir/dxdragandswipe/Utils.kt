package com.hotmail.or_dvir.dxdragandswipe

import android.view.View

//region drag
/**
 * a listener to be invoked when a drag event happened for an item
 *
 * @param view the root view of the ViewHolder of [item] for which the drag event has occurred
 * @param adapterPosition the position of the item in the adapter
 * @param item the item on which the drag event has occurred
 */
typealias OnDragEventListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit

/**
 * a listener to be invoked just BEFORE 2 items are about to be switched.
 *
 * @param draggedView the root view of item being dragged.
 * @param draggedPosition the position of the item being dragged (BEFORE the switch).
 * @param draggedItem the item being dragged.
 * @param targetView the root view of the item being replaced.
 * @param targetPosition the position of the item being replaced (BEFORE the switch).
 * @param targetItem the item being replaced.
 */
typealias OnItemMovedListener<ITEM> = (
    draggedView: View, draggedPosition: Int, draggedItem: ITEM,
    targetView: View, targetPosition: Int, targetItem: ITEM
) -> Unit
//endregion

//region swipe
/**
 * a listener to be invoked when a swipe event happened for an item
 *
 * @param view the root view of the ViewHolder of [item] for which the swipe event has occurred
 * @param adapterPosition the position of the item in the adapter
 * @param item the item on which the swipe event has occurred
 */
typealias OnSwipeEventListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit

/**
 * a listener to be invoked when an item has been fully swiped.
 *
 * @param view the root view of the ViewHolder of the swiped item.
 * @param adapterPosition the position of the item that was swiped.
 * @param direction the direction of the swipe.
 *
 * note that this direction is related to the directions passed to DxFeatureSwipe in DxItemTouchCallback.
 * for example: if you passed ItemTouchHelper.RIGHT, this will be the value of [direction] and NOT
 * ItemTouchHelper.END (even though they might represent the same direction)
 *
 * @param item the item that was swiped.
 */
typealias OnItemSwipedListener<ITEM> =
            (view: View, adapterPosition: Int, direction: Int, item: ITEM) -> Unit
//endregion