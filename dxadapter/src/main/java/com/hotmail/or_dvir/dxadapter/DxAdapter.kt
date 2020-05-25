package com.hotmail.or_dvir.dxadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class DxAdapter<ITEM : IDxBaseItem, VH : ViewHolder> : RecyclerView.Adapter<VH>() {

    //    private val allFeatures: MutableList<IDxBaseFeature> = mutableListOf()
    private val allFeatures: LinkedHashMap<Int, IDxBaseFeature> = LinkedHashMap()

    fun addFeature(feature: IDxBaseFeature) =
        allFeatures.put(feature.getFeatureId(), feature)

    fun removeFeature(feature: IDxBaseFeature) =
        allFeatures.remove(feature.getFeatureId())

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(getItemLayoutRes(parent, viewType), parent, false)

        val holder = createAdapterViewHolder(itemView, parent, viewType)

        allFeatures.values.forEach {
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

    /**
     * convenience function for [List.indexOf]
     */
    fun getIndex(item: ITEM) = getDxAdapterItems().indexOf(item)

    /**
     * returns a list of indices for the given [items].
     *
     * note that the returned list may contain -1 as it uses [List.indexOf].
     * To avoid this, set [filterNonExistingItems] to true
     */
    fun getIndexList(items: List<ITEM>, filterNonExistingItems: Boolean = false): List<Int> {
        var list = items.map { getIndex(it) }

        if(filterNonExistingItems) {
            list = list.filter { it != -1 }
        }

        return list
    }

    /**
     * gets the item at [position]
     */
    fun getItem(position: Int) = getDxAdapterItems()[position]

    /**
     * returns a list of items at the given [indices]
     */
    fun getItemsForIndices(indices: List<Int>) =
        indices.map { getItem(it) }

    abstract fun getDxAdapterItems(): MutableList<ITEM>

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
