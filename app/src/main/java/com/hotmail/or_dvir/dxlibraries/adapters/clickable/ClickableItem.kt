package com.hotmail.or_dvir.dxlibraries.adapters.clickable

import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.featureclicklisteners.IDxItemClickable

class ClickableItem(val text: String) : IDxBaseItem, IDxItemClickable {
    override fun getViewType() = R.id.MyItemId
}