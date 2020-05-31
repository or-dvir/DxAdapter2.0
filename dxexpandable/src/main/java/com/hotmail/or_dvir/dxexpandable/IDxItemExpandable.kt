package com.hotmail.or_dvir.dxexpandable

import com.hotmail.or_dvir.dxclick.IDxItemClickable

/**
 * represents an item that is expandable.
 */
interface IDxItemExpandable: IDxItemClickable {
    /**
     * holds the current expansion state of this item.
     *
     * other then providing an initial value, do NOT change this variable yourself!
     * to expand or collapse this item, use functions in [DxFeatureExpandable]
     */
    var isExpanded: Boolean
}