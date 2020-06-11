package com.hotmail.or_dvir.dxlibraries.tests

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxclick.OnItemClickListener
import com.hotmail.or_dvir.dxclick.OnItemLongClickListener
import com.hotmail.or_dvir.dxlibraries.BaseAdapter
import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxlibraries.R
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterNonClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemNonClickable
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TestFeatureClick : BaseTest() {
    private lateinit var mClickListener: OnItemClickListener<BaseItem>
    private lateinit var mLongClickListener: OnItemLongClickListener<BaseItem>
    private lateinit var mClickFeature: DxFeatureClick<BaseItem>

    @Before
    fun before() {
        mClickListener = spyk({ _, _, _ -> })
        mLongClickListener = spyk({ _, _, _ -> true })
        mLongClickListener = spyk({ _, _, _ -> true })

        mClickFeature = DxFeatureClick(mClickListener, mLongClickListener)
    }

    @Test
    fun clickListenersTest_nonClickableItem() {
        val items = MutableList(2) { index -> ItemNonClickable("item $index") }
        val adapter = AdapterNonClickable(items).apply { addFeature(mClickFeature) }

        onActivity { it.apply { setAdapter(adapter) } }

        var clickedPosition = 0
        clickAtPosition(clickedPosition)

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), any(), any()) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), any(), any()) }


        //testing long click

        clickedPosition = 1

        onView(withId(R.id.activityBase_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), any(), any()) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), any(), any()) }
    }

    @Test
    fun clickListenersTest_clickableItem() {
        val items = MutableList(2) { index -> ItemClickable("item $index") }
        val adapter = AdapterClickable(items).apply { addFeature(mClickFeature) }

        onActivity { it.apply { setAdapter(adapter) } }

        var clickedPosition = 0
        var clickedItem = adapter.getItem(clickedPosition)

        clickAtPosition(clickedPosition)

        //only regular click listener should be called
        verify(exactly = 1) { mClickListener.invoke(any(), clickedPosition, clickedItem) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), any(), any()) }


        //testing long click

        clickedPosition = 1
        clickedItem = adapter.getItem(clickedPosition)

        onView(withId(R.id.activityBase_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //only long click listener should be called
        verify(exactly = 1) { mLongClickListener.invoke(any(), clickedPosition, clickedItem) }
        //its impossible to reset the mockk.verify counter, so we must consider
        //previous invocations. also, we don't care about the function parameters
        verify(exactly = 1) { mClickListener.invoke(any(), any(), any()) }
    }

    @Test
    fun clickListenersTest_clickable_and_nonClickable_items() {
        val items = mutableListOf(
            //THE ORDER IS IMPORTANT!!! DO NOT CHANGE IT!!!
            //THE ORDER IS IMPORTANT!!! DO NOT CHANGE IT!!!
            //THE ORDER IS IMPORTANT!!! DO NOT CHANGE IT!!!
            ItemNonClickable("non-clickable"),
            ItemClickable("clickable")
        )

        val adapter = BaseAdapter(items).apply { addFeature(mClickFeature) }
        onActivity { it.apply { setAdapter(adapter) } }

        //region non-clickable item
        var position = 0
        clickAtPosition(position)
        longClickAtPosition(position)

        //listeners should not be called
        verify(exactly = 0) { mClickListener.invoke(any(), any(), any()) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), any(), any()) }
        //endregion

        //region clickable item
        position = 1
        onView(withId(R.id.activityBase_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(position, click())
        ).perform(
            actionOnItemAtPosition<ViewHolder>(position, longClick())
        )

        val item = adapter.getItem(position)

        //listeners should be called
        verify(exactly = 1) { mClickListener.invoke(any(), position, item) }
        verify(exactly = 1) { mLongClickListener.invoke(any(), position, item) }
        //endregion
    }
}