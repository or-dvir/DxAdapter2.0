package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemDraggable
import io.mockk.spyk
import org.junit.After
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
        mDragEventStart = spyk({ _, _ -> Log.i("aaaaa", "drag start") })
        mDragEventEnd = spyk({ _, _ -> })
        mOnItemMoved = spyk({ draggedView, draggedPosition, targetView, targetPosition -> })

        mDragFeature = DxFeatureDrag(
            mDragEventStart,
            mDragEventEnd,
            mOnItemMoved,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, //may be overridden later
            true
        )
    }

    @After
    fun after() {
        LowLevelActions.tearDown()
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun dragTest_draggableItem() {
        val items = MutableList(100) { index -> ItemDraggable("item $index") }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }


        //remember that this is a non draggable item!!!
        val dragFrom = 2
        val dragTo = 5

//        onView(withId(R.id.activityMain_rv)).perform(
//            actionOnItemAtPosition<ViewHolder>(dragFrom, ViewActions.longClick())
//        )

        //todo try to combine something to make drag and drop...

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(dragFrom, LowLevelActions.pressAndHold())
        )

        onView(withId(R.id.activityMain_rv)).perform(
            GeneralSwipeAction(
                Swipe.SLOW,
                RecyclerViewCoordinatesProvider(
                    dragFrom,
                    //drag from the text view...
                    ChildViewCoordinatesProvider(R.id.listItem_tv, GeneralLocation.CENTER)
                ),
                RecyclerViewCoordinatesProvider(
                    dragTo, GeneralLocation.CENTER
                    //CODE FROM STACK OVERFLOW
//                    if (dragFrom < dragTo) {
//                        CustomGeneralLocation.UNDER_RIGHT
//                    } else {
//                        CustomGeneralLocation.ABOVE_RIGHT
//                    }
                ),
                Press.FINGER
            )
        )
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