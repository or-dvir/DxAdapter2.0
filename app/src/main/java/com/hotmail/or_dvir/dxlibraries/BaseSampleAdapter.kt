package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

abstract class BaseSampleAdapter<VH : RecyclerView.ViewHolder>(var mItems: List<IDxBaseItem>) :
    DxAdapter<VH>() {

    override fun getDxAdapterItems(): List<IDxBaseItem> = mItems

    fun setItems(items: List<IDxBaseItem>) {
        mItems = items
        notifyDataSetChanged()
    }

    fun getItems() = mItems
}