package com.hotmail.or_dvir.dxadapterv2.stickyheader

import com.hotmail.or_dvir.dxadapterv2.BaseItem
import com.hotmail.or_dvir.dxadapterv2.R
import com.hotmail.or_dvir.dxstickyheader.IDxItemHeader

class ItemHeader(text: String) : BaseItem(text), IDxItemHeader {
    override fun getViewType() = R.id.MyHeaderId
}
