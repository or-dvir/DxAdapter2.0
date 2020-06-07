package com.hotmail.or_dvir.dxidlingresource

import android.util.Log
import androidx.test.espresso.idling.CountingIdlingResource

open class DxCountingIdlingResource(private val uniqueName: String) {
    companion object {
        val TAG = DxCountingIdlingResource::class.java.simpleName
    }

    //todo add a note in documentation that this will also run in release builds

    @JvmField
    val resource = CountingIdlingResource(uniqueName)

    fun increment(calledFrom: String) {
        Log.i(TAG, "idling resource \"$uniqueName\" incremented from $calledFrom")
        resource.increment()
    }

    fun decrement(calledFrom: String) {
        if (!resource.isIdleNow) {
            Log.i(TAG, "idling resource \"$uniqueName\" decremented from $calledFrom")
            resource.decrement()
        }
    }
}