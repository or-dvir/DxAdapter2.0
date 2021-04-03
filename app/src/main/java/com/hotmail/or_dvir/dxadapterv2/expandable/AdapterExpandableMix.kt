package com.hotmail.or_dvir.dxadapterv2.expandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapterv2.BaseItem
import com.hotmail.or_dvir.dxadapterv2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapterv2.R
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import kotlinx.android.synthetic.main.list_item_expandable.view.*

class AdapterExpandableMix(mItems: MutableList<BaseItem>) :
    BaseSampleAdapter<BaseItem, RecyclerView.ViewHolder>(mItems) {

    override fun createAdapterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.MyItemId -> BaseSampleAdapter.ViewHolder(
                getItemView(parent, R.layout.list_item_standard)
            )
            R.id.MyExpandableItemId -> ViewHolder(
                getItemView(parent, R.layout.list_item_expandable)
            )
            //just for compiler
            else -> BaseSampleAdapter.ViewHolder(getItemView(parent, R.layout.list_item_standard))
        }
    }

    private fun getItemView(parent: ViewGroup, @LayoutRes layoutRes: Int) =
        LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

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
        val tv: TextView = itemView.listItem_tv
        override val expandableView: View = itemView.listItem_expandable_expansionRoot
    }
}