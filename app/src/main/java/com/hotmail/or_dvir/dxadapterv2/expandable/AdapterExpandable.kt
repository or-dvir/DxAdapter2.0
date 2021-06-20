package com.hotmail.or_dvir.dxadapterv2.expandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hotmail.or_dvir.dxadapterv2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapterv2.R
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import kotlinx.android.synthetic.main.list_item_expandable.view.*

class AdapterExpandable(mItems: MutableList<ItemExpandable>) :
    BaseSampleAdapter<ItemExpandable, AdapterExpandable.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_expandable, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            tv.text = item.text

            if(item.isExpansionEnabled) {
                expandHandle.setImageResource(R.drawable.ic_arrow_down)
            } else {
                expandHandle.setImageResource(0)
            }
        }
    }

    //////////////////////////////////
    //////////////////////////////////
    //////////////////////////////////
    //////////////////////////////////

    class ViewHolder(itemView: View) : DxFeatureExpansion.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
        val expandHandle: ImageView = itemView.listItem_expandable_expandHandle
        override val expandableView: View = itemView.listItem_expandable_expansionRoot
    }
}