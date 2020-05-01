package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterNonClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemNonClickable
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClickListeners
import com.hotmail.or_dvir.featureclicklisteners.onItemClickListener
import com.hotmail.or_dvir.featureclicklisteners.onItemLongClickListener
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestFeatureClickListeners {

    private lateinit var mClickListener: onItemClickListener
    private lateinit var mLongClickListener: onItemLongClickListener
    private lateinit var mClickFeature: DxFeatureClickListeners

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun before() {
        mClickListener = spyk({ _, _ -> })
        mLongClickListener = spyk({ _, _ -> true })

        mClickFeature = DxFeatureClickListeners().apply {
            onItemClick = mClickListener
            onItemLongClick = mLongClickListener
        }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun clickListenersTest_nonClickableItem() {
        val items = MutableList(2) { index -> ItemNonClickable("item $index") }
        val adapter = AdapterNonClickable(items).apply { addFeature(mClickFeature) }

        onActivity { it.apply { setAdapter(adapter) } }

        var clickedPosition = 0

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }


        //testing long click

        clickedPosition = 1

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }
    }

    @Test
    fun clickListenersTest_clickableItem() {
        val items = MutableList(2) { index -> ItemClickable("item $index") }
        val adapter = AdapterClickable(items).apply { addFeature(mClickFeature) }

        onActivity { it.apply { setAdapter(adapter) } }

        var clickedPosition = 0

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //only regular click listener should be called
        verify(exactly = 1) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }


        //testing long click

        clickedPosition = 1

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //long click listener should be called
        verify(exactly = 1) { mLongClickListener.invoke(any(), clickedPosition) }

        //its impossible to reset the mockk.verify counter, so we must consider
        //previous invocations. also, we don't care about the function parameters
        verify(exactly = 1) { mClickListener.invoke(any(), any()) }
    }
}