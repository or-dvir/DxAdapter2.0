package com.hotmail.or_dvir.dxlibraries.stickyheader

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxlibraries.expandable.ItemExpandable
import kotlinx.android.synthetic.main.list_item_expandable.view.*
import kotlinx.android.synthetic.main.list_item_header.view.*

class AdapterExpandableMix(mItems: MutableList<BaseItem>) :
    BaseSampleAdapter<BaseItem, RecyclerView.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(
        itemView: View,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.MyItemId -> BaseSampleAdapter.ViewHolder(itemView)
            R.id.MyExpandableItemId -> ViewHolder(itemView)
            else -> BaseSampleAdapter.ViewHolder(itemView) //just for compiler
        }
    }

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int): Int {
        return when (viewType) {
            R.id.MyItemId -> R.layout.list_item_standard
            R.id.MyExpandableItemId -> R.layout.list_item_expandable
            else -> R.layout.list_item_standard //just for compiler
        }
    }

    @Suppress("USELESS_IS_CHECK")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (val item = getItem(position)) {
            is ItemExpandable -> (holder as ViewHolder).tv.text = item.text
            is BaseItem -> (holder as BaseSampleAdapter.ViewHolder).tv.text = item.text
        }
    }

    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////

    class ViewHolder(itemView: View) : DxFeatureExpansion.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_header_tv
        override val expandableView: View = itemView.listItem_expandable_expansionRoot
    }
}