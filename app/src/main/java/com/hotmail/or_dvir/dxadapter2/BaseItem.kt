package com.hotmail.or_dvir.dxadapter2

import com.hotmail.or_dvir.dxadapter.IDxBaseItem

open class BaseItem(val text: String) : IDxBaseItem {
    override fun getViewType() = R.id.MyItemId
}