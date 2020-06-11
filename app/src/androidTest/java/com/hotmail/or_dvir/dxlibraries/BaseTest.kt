package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule

abstract class BaseTest<T: BaseFeatureActivity> {

    abstract fun getTestActivityScenario(): ActivityScenarioRule<T>

    fun onActivity(task: (act: BaseFeatureActivity) -> Unit): ActivityScenario<T> =
        getTestActivityScenario().scenario.onActivity { task.invoke(it) }

    fun scrollToPosition(position: Int) {
        onView(withId(R.id.activityBase_rv)).perform(scrollToPosition<ViewHolder>(position))
    }

    fun clickAtPosition(position: Int) {
        Log.i("aaaaa", "test clicking item at $position")
        onView(withId(R.id.activityBase_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(
                position,
                click()
            )
        )
    }

    fun longClickAtPosition(position: Int) {
        Log.i("aaaaa", "test long-clicking item at $position")
        onView(withId(R.id.activityBase_rv)).perform(
            actionOnItemAtPosition<ViewHolder>(
                position,
                longClick()
            )
        )
    }
}