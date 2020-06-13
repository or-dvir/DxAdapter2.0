package com.hotmail.or_dvir.dxclick

import android.view.View

/**
 * a listener for long-clicking an item
 *
 * @param view the clicked view (the root view of the ViewHolder)
 * @param adapterPosition the adapter position of the clicked item
 * @param item the clicked item
 * @return true if the callback consumed the long-click, false otherwise
 */
typealias OnItemLongClickListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Boolean

/**
 * a listener for clicking an item
 *
 * @param view the clicked view (the root view of the ViewHolder)
 * @param adapterPosition the adapter position of the clicked item
 * @param item the clicked item
 */
typealias OnItemClickListener<ITEM> = (view: View, adapterPosition: Int, item: ITEM) -> Unit