package com.hotmail.or_dvir.dxadapter

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * a base interface for all functionalities (click listeners, swipe, etc.) to share
 */
interface IDxBaseFeature {

    //IMPORTANT NOTE:
    //do NOT add generic <ITEM> to the adapter type!
    //it leads to not being able to easily call addFeature() method in DxAdapter class
    fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    )

    /**
     * used internally by the library.
     *
     * DO NOT OVERRIDE!!!
     */
    //todo should i use a val instead? but then the @IdRes annotation does not work
    @IdRes
    fun getFeatureId(): Int
}