package com.hotmail.or_dvir.dxlibraries.adapters.clickable

import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.featureclicklisteners.IDxClickable

class ItemClickable(val text: String) : IDxBaseItem, IDxClickable {
    override fun getViewType() = R.id.MyItemId
}