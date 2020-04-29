package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxdragandswipe.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.onDragEventListener
import com.hotmail.or_dvir.dxdragandswipe.onItemMovedListener
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule

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
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, //may be overridden later
            true //may be overridden later
        )
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    //todo
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