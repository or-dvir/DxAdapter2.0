package com.hotmail.or_dvir.dxadapter2.draggable

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter2.BaseSampleAdapter
import com.hotmail.or_dvir.dxadapter2.R
import com.hotmail.or_dvir.dxadapter2.draggable.AdapterDraggable.ViewHolder
import kotlinx.android.synthetic.main.list_item_standard.view.*

class AdapterDraggable(mItems: MutableList<ItemDraggable>) :
    BaseSampleAdapter<ItemDraggable, ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_draggable

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.tv.text = item.text
    }

    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
    }
}