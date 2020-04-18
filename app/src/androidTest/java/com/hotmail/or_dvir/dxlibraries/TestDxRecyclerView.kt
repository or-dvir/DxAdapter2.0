package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.GenericListener
import io.mockk.spyk
import io.mockk.verify
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestDxRecyclerView {
    //visibility listeners
    private lateinit var mFirstVisible: GenericListener
    private lateinit var mFirstInvisible: GenericListener

    private lateinit var mLastVisible: GenericListener
    private lateinit var mLastInvisible: GenericListener

    //scroll listeners
    private lateinit var mOnScrollUp: GenericListener
    private lateinit var mOnScrollDown: GenericListener
    private lateinit var mOnScrollLeft: GenericListener
    private lateinit var mOnScrollRight: GenericListener


    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Before
    fun before() {
        //register idling resource
        onActivity {
            IdlingRegistry.getInstance().register(it.activityMain_rv.getIdlingResourceInstance())
        }

        //set a fresh empty list.
        //IMPORTANT NOTE
        //this method should NOT be called from inside the activity (e.g. inside onActivity{} block)
        //or the test will get stuck!!!
        setListForActivity(0)
    }

    @After
    fun after() {
        //unregister idling resource
        onActivity {
            IdlingRegistry.getInstance().unregister(it.activityMain_rv.getIdlingResourceInstance())
        }
    }

    //todo for some reason when i use this line in my tests i get "unresolved reference"
    // onView(withId(R.id.activityMain_rv))

    /**
     * perform a dummy test that should always pass in order to make espresso
     * wait until all idling resources have finished
     */
    private fun pauseTestUntilAsyncOperationDone() {
        //the recycler view should always be visible, so this is a simple test
        //that should always pass
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).check(
            ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    private fun setListForActivity(listSize: Int) {
        onActivity {
            it.mAdapter.setItems(List(listSize) { index -> MyItem("item $index") })
        }

        //since the listeners may be called after a small delay, we need to wait for
        //idling resources in order for testing to not fail
        pauseTestUntilAsyncOperationDone()
    }

    private fun setupVisibilityListeners() {
        mFirstVisible = spyk({})
        mFirstInvisible = spyk({})

        mLastVisible = spyk({})
        mLastInvisible = spyk({})

        onActivity {
            it.activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {
                onFirstItemVisible = mFirstVisible
                onFirstItemInvisible = mFirstInvisible

                onLastItemVisible = mLastVisible
                onLastItemInvisible = mLastInvisible
            }
        }
    }

    @Test
    fun visibilityListenersTest_shortList() {
        setupVisibilityListeners()

        //creating a short list so both first and last fit on the screen
        setListForActivity(2)

        //mFirstVisible and mLastVisible should be invoked
        verify(exactly = 1) { mFirstVisible.invoke() }
        verify(exactly = 1) { mLastVisible.invoke() }

        //mFirstInvisible and mLastInvisible should NOT be invoked
        verify(exactly = 0) { mFirstInvisible.invoke() }
        verify(exactly = 0) { mLastInvisible.invoke() }
    }

    @Test
    fun visibilityListenersTest_longList() {
        setupVisibilityListeners()

        //creating a long list that should not fit entirely on the screen
        val longListSize = 100
        setListForActivity(longListSize)

        //mFirstVisible and mLastInvisible should be invoked
        verify(exactly = 1) { mFirstVisible.invoke() }
        verify(exactly = 1) { mLastInvisible.invoke() }

        //mFirstInvisible and mLastVisible should NOT be invoked
        verify(exactly = 0) { mFirstInvisible.invoke() }
        verify(exactly = 0) { mLastVisible.invoke() }


        //scroll to end of list
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            //NOTE: the position parameter must be within the recycler view bounds!
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(longListSize - 1)
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //mFirstInvisible and mLastVisible should be called
        verify(exactly = 1) { mFirstInvisible.invoke() }
        verify(exactly = 1) { mLastVisible.invoke() }

        //mFirstVisible and mLastInvisible should NOT be called
        //NOTE: there is no way to reset the "call count" of mockk.verify{},
        //so we most account for previous invocations
        verify(atMost = 1) { mFirstVisible.invoke() }
        verify(atMost = 1) { mLastInvisible.invoke() }

        //scroll to top of list
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //mFirstVisible and mLastInvisible should be called
        //NOTE: there is no way to reset the "call count" of mockk.verify{},
        //so we most account for previous invocations
        verify(exactly = 2) { mFirstVisible.invoke() }
        verify(exactly = 2) { mLastInvisible.invoke() }

        //mFirstInvisible and mLastVisible should NOT be called
        //NOTE: there is no way to reset the "call count" of mockk.verify{},
        //so we most account for previous invocations
        verify(exactly = 1) { mFirstInvisible.invoke() }
        verify(exactly = 1) { mLastVisible.invoke() }
    }

    private fun setupScrollListeners(sensitivity: Int) {
        mOnScrollUp = spyk({})
        mOnScrollDown = spyk({})
        mOnScrollLeft = spyk({})
        mOnScrollRight = spyk({})

        onActivity {
            it.activityMain_rv.onScrollListener = DxScrollListener(sensitivity).apply {
                onScrollUp = mOnScrollUp
                onScrollDown = mOnScrollDown
                onScrollLeft = mOnScrollLeft
                onScrollRight = mOnScrollRight
            }
        }
    }

    private fun swipe(speed: Swipe, from: GeneralLocation, to: GeneralLocation) =
        GeneralSwipeAction(speed, from, to, Press.FINGER)

    private fun swipeUp(speed: Swipe) =
        swipe(speed, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER)

    private fun swipeDown(speed: Swipe) =
        swipe(speed, GeneralLocation.TOP_CENTER, GeneralLocation.BOTTOM_CENTER)

    private fun swipeUpFast() = swipeUp(Swipe.FAST)
    private fun swipeUpSlow() = swipeUp(Swipe.SLOW)

    private fun swipeDownFast() = swipeDown(Swipe.FAST)
    private fun swipeDownSlow() = swipeDown(Swipe.SLOW)

    @Test
    fun scrollListenerTest_highSensitivity() {
        //high sensitivity
        setupScrollListeners(200)
        val listSize = 100
        setListForActivity(listSize)

        onActivity { it.setLayoutManagerVertical() }

        //NOTE:
        //this function only tests that a slow scroll does not trigger listener with
        //high sensitivity.
        //all other scroll tests are performed in their own functions


        //scroll down slow
        //NOTE: using swipe action and not scrollToPosition() because scrollToPosition()
        //does not trigger the scroll listener properly (dx and dy values are 0)
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            swipeUpSlow()
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //verify only mOnScrollDown invoked (will be invoked many times)
        verify(exactly = 0) { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }
        verify(exactly = 0) { mOnScrollRight.invoke() }
    }

    @Test
    fun scrollListenerTest_vertical() {
        //low sensitivity to guarantee listeners will be called
        setupScrollListeners(1)
        val listSize = 100
        setListForActivity(listSize)

        onActivity { it.setLayoutManagerVertical() }

        //verify no listeners have been invoked
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }
        verify(exactly = 0) { mOnScrollRight.invoke() }

        //scroll down fast
        //NOTE: using swipe action and not scrollToPosition() because scrollToPosition()
        //does not trigger the scroll listener properly (dx and dy values are 0)
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            swipeUpFast()
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //verify only mOnScrollDown invoked (will be invoked many times)
        verify { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }
        verify(exactly = 0) { mOnScrollRight.invoke() }

        //scroll up fast
        //NOTE: using swipe action and not scrollToPosition() because scrollToPosition()
        //does not trigger the scroll listener properly (dx and dy values are 0)
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            swipeDownFast()
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //verify only mOnScrollUp invoked (will be invoked many times).
        //NOTE: there is no way to reset the "call count" of mockk.verify{},
        //so we most account for previous invocations
        verify { mOnScrollUp.invoke() }
        verify { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }
        verify(exactly = 0) { mOnScrollRight.invoke() }
    }

    @Test
    fun scrollListenerTest_horizontal() {
        //low sensitivity to guarantee listeners will be called
        setupScrollListeners(1)
        val listSize = 100
        setListForActivity(listSize)

        onActivity { it.setLayoutManagerHorizontal() }

        //verify no listeners have been invoked
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }
        verify(exactly = 0) { mOnScrollRight.invoke() }

        //scroll to end of list.
        //NOTE: using swipe action and not scrollToPosition() because scrollToPosition()
        //does not trigger the scroll listener properly (dx and dy values are 0)
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            ViewActions.swipeLeft()
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //verify only mOnScrollRight invoked (will be invoked many times)
        verify { mOnScrollRight.invoke() }
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollDown.invoke() }
        verify(exactly = 0) { mOnScrollLeft.invoke() }

        //scroll to top of list
        //NOTE: using swipe action and not scrollToPosition() because scrollToPosition()
        //does not trigger the scroll listener properly (dx and dy values are 0)
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
            ViewActions.swipeRight()
        )

        //wait for the scroll to finish
        pauseTestUntilAsyncOperationDone()

        //verify only mOnScrollLeft invoked (will be invoked many times).
        //NOTE: there is no way to reset the "call count" of mockk.verify{},
        //so we most account for previous invocations
        verify { mOnScrollLeft.invoke() }
        verify { mOnScrollRight.invoke() }
        verify(exactly = 0) { mOnScrollUp.invoke() }
        verify(exactly = 0) { mOnScrollDown.invoke() }
    }
}
