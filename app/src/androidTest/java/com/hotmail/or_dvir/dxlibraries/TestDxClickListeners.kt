package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxadapterclicklisteners.DxClickListeners
import com.hotmail.or_dvir.dxadapterclicklisteners.onItemClickListener
import com.hotmail.or_dvir.dxadapterclicklisteners.onItemLongClickListener
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import io.mockk.spyk
import io.mockk.verify
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestDxClickListeners {

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Before
    fun before() {
        setListForActivity(2)
    }

    //todo for some reason when i use this line in my tests i get "unresolved reference"
    // onView(withId(R.id.activityMain_rv))

    private fun setListForActivity(listSize: Int) {
        onActivity {
            it.mAdapter.items = List(listSize) { index -> MyItem("item $index") }
        }
    }

    @Test
    fun clickListenersTest() {
        val clickListener: onItemClickListener = spyk({ _, _ ->

            Log.i("aaaaa", "click")
        })
        val longClickListener: onItemLongClickListener = spyk({ _, _ -> true })

        val clickFunctionality = DxClickListeners().apply {
            onItemClick = clickListener
            onItemLongClick = longClickListener
        }

        problem
        the adapter is created, and the functionality is only added after the views
        have been created.
            so when onCreateViewHolder is called, the list of functionalities is empty!!!

        onActivity {
            it.apply {
                mAdapter.addFunctionality(clickFunctionality)
            }
        }

        Log.i("aaaaa", "new list from test")
        setListForActivity(10)

        var clickedPosition = 0

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, click())
        )

        //only regular click listener should be called
        verify(exactly = 1) { clickListener.invoke(any(), clickedPosition) }
        verify(exactly = 0) { longClickListener.invoke(any(), clickedPosition) }


        //testing long click

        clickedPosition = 1

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(clickedPosition, longClick())
        )

        //only long click listener should be called
        //NOTE:
        //its impossible to reset the mockk.verify counter, so we must consider
        //previous invocations
        verify(exactly = 1) { longClickListener.invoke(any(), clickedPosition) }
        verify(exactly = 1) { clickListener.invoke(any(), clickedPosition) }
    }
}