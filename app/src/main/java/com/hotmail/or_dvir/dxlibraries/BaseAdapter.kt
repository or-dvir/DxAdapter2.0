package com.hotmail.or_dvir.dxlibraries

import android.view.View
import android.view.ViewGroup

class BaseAdapter(mItems: MutableList<BaseItem>) :
    BaseSampleAdapter<BaseItem, BaseSampleAdapter.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_standard

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        holder.tv.text = item.text
    }
}