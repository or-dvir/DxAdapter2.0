package com.hotmail.or_dvir.dxlibraries.swipeable

import android.view.View
import android.view.ViewGroup
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxlibraries.draggable.ItemNonDraggable

class AdapterNonSwipeable(mItems: MutableList<ItemNonSwipeable>) :
    BaseSampleAdapter<BaseSampleAdapter.ViewHolder>(mItems as MutableList<IDxBaseItem>) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_standard

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getDxAdapterItem<ItemNonDraggable>(position)
        holder.tv.text = item.text
    }
}