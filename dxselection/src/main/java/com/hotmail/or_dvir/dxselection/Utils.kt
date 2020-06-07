package com.hotmail.or_dvir.dxselection

/**
 * a listener to be invoked when an item has been selected or deselected
 *
 * @param adapterPosition the position of the item
 * @param isSelected whether or not [item] is selected
 * @param item the item whose selection state has changed
 */
typealias OnItemSelectionChangedListener<ITEM> =
            (adapterPosition: Int, isSelected: Boolean, item: ITEM) -> Unit

/**
 * a listener to be invoked when selection mode has started or ended.
 *
 * "selection mode" is considered "started" if at least one item in your adapter is currently selected,
 * and considered "ended" if none of the items in your adapter are currently selected
 */
typealias OnSelectionModeStateChanged = (isSelectionModeActive: Boolean) -> Unit