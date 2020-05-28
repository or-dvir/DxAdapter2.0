package com.hotmail.or_dvir.dxlibraries

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestDxAdapter {
    companion object {
        private const val ORIGINAL_LIST_SIZE = 100
    }

    private lateinit var adapter: BaseAdapter

    @Before
    fun before() {
        val items = MutableList(ORIGINAL_LIST_SIZE) { index ->
            BaseItem("item $index")
        }

        adapter = BaseAdapter(items)
    }

    @Test
    fun testSingleItem() {
        val newItem = BaseItem("new item")
        val newItemIndex = 30

        //region indices
        adapter.mItems.add(newItemIndex, newItem)
        assertEquals(newItemIndex, adapter.getIndex(newItem))

        val nonExistingItem = BaseItem("non-existing item")
        assertEquals(-1, adapter.getIndex(nonExistingItem))
        //endregion

        //region items
        assertEquals(newItem, adapter.getItem(newItemIndex))

        var exception: Exception? = null
        val nonExistingIndex = -1

        try {
            adapter.getItem(nonExistingIndex)
        } catch (e: Exception) {
            exception = e
        } finally {
            assertNotNull(exception)
        }
        //endregion
    }

    @Test
    fun testItemsLists() {
        val newItems = mutableListOf(
            BaseItem("new item 1"),
            BaseItem("new item 2"),
            BaseItem("new item 3")
        )

        adapter.mItems.addAll(newItems)
        i stopped here

        //region indices


        //todo
        // all indices contained in adapter
        // none of the indices are contained
        // some are contained (returned list includes -1)
        // some are contained (returned list does not contain -1)
        //endregion

        //region items
        //endregion
    }


    /**
     * returns a list of indices for the given [items].
     *
     * note that the returned list may contain -1 as it uses [List.indexOf].
     * To avoid this, set [filterNonExistingItems] to true
     */
    fun getIndexList(items: List<ITEM>, filterNonExistingItems: Boolean = false): List<Int> {
        var list = items.map { getIndex(it) }

        if (filterNonExistingItems) {
            list = list.filter { it != -1 }
        }

        return list
    }

    /**
     * returns a list of items at the given [indices].
     *
     * If an index in [indices] is out of bounds of the adapter, that index is ignored
     */
    fun getItemsForIndices(indices: List<Int>): List<ITEM> {
        val range = 0 until itemCount - 1
        val list = mutableListOf<ITEM>()

        indices.forEach {
            if (it in range) {
                list.add(getItem(it))
            }
        }

        return list
    }
}