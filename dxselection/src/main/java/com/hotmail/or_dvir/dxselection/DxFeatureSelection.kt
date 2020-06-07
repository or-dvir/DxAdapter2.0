package com.hotmail.or_dvir.dxselection

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

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class DxFeatureSelection<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    clickFeature: DxFeatureClick<ITEM>,
    private val onItemSelectionChanged: OnItemSelectionChangedListener<ITEM>,
    private var onSelectionModeChanged: OnSelectionModeStateChanged
) : IDxBaseFeature, IDxClickListenerFeature {

    //todo add flag whether selection should be done with clicks or not
    // maybe the user only wants to select when clicking the user image for example...

    init {
        adapter.addFeature(clickFeature)
        clickFeature.clickListenerFeatures.add(this)
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
        //todo when documenting add a note that the listeners will only be invoked when an item is
        // manually selected/deselected (and not here).
        // any visual changes (like background) should be done in the user's adapter using the
        // IDxItemSelectable.isSelected field.
        // the onItemSelectionChanged listener are good to update the actionMode title for example.
        // the
    }

    override fun onBindViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_selection

    override val onItemClick: OnItemClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            //only trigger the selection actions if the item is selectable and
            //we are already in selection mode
            if (item is IDxItemSelectable && isInSelectionMode()) {
                //reverse the selection
                if (item.isSelected) {
                    deselect(adapterPosition)
                } else {
                    select(adapterPosition)
                }

                //NOTE:
                //do NOT trigger any listeners here.
                //all listeners are controlled in selectOrDeselect()
            }
        }

    override val onItemLongClick: OnItemLongClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            //only trigger the selection actions if the item is selectable and we are not
            //already in selection mode (first long click triggers selectionMode)
            if (item is IDxItemSelectable && !isInSelectionMode()) {
                select(adapterPosition)

                //NOTE:
                //do NOT trigger any listeners here.
                //all listeners are controlled in selectOrDeselect()
            }

            //it doesn't matter which value we return here
            true
        }

    private fun selectOrDeselect(shouldSelect: Boolean, items: List<ITEM>) {
        var tempPosition: Int
        val wasInSelectionModeBefore = isInSelectionMode()

        items.forEach {
            //only select/deselect if actually needed
            //to avoid triggering listener multiple times
            if (it is IDxItemSelectable && shouldSelect != it.isSelected) {
                it.isSelected = shouldSelect
                tempPosition = adapter.getIndex(it)

                if (tempPosition != -1) {
                    onItemSelectionChanged.invoke(tempPosition, it.isSelected, it)
                    adapter.notifyItemChanged(tempPosition)
                }
            }
        }

        val isInSelectionModeAfter = isInSelectionMode()

        if (wasInSelectionModeBefore && !isInSelectionModeAfter) {
            onSelectionModeChanged.invoke(false)
        } else if (!wasInSelectionModeBefore && isInSelectionModeAfter) {
            onSelectionModeChanged.invoke(true)
        }
    }

    //region select
    fun select(index: Int) = select(adapter.getItem(index))
    fun select(item: ITEM) = select(listOf(item))
    fun select(items: List<ITEM>) = selectOrDeselect(true, items)

    @JvmName("selectIndices")
    fun select(indices: List<Int>) = select(adapter.getItemsForIndices(indices))

    fun selectAll() = select(adapter.getDxAdapterItems())
    //endregion

    //region deselect
    fun deselect(index: Int) = deselect(adapter.getItem(index))
    fun deselect(item: ITEM) = deselect(listOf(item))
    fun deselect(items: List<ITEM>) = selectOrDeselect(false, items)

    @JvmName("deselectIndices")
    fun deselect(indices: List<Int>) = deselect(adapter.getItemsForIndices(indices))

    fun deselectAll() = deselect(adapter.getDxAdapterItems())
    //endregion

    //region general
    fun getAllSelectedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemSelectable && it.isSelected }

    //the returned list should NOT contain -1, because getAllSelectableItems()
    //only returns items that are already in the adapter
    fun getAllSelectedIndices() =
        adapter.getIndexList(getAllSelectedItems(), true)

    fun getNumSelectedItems() = getAllSelectedItems().size

    fun isInSelectionMode() = getAllSelectableItems().any { it.isSelected }

    //NOTE:
    //we must call this function every time and not use a global variable
    //because the adapter is dynamic and may change at anytime.
    //if we have a global variable, we will only ever access the initial list of items
    @VisibleForTesting
    fun getAllSelectableItems() =
        adapter.getDxAdapterItems().filterIsInstance<IDxItemSelectable>()
    //endregion
}