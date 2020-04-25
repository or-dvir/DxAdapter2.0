package com.hotmail.or_dvir.dxlibraries.draggable

import android.view.View
import android.view.ViewGroup
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.BaseItem.ViewHolder
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.R

class AdapterNonDraggable(mItems: List<ItemNonDraggable>) :
    BaseSampleAdapter<ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getDxAdapterItem<ItemNonDraggable>(position)
        holder.tv.text = item.text
    }
}