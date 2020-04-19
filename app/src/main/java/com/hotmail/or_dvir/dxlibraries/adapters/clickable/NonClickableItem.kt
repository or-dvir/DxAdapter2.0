package com.hotmail.or_dvir.dxlibraries.adapters.clickable

import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.featureclicklisteners.IDxClickable

class NonClickableItem(val text: String) : IDxBaseItem {
    override fun getViewType() = R.id.MyItemId
}