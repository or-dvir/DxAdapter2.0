package com.hotmail.or_dvir.dxidlingresource

import android.util.Log
import androidx.test.espresso.idling.CountingIdlingResource

class DxCountingIdlingResource(private val uniqueName: String) {
    companion object {
        val TAG = DxCountingIdlingResource::class.java.simpleName
    }

    //todo add a note in documentation that this will also run in release builds

    //resource name should be unique so we use TAG which is the class name
    @JvmField
    val mIdlingResource = CountingIdlingResource(uniqueName)

    fun increment(calledFrom: String) {
        Log.i(TAG, "idling resource $uniqueName incremented from $calledFrom")
        mIdlingResource.increment()
    }

    fun decrement(calledFrom: String) {
        if (!mIdlingResource.isIdleNow) {
            Log.i(TAG, "idling resource $uniqueName decremented from $calledFrom")
            mIdlingResource.decrement()
        }
    }
}