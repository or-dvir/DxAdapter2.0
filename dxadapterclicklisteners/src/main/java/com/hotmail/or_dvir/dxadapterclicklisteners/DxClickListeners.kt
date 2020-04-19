package com.hotmail.or_dvir.dxadapterclicklisteners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.IDxBaseFunctionality

class DxClickListeners : IDxBaseFunctionality {

    var onItemClick: onItemClickListener? = null
    var onItemLongClick: onItemLongClickListener? = null

    override fun onCreateViewHolder(itemView: View, holder: RecyclerView.ViewHolder) {

        itemView.setOnClickListener { view ->
            onItemClick?.invoke(view, holder.adapterPosition)
        }

        itemView.setOnLongClickListener { view ->
            onItemLongClick?.invoke(view, holder.adapterPosition) ?: true
        }
    }
}