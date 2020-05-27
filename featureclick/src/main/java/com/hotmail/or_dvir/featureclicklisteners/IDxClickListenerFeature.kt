package com.hotmail.or_dvir.featureclicklisteners

import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * represents an item that wishes to know when an item is clicked or long-clicked
 */
interface IDxClickListenerFeature {
    val onItemClick: OnItemClickListener<IDxBaseItem>
    val onItemLongClick: OnItemLongClickListener<IDxBaseItem>
}