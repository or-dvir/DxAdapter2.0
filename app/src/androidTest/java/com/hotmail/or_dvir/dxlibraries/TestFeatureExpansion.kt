package com.hotmail.or_dvir.dxlibraries

import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import com.hotmail.or_dvir.dxexpansion.IDxItemExpandable
import com.hotmail.or_dvir.dxexpansion.OnItemExpansionStateChangedListener
import com.hotmail.or_dvir.dxlibraries.expandable.ItemExpandable
import com.hotmail.or_dvir.dxlibraries.expandable.ItemNonExpandable
import com.hotmail.or_dvir.dxlibraries.stickyheader.AdapterExpandableMix
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestFeatureExpansion : BaseTest() {

    //todo test expansion with expandOnClick = false

    private val mAdapter = AdapterExpandableMix(mutableListOf())
    private lateinit var mItemExpansion: OnItemExpansionStateChangedListener<BaseItem>
    private lateinit var mExpansionFeature: DxFeatureExpansion<BaseItem>

    @Suppress("RedundantLambdaArrow")
    @Before
    fun before() {
        mItemExpansion = spyk({ _, _, _ -> })

        val featureClick = DxFeatureClick<BaseItem>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        mExpansionFeature = DxFeatureExpansion(
            mAdapter,
            featureClick,
            true,
            mItemExpansion
        )

        //selection feature automatically adds the click feature to the adapter
        mAdapter.addFeature(mExpansionFeature)
        onActivity { it.apply { setAdapter(mAdapter) } }
    }

    @Test
    fun expandedItems_expandedIndices() {
        val expanded1 = ItemExpandable("expandable 1").apply { isExpanded = true }
        val expanded2 = ItemExpandable("expandable 2").apply { isExpanded = true }
        val expanded3 = ItemExpandable("expandable 3").apply { isExpanded = false }
        val nonExpandable = ItemNonExpandable("non-expandable")

        mAdapter.mItems.addAll(listOf(expanded1, expanded2, expanded3, nonExpandable))

        //NOTE:
        //for some reason, if i call any expansion functions from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mExpansionFeature.apply {
                //items
                val expandedItems = getAllExpandedItems()
                val expectedItems = listOf(expanded1, expanded2)
                assertEquals(2, expandedItems.size)
                assertTrue(expandedItems.containsAll(expectedItems))

                //indices
                val expandedIndices = getAllExpandedIndices()
                val expectedIndices = listOf(0, 1)
                assertEquals(2, expandedIndices.size)
                assertTrue(expandedIndices.containsAll(expectedIndices))
            }
        }
    }

    @Test
    fun expand() {
        val expanded1 = ItemExpandable("expandable 1").apply { isExpanded = false }
        val expanded2 = ItemExpandable("expandable 2").apply { isExpanded = false }
        val expanded3 = ItemExpandable("expandable 3").apply { isExpanded = false }
        val nonExpandable = ItemNonExpandable("non-expandable")

        mAdapter.mItems.addAll(listOf(expanded1, expanded2, expanded3, nonExpandable))

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mExpansionFeature.apply {
                //making sure it doesn't crash
                expand(nonExpandable)

                //single index
                expand(0)
                assertTrue(expanded1.isExpanded)
                collapseAll() //reset the items

                //index list
                expand(listOf(0, 1))
                assertTrue(expanded1.isExpanded)
                assertTrue(expanded2.isExpanded)
                collapseAll() //reset the items

                //single item
                expand(expanded2)
                assertTrue(expanded2.isExpanded)
                collapseAll() //reset the items

                //item list
                expand(listOf(expanded1, expanded2))
                assertTrue(expanded1.isExpanded)
                assertTrue(expanded2.isExpanded)
                collapseAll() //reset the items

                //expand all
                expandAll()
                assertEquals(3, getNumExpandedItems())
                getAllExpandableItems().forEach { assertTrue(it.isExpanded) }
            }
        }
    }

    @Test
    fun collapse() {
        val expanded1 = ItemExpandable("expandable 1").apply { isExpanded = true }
        val expanded2 = ItemExpandable("expandable 2").apply { isExpanded = true }
        val expanded3 = ItemExpandable("expandable 3").apply { isExpanded = true }
        val nonSelectable = ItemNonExpandable("non-expandable")

        mAdapter.mItems.addAll(listOf(expanded1, expanded2, expanded3, nonSelectable))

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mExpansionFeature.apply {
                //making sure it doesn't crash
                collapse(nonSelectable)

                //single index
                collapse(0)
                assertFalse(expanded1.isExpanded)
                collapseAll() //reset the items

                //index list
                collapse(listOf(0, 1))
                assertFalse(expanded1.isExpanded)
                assertFalse(expanded2.isExpanded)
                collapseAll() //reset the items

                //single item
                collapse(expanded2)
                assertFalse(expanded2.isExpanded)
                collapseAll() //reset the items

                //item list
                collapse(listOf(expanded1, expanded2))
                assertFalse(expanded1.isExpanded)
                assertFalse(expanded2.isExpanded)
                collapseAll() //reset the items

                //deselect all
                collapseAll()
                assertEquals(0, getNumExpandedItems())
                getAllExpandableItems().forEach { assertFalse(it.isExpanded) }
            }
        }
    }

    @Test
    fun clickBehaviourAndListeners() {
        //start off with no items expanded
        val expandable1 = ItemExpandable("expandable 1").apply { isExpanded = false }
        val expandable2 = ItemExpandable("expandable 2").apply { isExpanded = false }
        val expandable3 = ItemExpandable("expandable 3").apply { isExpanded = false }
        val nonSelectable = ItemNonExpandable("non-expandable")

        mAdapter.mItems.addAll(listOf(expandable1, expandable2, expandable3, nonSelectable))

        mExpansionFeature.apply {
            var position = 0

            //cannot "reset" the verify calls counter, so keep our own counter
            var numCallsItemTrue = 0
            var numCallsItemFalse = 0

            //IMPORTANT NOTE!!!
            //when testing calls to mItemExpansion we will NOT
            //specify the item parameter, because it makes it complicated to track the number
            //of calls

            //region non-expandable item
            position = 3
            clickAtPosition(position)
            verify(exactly = numCallsItemTrue) { mItemExpansion.invoke(any(), any(), any()) }
            //endregion

            //region expanding item
            position = 0
            clickAtPosition(position)
            ++numCallsItemTrue
            verify(exactly = numCallsItemTrue) { mItemExpansion.invoke(position, true, any()) }
            assertTrue((mAdapter.getItem(position) as IDxItemExpandable).isExpanded)
            assertEquals(getNumExpandedItems(), 1)
            //endregion

            //region expanding another item
            position = 1
            clickAtPosition(position)
            ++numCallsItemTrue

            verify(exactly = 1) { mItemExpansion.invoke(position, true, any()) }
            assertTrue((mAdapter.getItem(position) as IDxItemExpandable).isExpanded)
            assertEquals(getNumExpandedItems(), 2)
            //endregion

            //region collapsing an item by clicking it
            //position did not change
            clickAtPosition(position)
            ++numCallsItemFalse

            verify(exactly = numCallsItemFalse) { mItemExpansion.invoke(position, false, any()) }
            assertFalse((mAdapter.getItem(position) as IDxItemExpandable).isExpanded)
            assertEquals(getNumExpandedItems(), 1)
            //endregion

            //region collapsing last item
            position = 0
            clickAtPosition(position)

            //not using numCallsItem because this is the first time the
            //method is called with these specific parameters
            verify(exactly = 1) { mItemExpansion.invoke(position, false, any()) }
            assertFalse((mAdapter.getItem(position) as IDxItemExpandable).isExpanded)
            assertEquals(getNumExpandedItems(), 0)
            //endregion
        }
    }

    @Test
    fun alreadyExpandedItemDoesNotTriggerListenerAgain() {
        val expandable1 = ItemExpandable("expandable 1").apply { isExpanded = true }
        mAdapter.mItems.add(expandable1)

        //NOTE:
        //for some reason, if i call any selection function from outside an onActivity{} block,
        //i get an exception saying im trying to access the ui from a non-ui thread
        onActivity {
            mExpansionFeature.apply {
                expand(expandable1)
                verify(exactly = 0) { mItemExpansion.invoke(any(), any(), any()) }
            }
        }
    }
}