package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxlibraries.selectable.ItemNonSelectable
import com.hotmail.or_dvir.dxlibraries.selectable.ItemSelectable
import com.hotmail.or_dvir.dxselection.DxFeatureSelection
import com.hotmail.or_dvir.dxselection.OnItemSelectionChangedListener
import com.hotmail.or_dvir.dxselection.OnSelectionModeStateChanged
import io.mockk.spyk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestFeatureSelection {
    private val mAdapter = BaseAdapter(mutableListOf())
    private lateinit var mItemSelectionListener: OnItemSelectionChangedListener<BaseItem>
    private lateinit var mSelectionModeListener: OnSelectionModeStateChanged

    private lateinit var mSelectionFeature: DxFeatureSelection<BaseItem>

    @Before
    fun before() {
        mItemSelectionListener = spyk({ _, _, _ -> })
        mSelectionModeListener = spyk({ })

        val featureClick = DxFeatureClick<BaseItem>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        mSelectionFeature = DxFeatureSelection(
            mAdapter,
            featureClick,
            mItemSelectionListener,
            mSelectionModeListener
        )

        //selection feature automatically adds the click feature to the adapter
        mAdapter.addFeature(mSelectionFeature)
    }

    @Test
    fun selectedItems_selectedIndices_selectionMode() {
        val selectable1 = ItemSelectable("").apply { isSelected = true }
        val selectable2 = ItemSelectable("").apply { isSelected = true }
        val selectable3 = ItemSelectable("").apply { isSelected = false }
        val nonSelectable = ItemNonSelectable("")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        mSelectionFeature.apply {
            //items
            val selectedItems = getAllSelectedItems()
            val expectedItems = listOf(selectable1, selectable2)
            assertEquals(2, selectedItems.size)
            assertTrue(selectedItems.containsAll(expectedItems))

            //indices
            val selectedIndices = getAllSelectedIndices()
            val expectedIndices = listOf(0, 1)
            assertEquals(2, selectedIndices.size)
            assertTrue(selectedIndices.containsAll(expectedIndices))

            //selection mode
            assertTrue(isInSelectionMode())
            deselectAll()
            assertFalse(isInSelectionMode())
        }
    }

    @Test
    fun selection() {
        val selectable1 = ItemSelectable("").apply { isSelected = false }
        val selectable2 = ItemSelectable("").apply { isSelected = false }
        val selectable3 = ItemSelectable("").apply { isSelected = false }
        val nonSelectable = ItemNonSelectable("")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        mSelectionFeature.apply {
            //making sure it doesn't crash
            select(nonSelectable)

            //single index
            select(0)
            assertTrue(selectable1.isSelected)
            deselectAll() //reset the items

            //index list
            select(listOf(0, 1))
            assertTrue(selectable1.isSelected)
            assertTrue(selectable2.isSelected)
            deselectAll() //reset the items

            //single item
            select(selectable2)
            assertTrue(selectable2.isSelected)
            deselectAll() //reset the items

            //item list
            select(listOf(selectable1, selectable2))
            assertTrue(selectable1.isSelected)
            assertTrue(selectable2.isSelected)
            deselectAll() //reset the items

            //select all
            selectAll()
            getAllSelectableItems().forEach { assertTrue(it.isSelected) }
        }
    }

    //todo
    // deselect index
    // deselect invalid index
    // deselect list of indices
    // deselect item
    // deselect item list
    // deselect all
    // first long click triggers selection mode, next long click does nothing
    // next regular click selects the item
    // next regular click deselects the item
    // already selected item does not get selected again
    // onItemSelectionChanged
    // onSelectionModeChanged

}