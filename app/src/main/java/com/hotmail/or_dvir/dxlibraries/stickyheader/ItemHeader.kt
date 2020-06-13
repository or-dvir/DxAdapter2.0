package com.hotmail.or_dvir.dxlibraries.stickyheader

import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxstickyheader.IDxItemHeader

class ItemHeader(text: String) : BaseItem(text), IDxItemHeader {
    override fun getViewType() = R.id.MyHeaderId
}
