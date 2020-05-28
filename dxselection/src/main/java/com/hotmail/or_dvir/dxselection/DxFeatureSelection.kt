package com.hotmail.or_dvir.dxselection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxclick.IDxClickListenerFeature
import com.hotmail.or_dvir.dxclick.OnItemClickListener
import com.hotmail.or_dvir.dxclick.OnItemLongClickListener

@Suppress("MemberVisibilityCanBePrivate", "unused")
class DxFeatureSelection<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    clickFeature: DxFeatureClick<ITEM>,
    private val onItemSelectionChanged: OnItemSelectionChangedListener<ITEM>,
    private var onSelectionModeChanged: OnSelectionModeStateChanged
) : IDxBaseFeature, IDxClickListenerFeature {

    write tests!!!
    "helper functions" (e.g. select, deselect etc) should be UNIT tests
    do i even need instrumentation tests here????

    init {
        clickFeature.clickListenerFeatures.add(this)
    }

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        //do nothing
        //todo when documenting add a note that the listeners will only be invoked when an item is
        // manually selected/deselected and not here for example.
        // any visual changes (like background) should be done in the user's adapter
        // using the IDxItemSelectable.isSelected field
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
                    onItemSelectionChanged.invoke(tempPosition, shouldSelect, it)
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

    //NOTE:
    //we must call this function every time and not use a global variable
    //because the adapter is dynamic and may change at anytime.
    //if we have a global variable, we will only ever access the initial list of items
    private fun getAllSelectableItems() =
        adapter.getDxAdapterItems().filterIsInstance<IDxItemSelectable>() //as List<ITEM>

    //region select
    fun select(index: Int) = select(adapter.getItem(index))
    fun select(item: ITEM) = select(listOf(item))
    fun select(items: List<ITEM>) = selectOrDeselect(true, items)

    @JvmName("selectIndices")
    fun select(indices: List<Int>) = select(adapter.getItemsForIndices(indices))

    fun selectAll() = select(adapter.getDxAdapterItems())
    //endregion

    //region select
    fun deselect(index: Int) = deselect(adapter.getItem(index))
    fun deselect(item: ITEM) = deselect(listOf(item))
    fun deselect(items: List<ITEM>) = selectOrDeselect(false, items)

    @JvmName("deselectIndices")
    fun deselect(indices: List<Int>) = deselect(adapter.getItemsForIndices(indices))

    fun deselectAll() = deselect(adapter.getDxAdapterItems())
    //endregion

    //region general
    fun getAllCurrentlySelectedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemSelectable && it.isSelected }

    fun getNumCurrentlySelectedItems() = getAllCurrentlySelectedItems().size

    //the returned list should NOT contain -1, because getAllSelectableItems()
    //only returns items that are already in the adapter
    fun getAllCurrentlySelectedIndices() =
        adapter.getIndexList(getAllSelectableItems() as List<ITEM>, true)

    fun isInSelectionMode() = getAllSelectableItems().any { it.isSelected }
    //endregion
}