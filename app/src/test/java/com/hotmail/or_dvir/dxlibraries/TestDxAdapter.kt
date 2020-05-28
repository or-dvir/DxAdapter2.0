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

        adapter.apply {
            //region indices
            mItems.add(newItemIndex, newItem)
            assertEquals(newItemIndex, getIndex(newItem))

            val nonExistingItem = BaseItem("non-existing item")
            assertEquals(-1, getIndex(nonExistingItem))
            //endregion

            //region items
            assertEquals(newItem, getItem(newItemIndex))

            var exception: Exception? = null
            val nonExistingIndex = -1

            try {
                getItem(nonExistingIndex)
            } catch (e: Exception) {
                exception = e
            } finally {
                assertNotNull(exception)
            }
            //endregion
        }
    }

    @Test
    fun testItemsLists() {
        val newItems = mutableListOf(
            BaseItem("new item 1"),
            BaseItem("new item 2"),
            BaseItem("new item 3")
        )

        adapter.apply {
            //region indices
            mItems.addAll(newItems)
            var indices = getIndexList(newItems)

            assertFalse(indices.contains(-1))
            assertTrue(
                getIndexList(newItems).containsAll(
                    listOf(
                        //indices are 0-based!
                        ORIGINAL_LIST_SIZE,
                        ORIGINAL_LIST_SIZE + 1,
                        ORIGINAL_LIST_SIZE + 2
                    )
                )
            )

            val nonExistingItems = mutableListOf(
                BaseItem("non-existing item 1"),
                BaseItem("non-existing item 2"),
                BaseItem("non-existing item 3")
            )

            indices = getIndexList(nonExistingItems)
            indices.forEach { assertEquals(-1, it) }

            indices = getIndexList(nonExistingItems, true)
            assertTrue(indices.isEmpty())


            val mixedItems = listOf(
                newItems[0],
                newItems[1],
                nonExistingItems[0]
            )

            indices = getIndexList(nonExistingItems)
            assertEquals(1, indices.filter { it == -1 }.size)

            indices = getIndexList(nonExistingItems, true)
            assertEquals(0, indices.filter { it == -1 }.size)
            //endregion

            i stopped here

            //region items
            //endregion
        }
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