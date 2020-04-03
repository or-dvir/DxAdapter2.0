package com.hotmail.or_dvir.dxlibraries

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.EmptyListener
import io.mockk.*
import kotlinx.android.synthetic.main.activity_main.*
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
        val emptyBlock = mockk<EmptyListener>()
        every { emptyBlock.invoke() } just runs

        val listener = DxVisibilityListener().apply {
            onFirstItemVisible = emptyBlock
        }

        onActivity {
            it.apply {
                activityMain_rv.onItemsVisibilityListener = listener
                //creating a short list so both first and last items appear on screen
                mAdapter.items = (List(2) { MyItem("name") })
            }
        }

        verify { emptyBlock.invoke() }


        //todo
        // set adapter with small list - make sure first and last listeners are not called
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
