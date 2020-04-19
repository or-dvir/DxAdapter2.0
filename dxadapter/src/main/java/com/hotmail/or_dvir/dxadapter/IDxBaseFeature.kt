package com.hotmail.or_dvir.dxadapter

import android.view.View

/**
 * a base interface for all functionalities (click listeners, swipe, etc.) to share
 */
interface IDxBaseFeature {

    fun onCreateViewHolder(itemView: View, adapterPosition: Int)
}