package com.hotmail.or_dvir.dxexpansion

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxclick.IDxClickFeature
import com.hotmail.or_dvir.dxclick.OnItemClickListener
import com.hotmail.or_dvir.dxclick.OnItemLongClickListener
import org.jetbrains.annotations.TestOnly

/**
 * a feature adding expand/collapse functionality to your adapter
 *
 * @param adapter your adapter.
 * @param clickFeature the [DxFeatureClick] used in your adapter.
 * @param defaultClickBehavior whether or not clicking an item will expand/collapse it.
 * @param onlyOneItemExpanded whether or not to allow for only 1 item to be expanded at any given time.
 *
 * note that this ignores the initial state of the item as set by [IDxItemExpandable],
 * and will only take effect once an item has been manually expanded (by user interaction or by code).
 *
 * @param onItemExpansionStateChanged a listener to be invoked when an item has been expanded or collapsed.
 *
 * note that this listener will only be invoked when manually expanding/collapsing an item
 * (by user interaction or by code) and NOT when [onBindViewHolder] is called.
 *
 * also note that any visual changes (e.g. the direction of the expansion handle) should be
 * done in [onBindViewHolder] and NOT in this listener
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
open class DxFeatureExpansion<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    clickFeature: DxFeatureClick<ITEM>,
    private var defaultClickBehavior: Boolean,
    private var onlyOneItemExpanded: Boolean,
    private val onItemExpansionStateChanged: OnItemExpansionStateChangedListener<ITEM>
) : IDxBaseFeature, IDxClickFeature {

    init {
        adapter.addFeature(clickFeature)
        clickFeature.clickListenerFeatures.add(this)
    }

    @TestOnly
    fun setOnlyOneItemExpanded(onlyOne: Boolean) {
        onlyOneItemExpanded = onlyOne
    }

    @TestOnly
    fun setExpandOnClick(expandOnClick: Boolean) {
        defaultClickBehavior = expandOnClick
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun onBindViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        val item = adapter.getItem(holder.adapterPosition)
        if (item is IDxItemExpandable && holder is ViewHolder) {
            holder.expandableView.apply {
                visibility =
                    if (item.isExpanded) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }
    }

    override fun getFeatureId() = R.id.feature_expansion

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override val onItemClick: OnItemClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            if (defaultClickBehavior && item is IDxItemExpandable) {
                //reverse the expansion
                if (item.isExpanded) {
                    collapse(adapterPosition)
                } else {
                    expand(adapterPosition)
                }

                //NOTE:
                //do NOT trigger any listeners here.
                //all listeners are controlled in expandOrCollapse()
            }
        }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override val onItemLongClick: OnItemLongClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            //do nothing (return false so we do not consume the event)
            false
        }

    private fun expandOrCollapse(shouldExpand: Boolean, items: List<ITEM>) {
        var tempPosition: Int

        items.forEach {
            if (it is IDxItemExpandable && shouldExpand != it.isExpanded) {
                it.isExpanded = shouldExpand
                tempPosition = adapter.getIndex(it)

                if (tempPosition != -1) {
                    onItemExpansionStateChanged.invoke(tempPosition, it.isExpanded, it)
                    adapter.notifyItemChanged(tempPosition)
                }
            }
        }
    }

    //region expand
    /**
     * expands the item in the given index.
     *
     * note that if the item is not expandable and [onlyOneItemExpanded] is true,
     * all currently expanded items will be collapsed
     */
    fun expand(index: Int) = expand(adapter.getItem(index))

    /**
     * expands the given item.
     *
     * note that if the item is not expandable and [onlyOneItemExpanded] is true,
     * all currently expanded items will be collapsed
     */
    fun expand(item: ITEM) = expand(listOf(item))

    /**
     * expands all of the given items.
     *
     * note that if none of the items are expandable and [onlyOneItemExpanded] is true,
     * all currently expanded items will be collapsed
     */
    fun expand(items: List<ITEM>) {
        if (onlyOneItemExpanded) {
            val firstExpandableItem = items.first { it is IDxItemExpandable }
            val itemsToCollapse = getAllExpandedItems().minus(firstExpandableItem)

            collapse(itemsToCollapse)
            expandOrCollapse(true, listOf(firstExpandableItem))
        } else {
            expandOrCollapse(true, items)
        }
    }

    /**
     * expands the items at the given indices.
     *
     * note that if none of the items are expandable and [onlyOneItemExpanded] is true,
     * all currently expanded items will be collapsed.
     *
     * also note that indices that are out of bounds of the adapter will be ignored
     */
    @JvmName("expandIndices")
    fun expand(indices: List<Int>) = expand(adapter.getItemsForIndices(indices))

    /**
     * expands all the items in the adapter
     */
    fun expandAll() = expand(adapter.getDxAdapterItems())
    //endregion

    //region collapse
    /**
     * collapses the item at the given index
     */
    fun collapse(index: Int) = collapse(adapter.getItem(index))

    /**
     * collapses the given item
     */
    fun collapse(item: ITEM) = collapse(listOf(item))

    /**
     * collapses all the given items
     */
    fun collapse(items: List<ITEM>) = expandOrCollapse(false, items)

    /**
     * collapses all the given indices.
     *
     * any indices that are out of bounds of the adapter will be ignored
     */
    @JvmName("collapseIndices")
    fun collapse(indices: List<Int>) = collapse(adapter.getItemsForIndices(indices))

    /**
     * collapses all the items in the adapter
     */
    fun collapseAll() = collapse(adapter.getDxAdapterItems())
    //endregion

    //region general
    /**
     * returns all currently expanded items
     */
    fun getAllExpandedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemExpandable && it.isExpanded }

    /**
     * returns the indices of all currently expanded items
     */
    //the returned list should NOT contain -1, because getAllExpandableItems()
    //only returns items that are already in the adapter
    fun getAllExpandedIndices() =
        adapter.getIndexList(getAllExpandedItems(), true)

    /**
     * returns the number of currently expanded items
     */
    fun getNumExpandedItems() = getAllExpandedItems().size

    //NOTE:
    //we must call this function every time and not use a global variable
    //because the adapter is dynamic and may change at anytime.
    //if we have a global variable, we will only ever access the initial list of items
    @VisibleForTesting
    fun getAllExpandableItems() =
        adapter.getDxAdapterItems().filterIsInstance<IDxItemExpandable>()
    //endregion

    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////

    /**
     * a [RecyclerView.ViewHolder] that holds a reference to a view that can be shown or hidden
     * when an item is expanded or collapsed
     */
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract val expandableView: View
    }
}