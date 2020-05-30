package com.example.dxstickeyheader

import android.view.View
import android.widget.AdapterView
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

abstract class DxFeatureStickyHeader<ITEM : IDxBaseItem>(private val adapter: DxAdapter<ITEM, *>) :
    IDxBaseFeature {

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //todo()
    }

    override fun getFeatureId() = R.id.feature_stickyHeader

    /**
     * returns the resource id of the header layout
     */
    //todo add support for multiple types of headers- in order to do this you need to
    // add position parameter!!!
    // note that this might require some changes in the adapter!!!
    @LayoutRes
    abstract fun getHeaderLayoutRes(/*headerPosition: Int*/): Int

    abstract fun bindStickyHeader(stickyHeader: View, headerAdapterPosition: Int, item: ITEM)
}