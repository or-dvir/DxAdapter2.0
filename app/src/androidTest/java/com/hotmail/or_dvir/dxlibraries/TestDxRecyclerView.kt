package com.hotmail.or_dvir.dxlibraries

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDxRecyclerView {
    //todo DxVisibilityListener
    // visibility listener is null - nothing is called
    // no (inner) listener is set - no listener is called
    // only first visible is set - only it is called
    // only first invisible is set - only it is called
    // only last visible is set - only it is called
    // only last invisible is set - only it is called

    //todo DxScrollListener
    // scroll listener is null - nothing is called
    // no (inner) listener is set - no listener is called
    // only up is set - only it is called
    // only down is set - only it is called
    // only left is set - only it is called - need to change recycler view orientation!
    // only right is set - only it is called - need to change recycler view orientation!
    // how do i test the sensitivity???????
}
