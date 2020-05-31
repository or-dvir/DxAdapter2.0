package com.hotmail.or_dvir.dxstickyheader

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

abstract class DxFeatureStickyHeader<ITEM : IDxBaseItem>(private val adapter: DxAdapter<ITEM, *>) :
    IDxBaseFeature {

    override fun onBindViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    internal fun isHeader(position: Int): Boolean {
        if (position !in 0 until adapter.getDxAdapterItems().size) {
            return false
        }

        return adapter.getItem(position) is IDxItemHeader
    }

    internal fun getItem(position: Int) = adapter.getItem(position)

    override fun getFeatureId() = R.id.feature_stickyHeader

    @LayoutRes
    abstract fun getHeaderLayoutRes(): Int

    abstract fun bindStickyHeader(headerView: View, headerAdapterPosition: Int, item: ITEM)
}