package com.hotmail.or_dvir.dxlibraries

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import kotlinx.android.synthetic.main.list_item_standard.view.*

abstract class BaseSampleAdapter<ITEM: IDxBaseItem, VH : RecyclerView.ViewHolder>(
    var mItems: MutableList<ITEM>
) : DxAdapter<ITEM, VH>() {

    override fun getDxAdapterItems() = mItems

    fun setItems(items: MutableList<ITEM>) {
        mItems = items
        notifyDataSetChanged()
    }

    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
    }
}