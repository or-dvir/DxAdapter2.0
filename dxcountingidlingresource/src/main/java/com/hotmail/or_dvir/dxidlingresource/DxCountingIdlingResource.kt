package com.hotmail.or_dvir.dxidlingresource

import android.util.Log
import androidx.test.espresso.idling.CountingIdlingResource

/**
 * a convenience class that holds a [CountingIdlingResource].
 * This class is used in DxRecyclerView for automated UI tests.
 *
 * note that while idling resources are used for testing, their logic also executes in
 * production/release versions
 */
open class DxCountingIdlingResource(private val uniqueName: String) {
    companion object {
        val TAG = DxCountingIdlingResource::class.java.simpleName
    }

    @JvmField
    val resource = CountingIdlingResource(uniqueName)

    /**
     * increments the [CountingIdlingResource] held by this class
     */
    fun increment(calledFrom: String) {
        Log.i(TAG, "idling resource \"$uniqueName\" incremented from $calledFrom")
        resource.increment()
    }

    /**
     * decrements the [CountingIdlingResource] held by this class. If the resource is idle,
     * this function does nothing
     */
    fun decrement(calledFrom: String) {
        if (!resource.isIdleNow) {
            Log.i(TAG, "idling resource \"$uniqueName\" decremented from $calledFrom")
            resource.decrement()
        }
    }
}