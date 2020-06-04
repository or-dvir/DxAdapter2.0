package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxlibraries.selectable.ItemNonSelectable
import com.hotmail.or_dvir.dxlibraries.selectable.ItemSelectable
import com.hotmail.or_dvir.dxselection.DxFeatureSelection
import com.hotmail.or_dvir.dxselection.IDxItemSelectable
import com.hotmail.or_dvir.dxselection.OnItemSelectionChangedListener
import com.hotmail.or_dvir.dxselection.OnSelectionModeStateChanged
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestFeatureSelection : BaseTest() {
    private val mAdapter = BaseAdapter(mutableListOf())
    private lateinit var mItemSelection: OnItemSelectionChangedListener<BaseItem>
    private lateinit var mSelectionMode: OnSelectionModeStateChanged

    private lateinit var mSelectionFeature: DxFeatureSelection<BaseItem>

    @Suppress("RedundantLambdaArrow")
    @Before
    fun before() {
        mItemSelection = spyk({ _, _, _ -> })
        mSelectionMode = spyk({ _ -> })

        val featureClick = DxFeatureClick<BaseItem>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        mSelectionFeature = DxFeatureSelection(
            mAdapter,
            featureClick,
            mItemSelection,
            mSelectionMode
        )

        //selection feature automatically adds the click feature to the adapter
        mAdapter.addFeature(mSelectionFeature)
        onActivity { it.apply { setAdapter(mAdapter) } }
    }

    @Test
    fun selectedItems_selectedIndices_selectionMode() {
        val selectable1 = ItemSelectable("selectable 1").apply { isSelected = true }
        val selectable2 = ItemSelectable("selectable 2").apply { isSelected = true }
        val selectable3 = ItemSelectable("selectable 3").apply { isSelected = false }
        val nonSelectable = ItemNonSelectable("non-selectable")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
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
    }

    @Test
    fun selection() {
        val selectable1 = ItemSelectable("selectable 1").apply { isSelected = false }
        val selectable2 = ItemSelectable("selectable 2").apply { isSelected = false }
        val selectable3 = ItemSelectable("selectable 3").apply { isSelected = false }
        val nonSelectable = ItemNonSelectable("non-selectable")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
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
                assertEquals(3, getNumSelectedItems())
                getAllSelectableItems().forEach { assertTrue(it.isSelected) }
            }
        }
    }

    @Test
    fun deselection() {
        val selectable1 = ItemSelectable("selectable 1").apply { isSelected = true }
        val selectable2 = ItemSelectable("selectable 2").apply { isSelected = true }
        val selectable3 = ItemSelectable("selectable 3").apply { isSelected = true }
        val nonSelectable = ItemNonSelectable("non-selectable")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mSelectionFeature.apply {
                //making sure it doesn't crash
                deselect(nonSelectable)

                //single index
                deselect(0)
                assertFalse(selectable1.isSelected)
                selectAll() //reset the items

                //index list
                deselect(listOf(0, 1))
                assertFalse(selectable1.isSelected)
                assertFalse(selectable2.isSelected)
                selectAll() //reset the items

                //single item
                deselect(selectable2)
                assertFalse(selectable2.isSelected)
                selectAll() //reset the items

                //item list
                deselect(listOf(selectable1, selectable2))
                assertFalse(selectable1.isSelected)
                assertFalse(selectable2.isSelected)
                selectAll() //reset the items

                //deselect all
                deselectAll()
                assertEquals(0, getNumSelectedItems())
                getAllSelectableItems().forEach { assertFalse(it.isSelected) }
            }
        }
    }

    @Test
    fun clickBehaviourAndListeners() {
        //start off with no items selected
        val selectable1 = ItemSelectable("selectable 1").apply { isSelected = false }
        val selectable2 = ItemSelectable("selectable 2").apply { isSelected = false }
        val selectable3 = ItemSelectable("selectable 3").apply { isSelected = false }
        val nonSelectable = ItemNonSelectable("non-selectable")

        mAdapter.mItems.addAll(listOf(selectable1, selectable2, selectable3, nonSelectable))

        mSelectionFeature.apply {
            var position = 0

            //cannot "reset" the verify calls counter, so keep our own counter
            var numCallsSelectionMode = 0
            var numCallsItemTrue = 0
            var numCallsItemFalse = 0

            //IMPORTANT NOTE!!!
            //when testing calls to mItemSelection we will NOT
            //specify the item parameter, because it makes it complicated to track the number
            //of calls

            //region clicking item (nothing should happen)
            clickAtPosition(position)
            verify(exactly = numCallsItemTrue) { mItemSelection.invoke(any(), any(), any()) }
            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(any()) }
            //endregion

            //region long clicking non-selectable item (nothing should happen)
            position = 3
            longClickAtPosition(position)
            verify(exactly = numCallsItemTrue) { mItemSelection.invoke(any(), any(), any()) }
            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(any()) }
            //endregion

            //region long clicking first item
            position = 0
            longClickAtPosition(position)
            ++numCallsItemTrue
            ++numCallsSelectionMode
            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(true) }
            verify(exactly = numCallsItemTrue) {
                mItemSelection.invoke(
                    position,
                    true,
                    any()
                )
            }
            assertTrue((mAdapter.getItem(position) as IDxItemSelectable).isSelected)
            assertTrue(isInSelectionMode())
            assertEquals(getNumSelectedItems(), 1)
            //endregion

            //region long clicking another item (nothing should change)
            position = 1
            longClickAtPosition(position)

            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(any()) }
            verify(exactly = numCallsItemTrue) { mItemSelection.invoke(any(), any(), any()) }
            assertFalse((mAdapter.getItem(position) as IDxItemSelectable).isSelected)
            assertEquals(getNumSelectedItems(), 1)
            //endregion

            //region selecting another item by clicking it
            //position and itemToCheck have not changed
            clickAtPosition(position)
            ++numCallsItemTrue

            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(any()) }
            verify(exactly = 1) {
                mItemSelection.invoke(
                    position,
                    true,
                    any()
                )
            }
            assertTrue((mAdapter.getItem(position) as IDxItemSelectable).isSelected)
            assertEquals(getNumSelectedItems(), 2)
            //endregion

            //region deselecting item by clicking it
            //clickedPosition and itemToCheck have not changed
            clickAtPosition(position)
            ++numCallsItemFalse

            verify(exactly = numCallsSelectionMode) { mSelectionMode.invoke(any()) }
            verify(exactly = numCallsItemFalse) {
                mItemSelection.invoke(
                    position,
                    false,
                    any()
                )
            }
            assertFalse((mAdapter.getItem(position) as IDxItemSelectable).isSelected)
            assertTrue(isInSelectionMode())
            assertEquals(getNumSelectedItems(), 1)
            //endregion

            //region deselecting last item
            position = 0
            clickAtPosition(position)

            //not using numCallsSelectionMode and numCallsItem because this is the first time the
            //method is called with these specific parameters
            verify(exactly = 1) { mSelectionMode.invoke(false) }
            verify(exactly = 1) {
                mItemSelection.invoke(
                    position,
                    false,
                    any()
                )
            }
            assertFalse((mAdapter.getItem(position) as IDxItemSelectable).isSelected)
            assertFalse(isInSelectionMode())
            assertEquals(getNumSelectedItems(), 0)
            //endregion
        }
    }

    @Test
    fun alreadySelectedItemDoesNotTriggerListenerAgain() {
        val selectable1 = ItemSelectable("selectable 1").apply { isSelected = true }
        mAdapter.mItems.add(selectable1)

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mSelectionFeature.apply {
                select(selectable1)
                verify(exactly = 0) { mItemSelection.invoke(any(), any(), any()) }
            }
        }
    }
}