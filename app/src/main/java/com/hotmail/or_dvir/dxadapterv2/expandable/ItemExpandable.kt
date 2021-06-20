package com.hotmail.or_dvir.dxadapterv2.expandable

import com.hotmail.or_dvir.dxexpansion.IDxItemExpandable
import com.hotmail.or_dvir.dxadapterv2.BaseItem
import com.hotmail.or_dvir.dxadapterv2.R

class ItemExpandable(text: String) : BaseItem(text), IDxItemExpandable {
    //not expanded by default
    override var isExpanded = false
    //expansion enabled by default
    override var isExpansionEnabled = true
    override fun getViewType() = R.id.MyExpandableItemId
}
