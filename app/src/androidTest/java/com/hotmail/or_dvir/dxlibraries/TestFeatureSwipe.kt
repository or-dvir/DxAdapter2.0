package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.ItemTouchHelper
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.OnItemSwipedListener
import com.hotmail.or_dvir.dxdragandswipe.OnSwipeEventListener
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxFeatureSwipe
import io.mockk.spyk
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Before

class TestFeatureSwipe : BaseTest() {

    private lateinit var mSwipeStart: OnSwipeEventListener
    private lateinit var mSwipeEnd: OnSwipeEventListener
    private lateinit var mItemSwiped: OnItemSwipedListener
    private lateinit var mSwipeFeature: DxFeatureSwipe

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    @Before
    fun before() {
        mSwipeStart = spyk({ view, position -> })
        mSwipeEnd = spyk({ view, position -> })
        mItemSwiped = spyk({ view, position, direction: Int -> })

        mSwipeFeature = DxFeatureSwipe(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, //may be overridden later
            mSwipeStart,
            mSwipeEnd,
            mItemSwiped
        )
    }

    @After
    fun after() {
        PressActions.tearDown()
    }

    private fun setupSwipeFeatureWithRecyclerView(adapter: DxAdapter<*>) {
        val touchCallback = DxItemTouchCallback(adapter).apply {
            swipeFeature = mSwipeFeature
        }

        onActivity { ItemTouchHelper(touchCallback).attachToRecyclerView(it.activityMain_rv) }
    }

    //todo
    // right direction
    // wrong direction
    // listeners (start/end/swiped)
    // non-swipeable item
    // flag isSwipeEnabled
    // swipeThreshold
    // swipeEscapeVelocity
    // swipeEscapeVelocityMultiplier
    // getSwipeBackgroundLeft
    // getSwipeBackgroundRight
}