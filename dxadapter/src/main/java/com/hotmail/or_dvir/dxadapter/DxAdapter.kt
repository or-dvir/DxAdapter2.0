package com.hotmail.or_dvir.dxadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class DxAdapter<VH : ViewHolder> : RecyclerView.Adapter<VH>() {

    private val allFeatures: MutableList<IDxBaseFeature> = mutableListOf()

    fun addFeature(feature: IDxBaseFeature) {
        //todo make sure not to add duplicates!
        // the order of the data structure doesnt necessarily matter,
        // but the order of calling each feature does! make sure that all listeners
        // are always called in the same order so the user can rely on this
        allFeatures.add(feature)
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(getItemLayoutRes(parent, viewType), parent, false)

        val holder = createAdapterViewHolder(itemView, parent, viewType)

        allFeatures.forEach {
            it.onCreateViewHolder(this, itemView, holder)
        }

        return holder
    }

//    @CallSuper
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        //todo
//
//        //position parameter may not be accurate
////        val adapterPosition = holder.adapterPosition
////        bindViewHolder(holder, adapterPosition, getItems()[adapterPosition])
//    }

//    @CallSuper
//    override fun onViewRecycled(holder: ViewHolder) {
//        super.onViewRecycled(holder)
//
//        //todo
//
////        holder.adapterPosition.let {
////            if (it != RecyclerView.NO_POSITION) {
////                unbindViewHolder(holder, it, getItems()[it])
////            }
////        }
//    }

    override fun getItemCount() = getDxAdapterItems().size
    override fun getItemViewType(position: Int) = getDxAdapterItems()[position].getViewType()

    //todo should i check the cast? i would only throw an exception anyways...
    fun <T> getDxAdapterItem(position: Int) = getDxAdapterItems()[position] as T

    //
    // abstract functions
    //

    abstract fun getDxAdapterItems(): MutableList<IDxBaseItem>
//    abstract fun getItems(): List<ITEM>

    /**
     * wrapper for [onCreateViewHolder][RecyclerView.Adapter.onCreateViewHolder]
     * with the addition of [itemView].
     *
     * use this function only. do NOT override
     * [onCreateViewHolder][RecyclerView.Adapter.onCreateViewHolder] directly
     * @param itemView the inflated view returned from [getItemLayoutRes]
     */
    abstract fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int): VH

    /**
     * returns the layout resource id for the view to to inflate in [createAdapterViewHolder]
     * @param parent the same as in [createAdapterViewHolder]
     * @param viewType the same as in [createAdapterViewHolder]
     */
    @LayoutRes
    abstract fun getItemLayoutRes(parent: ViewGroup, viewType: Int): Int

    //todo commented out because i cannot have ITEM as a type of one of the parameters
    // as long as its marked as "out"
//    /**
//     * wrapper for [onBindViewHolder][RecyclerView.Adapter.onBindViewHolder]
//     * with the addition of [item].
//     *
//     * use this function only. do NOT override
//     * [onBindViewHolder][RecyclerView.Adapter.onBindViewHolder] directly
//     * @param item the item at [position]
//     */
//    abstract fun bindViewHolder(holder: ViewHolder, position: Int, item: ITEM)

    //todo commented out because i cannot have ITEM as a type of one of the parameters
    // as long as its marked as "out"
//    /**
//     * wrapper for [onViewRecycled][RecyclerView.Adapter.onViewRecycled]
//     * with the addition of [position] and [item].
//     *
//     * use this function only. do NOT override
//     * [onViewRecycled][RecyclerView.Adapter.onViewRecycled] directly
//     * @param position the adapter position being recycled
//     * @param item the item associated with [position]
//     */
//    abstract fun unbindViewHolder(holder: ViewHolder, position: Int, item: ITEM)
}