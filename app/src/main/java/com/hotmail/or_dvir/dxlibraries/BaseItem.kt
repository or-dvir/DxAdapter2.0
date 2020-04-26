package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxadapter.IDxBaseItem

abstract class BaseItem(val text: String) : IDxBaseItem {
    override fun getViewType() = R.id.MyItemId
}