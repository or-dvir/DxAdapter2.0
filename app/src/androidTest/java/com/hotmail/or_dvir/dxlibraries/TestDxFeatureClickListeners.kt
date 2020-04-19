package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClickListeners
import com.hotmail.or_dvir.featureclicklisteners.onItemClickListener
import com.hotmail.or_dvir.featureclicklisteners.onItemLongClickListener
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test

class TestDxFeatureClickListeners {

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    //todo for some reason when i use this line in my tests i get "unresolved reference"
    // onView(withId(R.id.activityMain_rv))

    @Test
    fun clickListenersTest() {

        //IMPORTANT NOTE!!!
        //the click listeners rely on the function onCreateViewHolder,
        //which in this test case should only be called once.
        //therefore we must first create the adapter, and only then add it to the activity.

        //todo add test for non-clickable item

        val clickListener: onItemClickListener<IDxBaseItem> = spyk({ _, _, _ -> })
        val longClickListener: onItemLongClickListener<IDxBaseItem> = spyk({ _, _, _ -> true })

        val clickFunctionality = DxFeatureClickListeners<IDxBaseItem>().apply {
            onItemClick = clickListener
            onItemLongClick = longClickListener
        }

        val items = List(2) { index -> MyItem("item $index") }
        val testAdapter = MyAdapter(items).apply {
            addFunctionality(clickFunctionality)
        }

        onActivity {
            it.apply {
                setAdapter(testAdapter)
            }
        }

        var clickedPosition = 0

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //only regular click listener should be called
        verify(exactly = 1) { clickListener.invoke(any(), clickedPosition, any()) }
        verify(exactly = 0) { longClickListener.invoke(any(), clickedPosition, any()) }


        //testing long click

        clickedPosition = 1

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //long click listener should be called
        verify(exactly = 1) { longClickListener.invoke(any(), clickedPosition, any()) }

        //its impossible to reset the mockk.verify counter, so we must consider
        //previous invocations. also, we don't care about the function parameters
        verify(exactly = 1) { clickListener.invoke(any(), any(), any()) }
    }
}