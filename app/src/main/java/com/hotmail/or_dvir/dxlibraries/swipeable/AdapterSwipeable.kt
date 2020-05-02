package com.hotmail.or_dvir.dxlibraries.swipeable

import android.view.View
import android.view.ViewGroup
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxdragandswipe.IDxSwipeBackground
import com.hotmail.or_dvir.dxlibraries.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.R

class AdapterSwipeable(mItems: MutableList<ItemSwipeable>) :
    BaseSampleAdapter<AdapterSwipeable.ViewHolder>(mItems as MutableList<IDxBaseItem>) {

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int) =
        ViewHolder(itemView)

    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) =
        R.layout.list_item_swipeable

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getDxAdapterItem<ItemSwipeable>(position)
        holder.tv.text = item.text
    }

    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////

    class ViewHolder(itemView: View) : BaseSampleAdapter.ViewHolder(itemView),
        IDxSwipeBackground {
        //todo when documenting, add a note about the importance of getting
        // an already existing view (and not looking it up)
        // because this will be called many many times for DxItemTouchHelper
        override fun getItemForeground() = tv
    }
}