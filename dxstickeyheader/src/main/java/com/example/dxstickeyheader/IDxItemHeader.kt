package com.example.dxstickeyheader

import android.view.View
import androidx.annotation.LayoutRes

interface IDxItemHeader
{
    /**
     * returns the resource id of the header layout
     */
    //todo add support for multiple types of headers- in order to do this you need to
    // add position parameter!!!
    // note that this might require some changes in the adapter!!!
    @LayoutRes
    fun getHeaderLayoutRes(/*headerPosition: Int*/): Int

    /**
     * binds data to the STICKY header view (NOT the header item in the adapter).
     * @param stickyHeader the sticky header view (the one drawn at the top)
     * @param headerAdapterPosition position of the header item IN THE ADAPTER (use this to get data to bind from the adapter).
     */
    fun bindStickyHeader(stickyHeader: View, headerAdapterPosition: Int)

    /**
     * returns whether or not the item at [adapterPosition] represents a header
     */
    fun isHeader(adapterPosition: Int): Boolean
}