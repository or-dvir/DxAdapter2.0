package com.hotmail.or_dvir.dxlibraries

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemDraggable
import io.mockk.spyk
import io.mockk.verify
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestFeatureDrag {

    private lateinit var mDragEventStart: onDragEventListener
    private lateinit var mDragEventEnd: onDragEventListener
    private lateinit var mOnItemMoved: onItemMovedListener
    private lateinit var mDragFeature: DxFeatureDrag

    private lateinit var mItemTouchHelper: DxItemTouchHelper
    private lateinit var mItemTouchCallback: DxItemTouchHelper

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun before() {
        mDragEventStart = spyk({ view, _->
            Log.i("aaaaa", "drag start")
            view.setBackgroundColor(Color.RED)
        })
        mDragEventEnd = spyk({ _, _ -> })
        mOnItemMoved = spyk({ draggedView, draggedPosition, targetView, targetPosition -> })

        mDragFeature = DxFeatureDrag(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, //may be overridden later
            mDragEventStart,
            mDragEventEnd,
            mOnItemMoved,
            true
        )
    }

    @After
    fun after() {
        LowLevelActions.tearDown()
    }

    private fun setupDragFeatureWithRecyclerView(adapter: DxAdapter<*>) {
        val touchCallback = DxItemTouchCallback(adapter).apply {
            dragFeature = mDragFeature
        }

        val touchHelper = DxItemTouchHelper(touchCallback)

        onActivity {
            it.apply {
                touchHelper.attachToRecyclerView(activityMain_rv)
            }
        }
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
        val positionFrom = 2
        val positionTo = 5

//        onView(withId(R.id.activityMain_rv)).perform(
//            actionOnItemAtPosition<ViewHolder>(positionFrom, ViewActions.longClick())
//        )

        check your setup! the initial problem was that the callback was not attached
        to the recycler view!

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(positionFrom, LowLevelActions.pressAndHold())
        )

        verify(exactly = 1) { mDragEventStart.invoke(any(), positionFrom) }


//        onView(withId(R.id.activityMain_rv)).perform(
//            GeneralSwipeAction(
//                Swipe.SLOW,
//                RecyclerViewCoordinatesProvider(
//                    positionFrom,
//                    //drag from the text view...
//                    ChildViewCoordinatesProvider(R.id.listItem_tv, GeneralLocation.CENTER)
//                ),
//                RecyclerViewCoordinatesProvider(
//                    positionTo, GeneralLocation.CENTER
//                    //CODE FROM STACK OVERFLOW
////                    if (dragFrom < dragTo) {
////                        CustomGeneralLocation.UNDER_RIGHT
////                    } else {
////                        CustomGeneralLocation.ABOVE_RIGHT
////                    }
//                ),
//                Press.FINGER
//            )
//        )
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