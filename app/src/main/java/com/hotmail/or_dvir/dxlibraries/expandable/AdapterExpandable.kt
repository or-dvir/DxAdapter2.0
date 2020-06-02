package com.hotmail.or_dvir.dxlibraries.expandable

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.R
import kotlinx.android.synthetic.main.list_item_expandable.view.*

class AdapterExpandable(mItems: MutableList<ItemExpandable>) :
    BaseSampleAdapter<ItemExpandable, AdapterExpandable.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_expandable

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.tv.text = item.text
    }

    //////////////////////////////////
    //////////////////////////////////
    //////////////////////////////////
    //////////////////////////////////

    class ViewHolder(itemView: View) : DxFeatureExpansion.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
        override val expandableView: View = itemView.listItem_expandable_expansionRoot
    }

    //////////////////////////////////////////////////////////////////////
}