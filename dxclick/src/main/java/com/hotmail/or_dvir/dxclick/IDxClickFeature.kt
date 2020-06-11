package com.hotmail.or_dvir.dxclick

import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * represents a feature that depends on an item click for its' functionality
 */
interface IDxClickFeature {
    val onItemClick: OnItemClickListener<IDxBaseItem>
    val onItemLongClick: OnItemLongClickListener<IDxBaseItem>
}