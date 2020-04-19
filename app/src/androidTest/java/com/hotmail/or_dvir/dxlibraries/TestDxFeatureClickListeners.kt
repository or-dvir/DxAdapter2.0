package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.ClickableAdapter
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.NonClickableAdapter
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.ClickableItem
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.NonClickableItem
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClickListeners
import com.hotmail.or_dvir.featureclicklisteners.onItemClickListener
import com.hotmail.or_dvir.featureclicklisteners.onItemLongClickListener
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestDxFeatureClickListeners {

    private lateinit var mClickListener: onItemClickListener
    private lateinit var mLongClickListener: onItemLongClickListener
    private lateinit var mClickFunctionality: DxFeatureClickListeners

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun before() {
        mClickListener = spyk({ _, _ -> })
        mLongClickListener = spyk({ _, _ -> true })

        mClickFunctionality = DxFeatureClickListeners().apply {
            onItemClick = mClickListener
            onItemLongClick = mLongClickListener
        }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    //todo for some reason when i use this line in my tests i get "unresolved reference"
    // onView(withId(R.id.activityMain_rv))

    @Test
    fun clickListenersTest_nonClickableItem() {

        val items = List(2) { index -> NonClickableItem("item $index") }
        val testAdapter =
            NonClickableAdapter(items).apply { addFunctionality(mClickFunctionality) }

        onActivity { it.apply { setAdapter(testAdapter) } }

        var clickedPosition = 0

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }


        //testing long click

        clickedPosition = 1

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //non of the listeners should be called
        verify(exactly = 0) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }
    }

    @Test
    fun clickListenersTest_clickableItem() {

        val items = List(2) { index -> ClickableItem("item $index") }
        val testAdapter =
            ClickableAdapter(items).apply { addFunctionality(mClickFunctionality) }

        onActivity { it.apply { setAdapter(testAdapter) } }

        var clickedPosition = 0

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //only regular click listener should be called
        verify(exactly = 1) { mClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { mLongClickListener.invoke(any(), clickedPosition) }


        //testing long click

        clickedPosition = 1

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //long click listener should be called
        verify(exactly = 1) { mLongClickListener.invoke(any(), clickedPosition) }

        //its impossible to reset the mockk.verify counter, so we must consider
        //previous invocations. also, we don't care about the function parameters
        verify(exactly = 1) { mClickListener.invoke(any(), any()) }
    }
}