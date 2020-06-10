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
import org.jetbrains.annotations.TestOnly

/**
 * a feature adding selection functionality to your adapter
 *
 * note that the listeners in this class will NOT be triggered in [onCreateViewHolder] and
 * [onBindViewHolder]. They will only be triggered when an item is being manually selected
 * or deselected (by user interaction or by code).
 *
 * @param adapter your adapter
 * @param clickFeature the [DxFeatureClick] used by your adapter
 * @param defaultClickBehavior whether or not to use the default selection behavior:
 *
 * when no items are selected, long-clicking an item will select that item and start selection mode.
 * any subsequent regular clicks on any item will select/deselect the clicked item.
 *
 * @param onItemSelectionChanged a listener to be invoked when an item has been selected/deselected.
 *
 * note that visual changes to your item should be done in your adapter inside the
 * onBindViewHolder() function (you can use this listener for example to change the title, showing how many
 * items are currently selected).
 *
 * @param onSelectionModeChanged a listener to be invoked when selection mode has started or ended
 * (first item has been selected or last item has been deselected).
 *
 * note that visual changes to your item should be done in your adapter inside the
 * onBindViewHolder() function (you can use this listener for example to start/finish ActionMode)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
open class DxFeatureSelection<ITEM : IDxBaseItem>(
    private val adapter: DxAdapter<ITEM, *>,
    clickFeature: DxFeatureClick<ITEM>,
    private var defaultClickBehavior: Boolean,
    private val onItemSelectionChanged: OnItemSelectionChangedListener<ITEM>,
    private var onSelectionModeChanged: OnSelectionModeStateChanged
) : IDxBaseFeature, IDxClickListenerFeature {

    init {
        adapter.addFeature(clickFeature)
        clickFeature.clickListenerFeatures.add(this)
    }

    @TestOnly
    fun setDefaultClickBehavior(defaultBehavior: Boolean) {
        defaultClickBehavior = defaultBehavior
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
        //do nothing
    }

    override fun getFeatureId() = R.id.feature_selection

    override val onItemClick: OnItemClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            if (defaultClickBehavior) {
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
        }

    override val onItemLongClick: OnItemLongClickListener<IDxBaseItem> =
        { view, adapterPosition, item ->
            if (defaultClickBehavior) {
                //only trigger the selection actions if the item is selectable and we are not
                //already in selection mode (first long click triggers selectionMode)
                if (item is IDxItemSelectable && !isInSelectionMode()) {
                    select(adapterPosition)

                    //NOTE:
                    //do NOT trigger any listeners here.
                    //all listeners are controlled in selectOrDeselect()
                }
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
    /**
     * selects the item at the given index
     */
    fun select(index: Int) = select(adapter.getItem(index))

    /**
     * selects the given item
     */
    fun select(item: ITEM) = select(listOf(item))

    /**
     * selects all the given items
     */
    fun select(items: List<ITEM>) = selectOrDeselect(true, items)

    /**
     * selects all the items at the given indices.
     *
     * any out of bounds indices will be ignored
     */
    @JvmName("selectIndices")
    fun select(indices: List<Int>) = select(adapter.getItemsForIndices(indices))

    /**
     * selects all the items in the adapter
     */
    fun selectAll() = select(adapter.getDxAdapterItems())
    //endregion

    //region deselect
    /**
     * deselects the item at the given index
     */
    fun deselect(index: Int) = deselect(adapter.getItem(index))

    /**
     * deselects the given item
     */
    fun deselect(item: ITEM) = deselect(listOf(item))

    /**
     * deselects all the given items
     */
    fun deselect(items: List<ITEM>) = selectOrDeselect(false, items)

    /**
     * deselects all the items at the given indices.
     *
     * any out of bounds indices will be ignored
     */
    @JvmName("deselectIndices")
    fun deselect(indices: List<Int>) = deselect(adapter.getItemsForIndices(indices))

    /**
     * deselects all the items in the adapter
     */
    fun deselectAll() = deselect(adapter.getDxAdapterItems())
    //endregion

    //region general
    /**
     * returns all currently selected items
     */
    fun getAllSelectedItems() =
        adapter.getDxAdapterItems().filter { it is IDxItemSelectable && it.isSelected }

    /**
     * returns the indices of all currently selected items
     */
    //the returned list should NOT contain -1, because getAllSelectableItems()
    //only returns items that are already in the adapter
    fun getAllSelectedIndices() =
        adapter.getIndexList(getAllSelectedItems(), true)

    /**
     * returns the number of currently selected items
     */
    fun getNumSelectedItems() = getAllSelectedItems().size

    /**
     * return TRUE if there is at least one item in the adapter that is currently selected,
     * or FALSE if none of the items in the adapter are selected
     */
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