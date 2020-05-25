package com.hotmail.or_dvir.featureclicklisteners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

class DxFeatureClick<ITEM : IDxBaseItem>(
    private val onItemClick: OnItemClickListener<ITEM>,
    private val onItemLongClick: OnItemLongClickListener<ITEM>
) : IDxBaseFeature {

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        itemView.setOnClickListener { view ->
            val item = adapter.getItem(holder.adapterPosition)
            if (item is IDxItemClickable) {
                onItemClick.invoke(view, holder.adapterPosition, item as ITEM)
            }
        }

        itemView.setOnLongClickListener { view ->
            val item = adapter.getItem(holder.adapterPosition)
            if (item is IDxItemClickable) {
                onItemLongClick.invoke(view, holder.adapterPosition, item as ITEM)
            } else {
                //if the item is not clickable, we do not consume the event
                false
            }
        }
    }

    override fun getFeatureId() = R.id.feature_clickListener
}