package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.featureclicklisteners.IDxItemClickable

class MyItem(val text: String) : IDxBaseItem, IDxItemClickable{
    override fun getViewType() = R.id.MyItemId
}