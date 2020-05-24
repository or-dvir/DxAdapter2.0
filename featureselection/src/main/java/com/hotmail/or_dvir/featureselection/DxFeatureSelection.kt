package com.hotmail.or_dvir.featureselection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature

class DxFeatureSelection(
    private val adapter: DxAdapter<*>,
    private var onSelectionChanged: OnItemSelectionChangedListener
) : IDxBaseFeature {

    //todo can the tests for this feature be unit tests????


    //todo
    // behaviour is: first long click triggers selection, every subsequent click triggers
    //      selection/deselection
    // isInSelectionMode(at least one item is selected)
    //      this can be used in click listeners to trigger or not trigger regular click action
    // getAllSelectedItems
    // numSelectedItems
    // getAllSelectedIndices
    // listener for first item selected
    // listener for last item deselected
    // manually selected item
    // manually selected list of item
    // manually selected index
    // manually selected list of indices
    // manually deselected item
    // manually deselected list of item
    // manually deselected index
    // manually deselected list of indices
    // select all items
    // deselect all items

    override fun onCreateViewHolder(
        adapter: DxAdapter<*>,
        itemView: View,
        holder: RecyclerView.ViewHolder
    ) {
        addasasdas
    }

    override fun getFeatureId() = R.id.feature_selection

    //NOTE:
    //we must call this function every time and not use a global variable
    //because the adapter is dynamic and may change at anytime.
    //if we have a global variable, we will only ever access the initial list of items
    fun getAllSelectableItems() =
        adapter.getDxAdapterItems().filterIsInstance<IDxItemSelectable>()

    private fun selectOrDeselect(shouldSelect: Boolean, items: List<IDxItemSelectable>) {
        var tempPosition: Int
        items.forEach {
            //only select/deselect if actually needed
            //to avoid triggering listener multiple times
            if (shouldSelect != it.isSelected) {
                it.isSelected = shouldSelect
                tempPosition = getIndexForItem(it)

                if (tempPosition != -1) {
                    onSelectionChanged.invoke(tempPosition, shouldSelect)
                    adapter.notifyItemChanged(tempPosition)
                }
            }
        }
    }

    /**
     * @see [List.indexOf]
     */
    private fun getIndexForItem(item: IDxItemSelectable) =
        adapter.getDxAdapterItems().indexOf(item)

    fun select(items: List<IDxItemSelectable>) =
        selectOrDeselect(true, items)

    fun deselect(items: List<IDxItemSelectable>) =
        selectOrDeselect(false, items)


    //todo
    // manually selected item
    // manually selected index
    // manually selected list of item
    // manually selected list of indices
    // manually deselected item
    // manually deselected index
    // manually deselected list of item
    // manually deselected list of indices
    // select all items
    // deselect all items
}
