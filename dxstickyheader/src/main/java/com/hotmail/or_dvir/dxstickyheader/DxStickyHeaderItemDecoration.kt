package com.hotmail.or_dvir.dxstickyheader

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * a wrapper for [RecyclerView.ItemDecoration].
 * add this to your [RecyclerView].
 */
open class DxStickyHeaderItemDecoration<ITEM : IDxBaseItem>(
    private val stickyHeaderFeature: DxFeatureStickyHeader<ITEM>
) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, recyclerView, state)

        val topChild = recyclerView.layoutManager?.getChildAt(0) ?: return

        val topChildPosition = recyclerView.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION)
            return

        val currentHeader = getHeaderViewForItemPosition(topChildPosition, recyclerView) ?: return
        fixLayoutSize(recyclerView, currentHeader)

        val childInContact = getChildInContact(recyclerView, currentHeader.bottom) ?: return

        if (stickyHeaderFeature.isHeader(recyclerView.getChildAdapterPosition(childInContact))) {
            moveAndDrawHeader(
                c,
                currentHeader,
                0f,
                childInContact.top.toFloat() - currentHeader.height
            )
            return
        }

        moveAndDrawHeader(c, currentHeader, 0f, 0f)
    }

    private fun getHeaderPositionFromItemPosition(position: Int) =
        (position downTo 0).firstOrNull { stickyHeaderFeature.isHeader(it) }

    private fun getHeaderViewForItemPosition(itemPosition: Int, recyclerView: RecyclerView): View? {
        val headerPosition = getHeaderPositionFromItemPosition(itemPosition) ?: return null

        //todo how can i improve this??? do not inflate every time!!!!

        val header = LayoutInflater.from(recyclerView.context)
            .inflate(
                stickyHeaderFeature.getHeaderLayoutRes(),
                recyclerView,
                false
            )

        stickyHeaderFeature.apply {
            bindStickyHeader(header, headerPosition, getItem(headerPosition))
        }
        return header
    }

    @Suppress("SameParameterValue")
    private fun moveAndDrawHeader(
        c: Canvas,
        currentHeader: View,
        translationX: Float,
        translationY: Float
    ) {
        c.save()
        c.translate(translationX, translationY)
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(recyclerView: RecyclerView, contactPoint: Int): View? {
        var child: View
        val viewRect = Rect()

        for (i in 0 until recyclerView.childCount) {
            child = recyclerView.getChildAt(i)
            recyclerView.getDecoratedBoundsWithMargins(child, viewRect)
            if (viewRect.contains(child.left, contactPoint))
                return child
        }

        return null
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        //Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        //Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, /*mStickyHeaderHeight = */view.measuredHeight)
    }
}