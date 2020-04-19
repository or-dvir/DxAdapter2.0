package com.hotmail.or_dvir.featureclicklisteners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature

class DxFeatureClickListeners : IDxBaseFeature {

    var onItemClick: onItemClickListener? = null
    var onItemLongClick: onItemLongClickListener? = null

    override fun onCreateViewHolder(
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        itemView.setOnClickListener { view ->
            if(item is IDxClickable) {
                onItemClick?.invoke(view, holder.adapterPosition)
            }
        }

        itemView.setOnLongClickListener { view ->
            onItemLongClick?.invoke(view, holder.adapterPosition) ?: true
        }
    }
}