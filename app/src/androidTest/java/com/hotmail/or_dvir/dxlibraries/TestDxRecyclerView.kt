package com.hotmail.or_dvir.dxlibraries

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxrecyclerview.DxRecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.EmptyListener
import io.mockk.spyk
import io.mockk.verify
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestDxRecyclerView {
    private lateinit var mFirstVisible: EmptyListener
    private lateinit var mFirstInvisible: EmptyListener

    private lateinit var mLastVisible: EmptyListener
    private lateinit var mLastInvisible: EmptyListener

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    @Before
    fun before() {
        //register idling resource
        onActivity {
            IdlingRegistry.getInstance().register(it.activityMain_rv.getIdlingResourceInstance())
        }

        //setup individual visibility listeners
        mFirstVisible = spyk({})
        mFirstInvisible = spyk({})

        mLastVisible = spyk({})
        mLastInvisible = spyk({})

        //set a fresh empty list and set the DxVisibilityListener using the individual listeners
        //from above
        onActivity {
            setListForActivity(0)
            it.activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {
                onFirstItemVisible = mFirstVisible
                onFirstItemInvisible = mFirstInvisible

                onLastItemVisible = mLastVisible
                onLastItemInvisible = mLastInvisible
            }
        }
    }

    @After
    fun after() {
        //unregister idling resource
        onActivity {
            IdlingRegistry.getInstance().unregister(it.activityMain_rv.getIdlingResourceInstance())
        }
    }

    /**
     * perform a dummy test that should always pass in order to make espresso
     * wait until all idling resources have finished
     */
    private fun pauseTestUntilAsyncOperationDone() {
        //the recycler view should always be visible, so this is a simple test
        //that should always pass
        //todo why am i getting unresolved reference for this id?????
//        onView(withId(R.id.activityMain_rv)).check(
        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).check(
            ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    private fun setListForActivity(listSize: Int) {
        onActivity {
            it.mAdapter.items = List(listSize) { index -> MyItem("items $index") }
        }
    }

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun visibilityListeners_shortListTest() {
        //creating a short list so both first and last fit on the screen
        setListForActivity(2)

        pauseTestUntilAsyncOperationDone()

//        ITS FAILING BECAUSE I ADDED A POST CALL TO THE RECYCLER VIEW !!!
//        SO THE TEST FAILS BECAUSE IT RUNS BEFORE THE RECYCLER VIEW FINISHED ITS LAYOUT PASS
//        Thread.sleep(200)
//
//        make idling resource into its own library!!!!!!!


        //mFirstVisible and mLastVisible should be invoked
        verify(exactly = 1) { mFirstVisible.invoke() }
        verify(exactly = 1) { mLastVisible.invoke() }

        //mFirstInvisible and mLastInvisible should NOT be invoked
        verify(exactly = 0) { mFirstInvisible.invoke() }
        verify(exactly = 0) { mLastInvisible.invoke() }
    }

    @Test
    fun visibilityListeners_longListTest() {
        //creating a long list that should not fit entirely on the screen
        setListForActivity(100)

        pauseTestUntilAsyncOperationDone()

        //mFirstVisible and mLastInvisible should be invoked
        verify(exactly = 1) { mFirstVisible.invoke() }
        verify(exactly = 1) { mLastInvisible.invoke() }

        //mFirstInvisible and mLastVisible should NOT be invoked
        verify(exactly = 0) { mFirstInvisible.invoke() }
        verify(exactly = 0) { mLastVisible.invoke() }


        //todo for some reason i get unresolved reference R.id.activityMain_rv
//        onView(withId(R.id.activityMain_rv)).perform(
//        onView(withClassName(containsString(DxRecyclerView::class.java.simpleName))).perform(
//            RecyclerViewActions.scrollToPosition<ViewHolder>(50)
//        )

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
