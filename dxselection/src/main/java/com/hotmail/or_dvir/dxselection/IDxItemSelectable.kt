package com.hotmail.or_dvir.dxselection

import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * represents an item that is selectable.
 */
interface IDxItemSelectable: IDxBaseItem {
    /**
     * holds the current selected state of this item.
     *
     * other then providing an initial value, do NOT change this variable yourself!
     * to select or deselect this item, use functions in [DxFeatureSelection]
     */
    var isSelected: Boolean
}