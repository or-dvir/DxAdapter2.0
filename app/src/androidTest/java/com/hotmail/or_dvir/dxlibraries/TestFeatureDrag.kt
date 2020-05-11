package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterNonDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemNonDraggable
import io.mockk.spyk
import io.mockk.verify
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.absoluteValue

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
        mDragEventStart = spyk({ view, position ->
            Log.i("aaaaa", "drag start")
        })
        mDragEventEnd = spyk({ view, position -> })
        mOnItemMoved = spyk({ draggedView, draggedPosition, targetView, targetPosition ->
            Log.i("aaaaa", "$draggedPosition to $targetPosition")
        })

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
        onActivity { touchHelper.attachToRecyclerView(it.activityMain_rv) }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun dragTest_draggableItem_longClick() {
        val items = MutableList(100) { index -> ItemDraggable("item $index") }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }
        setupDragFeatureWithRecyclerView(adapter)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(positionFrom, LowLevelActions.pressAndHold())
        )

        verify(exactly = 1) { mDragEventStart.invoke(any(), positionFrom) }

        onView(withId(R.id.activityMain_rv)).perform(
            GeneralSwipeAction(
                Swipe.SLOW,
                RecyclerViewCoordinatesProvider(
                    positionFrom,
                    GeneralLocation.CENTER
                ),
                RecyclerViewCoordinatesProvider(
                    positionTo, GeneralLocation.CENTER
                ),
                Press.FINGER
            )
        )

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(positionFrom, LowLevelActions.release())
        )

        //using absoluteValue in case we change the positions in the future to be dragged
        //from bottom to top.
        //reducing 1 from the range because the listener is only called on the swapping of the items
        //and there is always 1 less swap than the number of items.
        //example:
        //  moving from position 1 to position 5 would have the following swaps
        //  (represented by an arrow): 1->2->3->4->5
        val range = 0 until (positionTo - positionFrom).absoluteValue - 1

        var newPositionFrom: Int

        for (i in range) {
            newPositionFrom = positionFrom + i
            verify(exactly = 1) {
                mOnItemMoved.invoke(
                    any(),
                    newPositionFrom,
                    any(),
                    newPositionFrom + 1 //dragging sequentially so should be 1 higher
                )
            }
        }


        //reducing 1 from positionTo because we are dragging to the CENTER of positionTo
        //and that is not enough for the items to be swapped (even BOTTOM_CENTER is not enough)
        val actualPositionToCheck = positionTo - 1
        verify(exactly = 1) { mDragEventEnd.invoke(any(), actualPositionToCheck) }

        scrollAndVerifyText(actualPositionToCheck, "item $positionFrom", adapter)
    }

    @Test
    fun dragTest_nonDraggableItem() {
        val items = MutableList(100) { index -> ItemNonDraggable("item $index") }
        val adapter = AdapterNonDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }
        setupDragFeatureWithRecyclerView(adapter)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(positionFrom, LowLevelActions.pressAndHold())
        )

        verify(exactly = 0) { mDragEventStart.invoke(any(), any()) }

        onView(withId(R.id.activityMain_rv)).perform(
            GeneralSwipeAction(
                Swipe.SLOW,
                RecyclerViewCoordinatesProvider(
                    positionFrom,
                    GeneralLocation.CENTER
                ),
                RecyclerViewCoordinatesProvider(
                    positionTo, GeneralLocation.CENTER
                ),
                Press.FINGER
            )
        )

        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(positionFrom, LowLevelActions.release())
        )

        verify(exactly = 0) { mOnItemMoved.invoke(any(), any(), any(), any()) }
        verify(exactly = 0) { mDragEventEnd.invoke(any(), any()) }

        scrollAndVerifyText(positionFrom, "item $positionFrom", adapter)
    }

    /**
     * a helper function to test that the adapter retains changes made to item in [positionToCheck]
     * by scrolling to the end and start of the recyclerView and then verifying that the item in
     * [positionToCheck] contains the desired [textToCheck]
     */
    private fun scrollAndVerifyText(
        positionToCheck: Int,
        textToCheck: String,
        adapter: DxAdapter<*>
    ) {
        onView(withId(R.id.activityMain_rv))
            //scroll to end
            .perform(scrollToPosition<ViewHolder>(adapter.getDxAdapterItems().size - 1))
            //scroll to start
            .perform(scrollToPosition<ViewHolder>(0))
            //scroll to position to make sure its visible
            .perform(scrollToPosition<ViewHolder>(positionToCheck))
            //check the text
            .check(matches(atPosition(positionToCheck, hasDescendant(withText(textToCheck)))))
    }


    //todo
    // non draggable item
    // all listeners
    // drag with handle
    // drag directions
    // isDragEnabled flag
    // dragging out of bounds
    // after each test add some up/down swipes and make sure the changes are still there
}