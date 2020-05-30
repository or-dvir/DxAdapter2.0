package com.hotmail.or_dvir.dxlibraries

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule

abstract class BaseTest {

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    fun onActivity(task: (act: ActivityMain) -> Unit): ActivityScenario<ActivityMain> =
        activityScenario.scenario.onActivity { task.invoke(it) }

    fun clickAtPosition(position: Int) {
        Log.i("aaaaa", "test clicking item at $position")
        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                click()
            )
        )
    }

    fun longClickAtPosition(position: Int) {
        Log.i("aaaaa", "test long-clicking item at $position")
        onView(withId(R.id.activityMain_rv)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                longClick()
            )
        )
    }
}