package com.hotmail.or_dvir.dxlibraries

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.hotmail.or_dvir.dxlibraries.stickyheader.ActivityStickyHeader
import org.junit.Rule

class TestFeatureStickyHeader : BaseTest<ActivityStickyHeader>() {

    @get:Rule
    var scenario = ActivityScenarioRule(ActivityStickyHeader::class.java)
    override fun getTestActivityScenario() = scenario

    //todo
    // HOW DO I TEST THESE?!?! CANT FIND A WAY
    // make sure no header displayed
    // scroll down to first header - make sure its displayed
    // scroll down to second header - make sure its displayed
    // scroll up back to first - make sure its displayed
    // scroll up back to 0 - make sure no header is displayed
}