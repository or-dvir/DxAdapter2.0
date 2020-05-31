package com.hotmail.or_dvir.dxexpansion

/**
 * a listener called when the expanded state of an item has changed
 * @param view the view that was changed
 * @param adapterPosition the adapter position of the changed item
 * @param isExpanded the new expanded state of the item at [adapterPosition]
 */
typealias OnItemExpansionStateChangedListener<ITEM> =
            (adapterPosition: Int, isExpanded: Boolean, item: ITEM) -> Unit