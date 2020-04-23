package com.hotmail.or_dvir.dxlibraries.clickable

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable.*
import kotlinx.android.synthetic.main.list_item.view.*

class AdapterClickable(mItems: List<ItemClickable>) :
    BaseSampleAdapter<ViewHolder>(mItems) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getDxAdapterItem<ItemClickable>(position)
        holder.tv.text = item.text
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
    }
}