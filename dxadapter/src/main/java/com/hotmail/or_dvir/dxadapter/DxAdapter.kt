package com.hotmail.or_dvir.dxadapter

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * The main class of this library. your adapter must extend this class.
 *
 * Note that you must add any desired feature using [addFeature] function.
 *
 * @param ITEM the type of object this adapter will hold.
 * @param VH the [ViewHolder] this adapter will use
 */
abstract class DxAdapter<ITEM : IDxBaseItem, VH : ViewHolder> : RecyclerView.Adapter<VH>() {

    private val allFeatures: LinkedHashMap<Int, IDxBaseFeature> = LinkedHashMap()

    /**
     * adds a feature to this adapter (selection, click, expansion, etc...)
     *
     * if you do not add your feature using this function, it will not work properly.
     */
    fun addFeature(feature: IDxBaseFeature) =
        allFeatures.put(feature.getFeatureId(), feature)

    /**
     * removes a feature from this adapter (selection, click, expansion, etc...)
     */
    fun removeFeature(feature: IDxBaseFeature) =
        allFeatures.remove(feature.getFeatureId())

    /**
     * do not override this function directly. Use [createAdapterViewHolder]
     */
    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = createAdapterViewHolder(parent, viewType)

        allFeatures.values.forEach {
            it.onCreateViewHolder(this, holder.itemView, holder)
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
     * convenience function for [List.indexOf] for the items held by this adapter.
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
     * Returns a list of items at the given [indices].
     * Any out of bounds indices will be ignored
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

    /**
     * returns the list of [ITEM] this adapter holds
     */
    abstract fun getDxAdapterItems(): MutableList<ITEM>

    /**
     * wrapper for [onCreateViewHolder][RecyclerView.Adapter.onCreateViewHolder]
     *
     * use this function only. do NOT override
     * [onCreateViewHolder][RecyclerView.Adapter.onCreateViewHolder] directly.
     */
    abstract fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): VH
}
