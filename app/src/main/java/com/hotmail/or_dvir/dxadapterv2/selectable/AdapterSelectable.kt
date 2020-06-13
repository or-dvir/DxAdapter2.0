package com.hotmail.or_dvir.dxadapterv2.selectable

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import com.hotmail.or_dvir.dxadapterv2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapterv2.R

class AdapterSelectable(mItems: MutableList<ItemSelectable>) :
    BaseSampleAdapter<ItemSelectable, BaseSampleAdapter.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_standard

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.apply {
            tv.text = item.text
            itemView.background = ColorDrawable(
                if (item.isSelected) {
                    Color.RED
                } else {
                    Color.WHITE
                }
            )
        }
    }
}