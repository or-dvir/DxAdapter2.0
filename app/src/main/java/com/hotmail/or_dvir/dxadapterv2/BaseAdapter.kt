package com.hotmail.or_dvir.dxadapterv2

import android.view.LayoutInflater
import android.view.ViewGroup

class BaseAdapter(mItems: MutableList<BaseItem>) :
    BaseSampleAdapter<BaseItem, BaseSampleAdapter.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_standard, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        holder.tv.text = item.text
    }
}