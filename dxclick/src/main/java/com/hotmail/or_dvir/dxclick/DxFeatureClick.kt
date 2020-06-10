package com.hotmail.or_dvir.dxclick

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * A feature that adds click listeners functionality to your adapter.
 */
open class DxFeatureClick<ITEM : IDxBaseItem>(
    private val onItemClick: OnItemClickListener<ITEM>,
    private val onItemLongClick: OnItemLongClickListener<ITEM>
) : IDxBaseFeature {

    /**
     * do NOT access or modify this field. It's used internally by the library.
     */
    val clickListenerFeatures =
        mutableListOf<IDxClickListenerFeature>()

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        itemView.setOnClickListener { view ->
            val item = adapter.getItem(holder.adapterPosition)
            if (item is IDxItemClickable) {
                onItemClick.invoke(view, holder.adapterPosition, item as ITEM)
                clickListenerFeatures.forEach {
                    it.onItemClick(view, holder.adapterPosition, item)
                }
            }
        }

        itemView.setOnLongClickListener { view ->
            val item = adapter.getItem(holder.adapterPosition)
            if (item is IDxItemClickable) {
                val result = onItemLongClick.invoke(view, holder.adapterPosition, item as ITEM)
                clickListenerFeatures.forEach {
                    it.onItemLongClick(view, holder.adapterPosition, item)
                }

                result
            } else {
                //if the item is not clickable, we do not consume the event
                false
            }
        }
    }

    override fun onBindViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_clickListener
}