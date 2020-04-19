package com.hotmail.or_dvir.featureclicklisteners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

class DxFeatureClickListeners : IDxBaseFeature {

    var onItemClick: onItemClickListener? = null
    var onItemLongClick: onItemLongClickListener? = null

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        itemView.setOnClickListener { view ->
            val item = adapter.getDxAdapterItem<IDxBaseItem>(holder.adapterPosition)
            if (item is IDxItemClickable) {
                onItemClick?.invoke(view, holder.adapterPosition)
            }
        }

        itemView.setOnLongClickListener { view ->
            val item = adapter.getDxAdapterItem<IDxBaseItem>(holder.adapterPosition)
            if (item is IDxItemClickable) {
                onItemLongClick?.invoke(view, holder.adapterPosition) ?: true
            } else {
                //if the item is not clickable, we do not consume the event
                false
            }
        }
    }
}