package com.hotmail.or_dvir.featureclicklisteners

import android.view.View

/**
 * a listener called when an item is long-clicked
 *
 * @param view the clicked view
 * @param adapterPosition the adapter position of the clicked item
 * @param item the clicked item
 * @return true if the callback consumed the long click, false otherwise
 */
typealias OnItemLongClickListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Boolean

/**
 * a listener called when an item is clicked
 *
 * @param view the clicked view
 * @param adapterPosition the adapter position of the clicked item
 * @param item the clicked item
 */
typealias OnItemClickListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit