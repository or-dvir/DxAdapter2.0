package com.hotmail.or_dvir.dxlibraries.stickyheader

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxstickyheader.DxFeatureStickyHeader
import kotlinx.android.synthetic.main.list_item_header.view.*

class MyStickyHeaderFeature(adapter: AdapterStickyHeader) :
    DxFeatureStickyHeader<BaseItem>(adapter) {

    override fun getHeaderLayoutRes() = R.layout.list_item_header

    override fun bindStickyHeader(headerView: View, headerAdapterPosition: Int, item: BaseItem) {
        headerView.listItem_header_tv.text = (item as ItemHeader).text
    }
}