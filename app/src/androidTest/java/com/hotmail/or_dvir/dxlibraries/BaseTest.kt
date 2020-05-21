package com.hotmail.or_dvir.dxlibraries

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule

abstract class BaseTest {

    @get:Rule
    var activityScenario = ActivityScenarioRule(ActivityMain::class.java)

    fun onActivity(task: (act: ActivityMain) -> Unit): ActivityScenario<ActivityMain> =
        activityScenario.scenario.onActivity { task.invoke(it) }

}