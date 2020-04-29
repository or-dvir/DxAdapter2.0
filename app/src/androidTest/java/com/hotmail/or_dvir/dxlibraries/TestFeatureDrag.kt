package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxdragandswipe.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterNonDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemNonDraggable
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import io.mockk.spyk
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestFeatureDrag {

    private lateinit var mDragEventStart: onDragEventListener
    private lateinit var mDragEventEnd: onDragEventListener
    private lateinit var mOnItemMoved: onItemMovedListener
    private lateinit var mDragFeature: DxFeatureDrag

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun before() {
        mDragEventStart = spyk({ _, _ -> })
        mDragEventEnd = spyk({ _, _ -> })
        mOnItemMoved = spyk({ draggedView, draggedPosition, targetView, targetPosition -> })

        mDragFeature = DxFeatureDrag(
            mDragEventStart,
            mDragEventEnd,
            mOnItemMoved,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN //may be overridden later
        )
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun dragTest_nonDraggableItem() {
        val items = MutableList(2) { index -> ItemNonDraggable("item $index") }
        val adapter = AdapterNonDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }

        val draggedPosition = 0

        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            actionOnItemAtPosition<ViewHolder>(draggedPosition, ViewActions.longClick())
        )

        //todo possible way to test dragging:
        // https://stackoverflow.com/questions/54804321/how-to-write-test-case-for-recyclerview-drag-and-drop-using-espress


    }

    //todo
    // non draggable item
    // all listeners
    // fast dragging
    // slow dragging
    // drag with handle
    // drag directions
    // drag with long click
    // isDragEnabled flag
    // dragging out of bounds
    // after each test add some up/down swipes and make sure the changes are still there


}