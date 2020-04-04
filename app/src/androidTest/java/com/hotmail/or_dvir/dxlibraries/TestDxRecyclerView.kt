package com.hotmail.or_dvir.dxlibraries

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.EmptyListener
import io.mockk.*
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestDxRecyclerView {
    //    private val firstVisible = mockk<EmptyListener>()
//    private val firstInvisible = mockk<EmptyListener>()
    private var mFirstVisible: EmptyListener = {}
    private var mFirstInvisible: EmptyListener = {}

    //    private var lastVisible = mockk<EmptyListener>()
//    private var lastInvisible = mockk<EmptyListener>()
    private var mLastVisible: EmptyListener = {}
    private var mLastInvisible: EmptyListener = {}

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun setupIndividualListeners() {
        mFirstVisible = spyk(mFirstVisible)
        mFirstInvisible = spyk(mFirstInvisible)

        mLastVisible = spyk(mLastVisible)
        mLastInvisible = spyk(mLastInvisible)
    }

    @Before
    fun setupVisibilityListener() {
        onActivity {
            it.activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {
                onFirstItemVisible = mFirstVisible
                onFirstItemInvisible = mFirstInvisible

                onLastItemVisible = mLastVisible
                onLastItemInvisible = mLastInvisible
            }
        }
    }

    private fun setListForActivity(listSize: Int) {
        onActivity {
            it.mAdapter.items = List(listSize) { MyItem("name") }
        }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun visibilityListenerShortListTest() {
//        val firstVisible = mockk<EmptyListener>()
//        val firstInvisible = mockk<EmptyListener>()
//        every { firstVisible.invoke() } just runs
//        every { firstInvisible.invoke() } just runs
//
//        val lastVisible = mockk<EmptyListener>()
//        val lastInvisible = mockk<EmptyListener>()
//        every { lastVisible.invoke() } just runs
//        every { lastInvisible.invoke() } just runs
//
//        val listener = DxVisibilityListener().apply {
//            onFirstItemVisible = mFirstVisible
//            onFirstItemInvisible = mFirstInvisible
//
//            onLastItemVisible = mLastVisible
//            onLastItemInvisible = mLastInvisible
//        }

        //creating a short list so both first and last fit on the screen
        setListForActivity(2)

        //first and last item visibility listeners should be invoked
        verify(exactly = 1) { mFirstVisible.invoke() }
        verify(exactly = 1) { mLastVisible.invoke() }

        //first and last item invisibility listeners should NOT be invoked
        verify(exactly = 0) { mFirstInvisible.invoke() }
        verify(exactly = 0) { mLastInvisible.invoke() }


//        val listener2 = DxVisibilityListener().apply {
//            onFirstItemVisible = mFirstVisible
//            onFirstItemInvisible = mFirstInvisible
//
//            onLastItemVisible = mLastVisible
//            onLastItemInvisible = mLastInvisible
//        }
//
//        //creating a large list that should not fit on the screen
//        onActivity {
//            it.apply {
//                it.mAdapter.items = (List(100) { index ->
//                    MyItem("item $index")
//                })
//            }
//        }
//
//        //only lastItemInvisible should be invoked
//        verify(exactly = 1) { mLastInvisible.invoke() }
//        verify(exactly = 0) { lastVisible.invoke() }
//        verify(exactly = 0) { firstInvisible.invoke() }
//        verify(exactly = 0) { firstVisible.invoke() }

        //todo for some reason i get unresolved reference R.id.activityMain_rv
//        onView(withId(R.id.activityMain_rv)).perform(
//        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
//            RecyclerViewActions.scrollToPosition<ViewHolder>(50)
//        )
//
//        verify(exactly = 1) { firstInvisible.invoke() }

        //todo
        // set adapter with large list - make sure last item invisible not triggered
        // scroll all the way down - make sure first item invisible not triggered
        // set all inner listeners in visiblity listener
        // set adapter with small list - make sure first and last listeners are called
        //      and invisible not called
        // set adapter with large list - make sure last item visible not triggered
        // scroll all the way down - make sure first item invisible triggered and last item visible triggered
    }


    //todo DxVisibilityListener
    // no (inner) listeners are set - no listener is called (does that even make sense?)
    // all listeners are set - only relevant ones are called (check class documentation)
    // not all listeners are set - non-set listeners aren't called (does that even make sense?)

    //todo DxScrollListener
    // no (inner) listeners are set - no listener is called (does that even make sense?)
    // all listeners are set - only relevant ones are called (check class documentation)
    // not all listeners are set - non-set listeners aren't called (does that even make sense?)
    // how do i test the sensitivity???????
}
