package com.hotmail.or_dvir.dxlibraries

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.EmptyListener
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDxRecyclerView {

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    private fun onActivity(task: (act: ActivityMain) -> Unit) =
        activityScenario.scenario.onActivity { task.invoke(it) }

    @Test
    fun visibilityListenerTest() {
        val firstVisible = mockk<EmptyListener>()
        val firstInvisible = mockk<EmptyListener>()
        every { firstVisible.invoke() } just runs
        every { firstInvisible.invoke() } just runs

        val lastVisible = mockk<EmptyListener>()
        val lastInvisible = mockk<EmptyListener>()
        every { lastVisible.invoke() } just runs
        every { lastInvisible.invoke() } just runs

        val listener = DxVisibilityListener().apply {
            onFirstItemVisible = firstVisible
            onFirstItemInvisible = firstInvisible

            onLastItemVisible = lastVisible
            onLastItemInvisible = lastInvisible
        }

//        onActivity {
//            it.apply {
//                //creating a short list so both first and last items appear on screen
//                mAdapter.items = (List(2) { MyItem("name") })
//                activityMain_rv.onItemsVisibilityListener = listener
//            }
//        }
//
//        //first and last item visibility listeners should be invoked
//        verify(exactly = 1) { firstVisible.invoke() }
//        verify(exactly = 1) { lastVisible.invoke() }
//
//        //first and last item invisibility listeners should NOT be invoked
//        verify(exactly = 0) { firstInvisible.invoke() }
//        verify(exactly = 0) { lastInvisible.invoke() }


        //creating a large list that should not fit on the screen
        onActivity { it.mAdapter.items = (List(100) { index -> MyItem("item $index") }) }

        onView(withId(R.id.activityMain_rv)).perform(
            RecyclerViewActions.scrollToPosition<ViewHolder>(50)
        )

//        verify(exactly = 1) { firstVisible.invoke() }

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
