package com.hotmail.or_dvir.dxexpandable

import com.hotmail.or_dvir.dxclick.IDxItemClickable

/**
 * represents an item that is selectable.
 */
interface IDxItemExpandable: IDxItemClickable {
    /**
     * holds the current selected state of this item.
     *
     * other then providing an initial value, do NOT change this variable yourself!
     * to select or deselect this item, use functions in [DxFeatureExpandable]
     */
    var isSelected: Boolean
}