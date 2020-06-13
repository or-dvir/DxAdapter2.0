package com.hotmail.or_dvir.dxadapterv2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * finds coordinates of an item within your RecyclerView.
 *
 * copied from https://stackoverflow.com/questions/54804321/how-to-write-test-case-for-recyclerview-drag-and-drop-using-espress
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

/**
 * copied from https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso#answer-34795431
 */
fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}
