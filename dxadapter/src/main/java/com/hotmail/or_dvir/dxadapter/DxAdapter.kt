package com.hotmail.or_dvir.dxadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class DxAdapter<ITEM : IDxBaseItem, VH : ViewHolder> : RecyclerView.Adapter<VH>() {
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

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        allFeatures.values.forEach {
            it.onBindViewHolder(this, holder.itemView, holder)
        }
    }

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

        if (filterNonExistingItems) {
            list = list.filter { it != -1 }
        }

        return list
    }

    /**
     * gets the item at [position]
     */
    fun getItem(position: Int) = getDxAdapterItems()[position]

    /**
     * returns a list of items at the given [indices].
     *
     * If an index in [indices] is out of bounds of the adapter, that index is ignored
     */
    fun getItemsForIndices(indices: List<Int>): List<ITEM> {
        val range = 0 until itemCount - 1
        val list = mutableListOf<ITEM>()

        indices.forEach {
            if (it in range) {
                list.add(getItem(it))
            }
        }

        return list
    }

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
}
