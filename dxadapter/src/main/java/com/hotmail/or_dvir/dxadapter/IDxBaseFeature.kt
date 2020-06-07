package com.hotmail.or_dvir.dxadapter

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * a base interface for all functionalities (click listeners, swipe, etc.)
 */
interface IDxBaseFeature {

    //IMPORTANT NOTE:
    //do NOT add generic <ITEM> to the adapter type!
    //it leads to not being able to easily call addFeature() method in DxAdapter class
    /**
     * a listener to be invoked whenever [RecyclerView.Adapter.onCreateViewHolder] is invoked
     * on your adapter
     */
    fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    )

    /**
     * a listener be invoked whenever [RecyclerView.Adapter.onBindViewHolder] is invoked
     * on your adapter
     */
    fun onBindViewHolder(adapter: DxAdapter<*, *>, itemView: View, holder: RecyclerView.ViewHolder)

    /**
     * a unique id used to identify the feature
     */
    @IdRes
    fun getFeatureId(): Int
}