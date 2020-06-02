package com.hotmail.or_dvir.dxexpansion

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxclick.IDxClickListenerFeature
import com.hotmail.or_dvir.dxclick.OnItemClickListener
import com.hotmail.or_dvir.dxclick.OnItemLongClickListener
import org.jetbrains.annotations.TestOnly

@Suppress("MemberVisibilityCanBePrivate", "unused")
class DxFeatureExpansion<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    clickFeature: DxFeatureClick<ITEM>,
    private var expandAndCollapseOnClick: Boolean,
    private val onItemExpansionStateChanged: OnItemExpansionStateChangedListener<ITEM>
) : IDxBaseFeature, IDxClickListenerFeature {

    //todo
    // add option for only 1 item expanded

    init {
        adapter.addFeature(clickFeature)
        clickFeature.clickListenerFeatures.add(this)
    }

    @TestOnly
    fun setExpandOnClick(expandOnClick: Boolean) {
        expandAndCollapseOnClick = expandOnClick
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

        //todo
        // should i trigger the listener here?
        // unlike selection which the user can interpret in different ways,
        //      in this case there are only 2 possible cases: expanded view is visible or gone
        // this is the old explanation (from selection feature):
        //  when documenting add a note that the listeners will only be invoked when an item is
        //  manually expanded/collapsed (and not here).
        //  any visual changes (like expansion handle) should be done in the user's adapter using the
        //  IDxItemExpandable.isExpanded field.
        //  the onItemExpansionStateChanged listener can be used to... what?
    }

    override fun getFeatureId() = R.id.feature_expansion

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override val onItemClick: OnItemClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            if (expandAndCollapseOnClick && item is IDxItemExpandable) {
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
    fun expand(index: Int) = expand(adapter.getItem(index))
    fun expand(item: ITEM) = expand(listOf(item))
    fun expand(items: List<ITEM>) = expandOrCollapse(true, items)

    @JvmName("expandIndices")
    fun expand(indices: List<Int>) = expand(adapter.getItemsForIndices(indices))
    fun expandAll() = expand(adapter.getDxAdapterItems())
    //endregion

    //region collapse
    fun collapse(index: Int) = collapse(adapter.getItem(index))
    fun collapse(item: ITEM) = collapse(listOf(item))
    fun collapse(items: List<ITEM>) = expandOrCollapse(false, items)

    @JvmName("collapseIndices")
    fun collapse(indices: List<Int>) = collapse(adapter.getItemsForIndices(indices))
    fun collapseAll() = collapse(adapter.getDxAdapterItems())
    //endregion

    //region general
    fun getAllExpandedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemExpandable && it.isExpanded }

    //the returned list should NOT contain -1, because getAllExpandableItems()
    //only returns items that are already in the adapter
    fun getAllExpandedIndices() =
        adapter.getIndexList(getAllExpandedItems(), true)

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

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract val expandableView: View
    }
}