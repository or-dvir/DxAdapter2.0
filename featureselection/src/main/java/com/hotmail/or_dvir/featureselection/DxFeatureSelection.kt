package com.hotmail.or_dvir.featureselection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

class DxFeatureSelection<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    private var onSelectionChanged: OnItemSelectionChangedListener
) : IDxBaseFeature {

    //todo test the "helper functions" (e.g. select, deselect and all those) in UNIT TESTS

    //todo
    // behaviour is: first long click triggers selection, every subsequent click triggers
    //      selection/deselection
    // listener for first item selected
    // listener for last item deselected

    override fun onCreateViewHolder(
        adapter: DxAdapter<*, *>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        TODO()
    }

    override fun getFeatureId() = R.id.feature_selection

    private fun selectOrDeselect(shouldSelect: Boolean, items: List<ITEM>) {
        var tempPosition: Int
        items.forEach {
            //only select/deselect if actually needed
            //to avoid triggering listener multiple times
            if (it is IDxItemSelectable && shouldSelect != it.isSelected) {
                it.isSelected = shouldSelect
                tempPosition = adapter.getIndex(it)

                if (tempPosition != -1) {
                    onSelectionChanged.invoke(tempPosition, shouldSelect)
                    adapter.notifyItemChanged(tempPosition)
                }
            }
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
    fun getAllSelectedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemSelectable && it.isSelected }

    fun getNumSelectedItems() = getAllSelectedItems().size

    //the returned list should NOT contain -1, because getAllSelectableItems()
    //only returns items that are already in the adapter
    fun getAllSelectedIndices() = adapter.getIndexList(getAllSelectableItems() as List<ITEM>, true)

    fun isInSelectionMode() = getAllSelectableItems().any { it.isSelected }
    //endregion
}