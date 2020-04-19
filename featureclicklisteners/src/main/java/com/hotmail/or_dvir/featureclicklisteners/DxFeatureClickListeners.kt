package com.hotmail.or_dvir.featureclicklisteners

import android.view.View
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

class DxFeatureClickListeners<ITEM : IDxBaseItem> : IDxBaseFeature<ITEM> {

    var onItemClick: onItemClickListener<ITEM>? = null
    var onItemLongClick: onItemLongClickListener<ITEM>? = null

    override fun onCreateViewHolder(itemView: View, adapterPosition: Int, item: ITEM) {
        if (item !is IDxItemClickable) {
            return
        }

        itemView.setOnClickListener { view ->
            onItemClick?.invoke(view, adapterPosition, item)
        }

        itemView.setOnLongClickListener { view ->
            onItemLongClick?.invoke(view, adapterPosition, item) ?: true
        }
    }
}