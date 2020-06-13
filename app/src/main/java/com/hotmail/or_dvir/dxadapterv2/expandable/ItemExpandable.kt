package com.hotmail.or_dvir.dxadapterv2.expandable

import com.hotmail.or_dvir.dxexpansion.IDxItemExpandable
import com.hotmail.or_dvir.dxadapterv2.BaseItem
import com.hotmail.or_dvir.dxadapterv2.R

class ItemExpandable(text: String) : BaseItem(text), IDxItemExpandable {
    override var isExpanded = false
    override fun getViewType() = R.id.MyExpandableItemId
}
