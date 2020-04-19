package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.featureclicklisteners.IDxClickable

class MyItem(val text: String) : IDxBaseItem, IDxClickable {
    override fun getViewType() = R.id.MyItemId
}