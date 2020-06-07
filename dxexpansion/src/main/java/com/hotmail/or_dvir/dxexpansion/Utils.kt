package com.hotmail.or_dvir.dxexpansion

/**
 * a listener to be invoked when the expanded state of an item has changed.
 *
 * @param adapterPosition the position of the item
 * @param isExpanded whether the item is expanded or not
 * @param item the item for which the expansion state has changed
 */
typealias OnItemExpansionStateChangedListener<ITEM> =
            (adapterPosition: Int, isExpanded: Boolean, item: ITEM) -> Unit