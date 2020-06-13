package com.hotmail.or_dvir.dxadapter2.stickyheader

import android.view.View
import com.hotmail.or_dvir.dxadapter2.BaseItem
import com.hotmail.or_dvir.dxadapter2.R
import com.hotmail.or_dvir.dxstickyheader.DxFeatureStickyHeader
import kotlinx.android.synthetic.main.list_item_header.view.*

class MyStickyHeaderFeature(adapter: AdapterStickyHeader) :
    DxFeatureStickyHeader<BaseItem>(adapter) {

    override fun getHeaderLayoutRes() = R.layout.list_item_header

    override fun bindStickyHeader(headerView: View, headerAdapterPosition: Int, item: BaseItem) {
        headerView.listItem_header_tv.text = (item as ItemHeader).text
    }
}