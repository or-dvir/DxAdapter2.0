package com.hotmail.or_dvir.dxlibraries

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import kotlinx.android.synthetic.main.list_item.view.*

class MyAdapter(private var mItems: List<MyItem>) : DxAdapter<MyAdapter.ViewHolder>() {
    override fun getDxAdapterItems() = mItems

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) = R.layout.list_item

    fun getItems() = mItems

    fun setItems(items: List<MyItem>) {
        mItems = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = mItems[position].text
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.listItem_tv
    }
}