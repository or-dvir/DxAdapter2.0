package com.hotmail.or_dvir.dxadapter2

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

            indices = getIndexList(mixedItems)
            assertEquals(1, indices.filter { it == -1 }.size)

            indices = getIndexList(mixedItems, true)
            assertEquals(0, indices.filter { it == -1 }.size)
            //endregion

            //region items

            //all indices valid
            indices = listOf(0, 1, 2)
            val expectedItems = indices.map { mItems[it] }
            assertTrue(getItemsForIndices(indices).containsAll(expectedItems))

            //all indices invalid
            indices = listOf(-1, 1000000)
            assertTrue(getItemsForIndices(indices).isEmpty())

            //mixed indices
            indices = listOf(0, 1, -1)
            assertEquals(2, getItemsForIndices(indices).size)
            //endregion
        }
    }
}