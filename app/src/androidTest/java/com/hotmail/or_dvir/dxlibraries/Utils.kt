package com.hotmail.or_dvir.dxlibraries

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.CoordinatesProvider


/*
* classes copied from
* https://stackoverflow.com/questions/54804321/how-to-write-test-case-for-recyclerview-drag-and-drop-using-espress
* */
/**
 * finds coordinates of an item within your RecyclerView
 */
class RecyclerViewCoordinatesProvider(
    private val position: Int,
    private val childItemCoordinatesProvider: CoordinatesProvider
) : CoordinatesProvider {

    override fun calculateCoordinates(view: View): FloatArray {
        return childItemCoordinatesProvider.calculateCoordinates(
            (view as RecyclerView).layoutManager!!.findViewByPosition(position)!!
        )
    }
}

///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////

/**
 * finds coordinates of a child with a given id
 */
class ChildViewCoordinatesProvider(
    private val childViewId: Int,
    private val insideChildViewCoordinatesProvider: CoordinatesProvider
) : CoordinatesProvider {
    override fun calculateCoordinates(view: View): FloatArray {
        return insideChildViewCoordinatesProvider.calculateCoordinates(
            view.findViewById(childViewId)
        )
    }
}

///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////
///////////////////////////////////////////

/**
 * custom general locations providing coordinates above and under a View
 * (assuming recycler view is using LinearLayoutManager)
 */
enum class CustomGeneralLocation : CoordinatesProvider {
    UNDER_RIGHT {
        override fun calculateCoordinates(view: View): FloatArray {
            val screenLocation = IntArray(2)
            view.getLocationOnScreen(screenLocation)
            return floatArrayOf(
                screenLocation[0] + view.width - 1f,
                screenLocation[1] + view.height * 1.5f
            )
        }
    },
    ABOVE_RIGHT {
        override fun calculateCoordinates(view: View): FloatArray {
            val screenLocation = IntArray(2)
            view.getLocationOnScreen(screenLocation)
            return floatArrayOf(
                screenLocation[0] + view.width - 1f,
                screenLocation[1] - view.height * 0.5f
            )
        }
    }
}
