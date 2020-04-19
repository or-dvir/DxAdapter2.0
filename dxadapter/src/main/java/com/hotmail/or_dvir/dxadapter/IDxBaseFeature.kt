package com.hotmail.or_dvir.dxadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * a base interface for all functionalities (click listeners, swipe, etc.) to share
 */
interface IDxBaseFeature {

    fun onCreateViewHolder(adapter: DxAdapter<*>, itemView: View, holder: RecyclerView.ViewHolder)
}