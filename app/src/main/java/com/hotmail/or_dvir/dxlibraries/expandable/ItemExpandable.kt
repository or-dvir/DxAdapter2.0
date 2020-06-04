package com.hotmail.or_dvir.dxlibraries.expandable

import com.hotmail.or_dvir.dxexpansion.IDxItemExpandable
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.R

class ItemExpandable(text: String) : BaseItem(text), IDxItemExpandable {
    override var isExpanded = false
    override fun getViewType() = R.id.MyExpandableItemId
}
