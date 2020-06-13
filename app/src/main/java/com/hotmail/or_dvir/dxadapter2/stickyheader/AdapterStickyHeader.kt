package com.hotmail.or_dvir.dxadapter2.stickyheader

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter2.BaseItem
import com.hotmail.or_dvir.dxadapter2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapter2.R
import kotlinx.android.synthetic.main.list_item_header.view.*

class AdapterStickyHeader(mItems: MutableList<BaseItem>) :
    BaseSampleAdapter<BaseItem, RecyclerView.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(
        itemView: View,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.MyItemId -> BaseSampleAdapter.ViewHolder(itemView)
            R.id.MyHeaderId -> ViewHolder(itemView)
            else -> BaseSampleAdapter.ViewHolder(itemView) //just for compiler
        }
    }

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int): Int {
        return when (viewType) {
            R.id.MyItemId -> R.layout.list_item_standard
            R.id.MyHeaderId -> R.layout.list_item_header
            else -> R.layout.list_item_standard //just for compiler
        }
    }

    @Suppress("USELESS_IS_CHECK")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (val item = getItem(position)) {
            is ItemHeader -> (holder as ViewHolder).tv.text = item.text
            is BaseItem -> (holder as BaseSampleAdapter.ViewHolder).tv.text = item.text
        }
    }

    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_header_tv
    }
}