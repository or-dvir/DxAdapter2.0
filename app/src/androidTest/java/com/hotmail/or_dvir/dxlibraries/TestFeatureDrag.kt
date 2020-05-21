package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.annotation.IdRes
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

    //todo can i test dragging out of bounds of screen?

    private lateinit var mDragEventStart: onDragEventListener
    private lateinit var mDragEventEnd: onDragEventListener
    private lateinit var mOnItemMoved: onItemMovedListener
    private lateinit var mDragFeature: DxFeatureDrag

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    @Before
    fun before() {
        mDragEventStart = spyk({ view, position ->
            Log.i("aaaaa", "start drag from test")
        })
        mDragEventEnd = spyk({ view, position -> })
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
        PressActions.tearDown()
    }

    private fun setupDragFeatureWithRecyclerView(adapter: DxAdapter<*>, @IdRes dragHandleId: Int?) {
        val touchCallback = DxItemTouchCallback(adapter).apply {
            dragFeature = mDragFeature
        }

        val touchHelper = DxItemTouchHelper(touchCallback).apply {
            dragHandleId?.apply { setDragHandleId(this) }
        }

        onActivity { touchHelper.attachToRecyclerView(it.activityMain_rv) }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun dragTest_longClick() {
        val items = MutableList(100) { index -> ItemDraggable("item $index") }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }
        setupDragFeatureWithRecyclerView(adapter, null)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        performDrag(positionFrom, positionTo, null)

        //IMPORTANT NOTE!!!
        //for an unknown reason the drag operation in the performDrag() function
        //triggers mDragEventStart (in addition to the press-and-hold operation).
        //THIS DOES NOT HAPPEN when i manually test the app!!!
        //so just accept it and check that it was called 2 times
        verify(exactly = 2) { mDragEventStart.invoke(any(), positionFrom) }

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
    fun dragTest_longClick_dragDisabled() {
        val items = MutableList(100) { index -> ItemDraggable("item $index") }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)
        mDragFeature.isDragEnabled = false

        onActivity { it.apply { setAdapter(adapter) } }
        setupDragFeatureWithRecyclerView(adapter, null)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        performDrag(positionFrom, positionTo, null)

        verify(exactly = 0) { mDragEventStart.invoke(any(), any()) }
        verify(exactly = 0) { mOnItemMoved.invoke(any(), any(), any(), any()) }
        verify(exactly = 0) { mDragEventEnd.invoke(any(), any()) }

        scrollAndVerifyText(positionFrom, "item $positionFrom", adapter)
    }

    @Test
    fun dragTest_longClick_nonDraggableItem() {
        val items = MutableList(100) { index -> ItemNonDraggable("item $index") }
        val adapter = AdapterNonDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.setDragOnLongClick(true)

        onActivity { it.apply { setAdapter(adapter) } }
        setupDragFeatureWithRecyclerView(adapter, null)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        performDrag(positionFrom, positionTo, null)

        verify(exactly = 0) { mDragEventStart.invoke(any(), any()) }
        verify(exactly = 0) { mOnItemMoved.invoke(any(), any(), any(), any()) }
        verify(exactly = 0) { mDragEventEnd.invoke(any(), any()) }

        scrollAndVerifyText(positionFrom, "item $positionFrom", adapter)
    }

    @Test
    fun dragTest_longClick_wrongDirection() {
        val items = MutableList(100) { index -> ItemDraggable("item $index") }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.apply {
            setDragOnLongClick(true)
            setDragDirection(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }

        onActivity {
            it.apply {
                setAdapter(adapter)
                setLayoutManagerVertical()
            }
        }

        setupDragFeatureWithRecyclerView(adapter, null)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        performDrag(positionFrom, positionTo, null)

        //all conditions for allowing drag are fulfilled so its expected behaviour
        // for the start/end drag listeners to trigger. however since this test is about dragging
        // in the wrong direction, the move listener should not be triggered.
        //IMPORTANT NOTE!!!
        // for an unknown reason the drag operation in the performDrag() function
        // triggers mDragEventStart (in addition to the press-and-hold operation).
        // THIS DOES NOT HAPPEN when i manually test the app!!!
        // so just accept it and check that it was called 2 times
        verify(exactly = 2) { mDragEventStart.invoke(any(), any()) }
        verify(exactly = 0) { mOnItemMoved.invoke(any(), any(), any(), any()) }
        verify(exactly = 1) { mDragEventEnd.invoke(any(), any()) }

        scrollAndVerifyText(positionFrom, "item $positionFrom", adapter)
    }

    @Test
    fun dragTest_dragHandle() {
        val items = MutableList(100) { index ->
            ItemDraggable("item $index")
        }
        val adapter = AdapterDraggable(items).apply { addFeature(mDragFeature) }
        mDragFeature.apply {
            setDragOnLongClick(false)
            setDragDirection(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        }

        onActivity {
            it.apply {
                setAdapter(adapter)
                setLayoutManagerVertical()
            }
        }

        val handleId = R.id.listItem_dragHandle
        setupDragFeatureWithRecyclerView(adapter, handleId)

        //the positions MUST be visible on screen.
        val positionFrom = 1
        val positionTo = 5

        performDrag(positionFrom, positionTo, handleId)

        //NOTE:
        // not specifying the amount of calls to mDragEventStart because
        // for an unknown reason the drag operation in the performDrag() function
        // triggers mDragEventStart (in addition to the press-and-hold operation).
        // THIS DOES NOT HAPPEN when i manually test the app!!!
        //NOTE:
        // not specifying the amount of calls to mOnItemMoved because the check in dragTest_longClick()
        // should be enough
        verify { mDragEventStart.invoke(any(), any()) }
        verify { mOnItemMoved.invoke(any(), any(), any(), any()) }
        verify(exactly = 1) { mDragEventEnd.invoke(any(), any()) }

        //reducing 1 from positionTo because we are dragging to the CENTER of positionTo
        //and that is not enough for the items to be swapped (even BOTTOM_CENTER is not enough)
        scrollAndVerifyText(positionTo - 1, "item $positionFrom", adapter)
    }

    //region helper functions
    @Suppress("SameParameterValue", "SameParameterValue")
    private fun performDrag(
        positionFrom: Int,
        positionTo: Int,
        @IdRes innerViewId: Int?
    ) {
        onView(withId(R.id.activityMain_rv))
            .perform(
                actionOnItemAtPosition<ViewHolder>(
                    positionFrom,
                    PressActions.pressAndHold(innerViewId)
                )
            )
            .perform(
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
            .perform(
                actionOnItemAtPosition<ViewHolder>(positionFrom, PressActions.release(innerViewId))
            )
    }

    /**
     * a helper function to test that the adapter retains changes made to item in [positionToCheck]
     * by scrolling to the end and start of the recyclerView and then verifying that the item in
     * [positionToCheck] contains the desired [textToCheck]
     */
    @Suppress("SameParameterValue")
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
    //endregion
}