package com.hotmail.or_dvir.dxadapterv2.draggable

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hotmail.or_dvir.dxadapterv2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapterv2.R

class AdapterNonDraggable(mItems: MutableList<ItemNonDraggable>) :
    BaseSampleAdapter<ItemNonDraggable, AdapterDraggable.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDraggable.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_draggable, parent, false)

        return AdapterDraggable.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterDraggable.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.tv.text = item.text
    }
}