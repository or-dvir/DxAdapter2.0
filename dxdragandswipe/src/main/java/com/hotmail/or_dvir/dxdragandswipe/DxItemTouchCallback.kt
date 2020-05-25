package com.hotmail.or_dvir.dxdragandswipe

import android.graphics.Canvas
import android.graphics.Rect
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.drag.IDxItemDraggable
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxFeatureSwipe
import com.hotmail.or_dvir.dxdragandswipe.swipe.DxSwipeBackground
import com.hotmail.or_dvir.dxdragandswipe.swipe.IDxItemSwipeable
import kotlin.math.roundToInt

class DxItemTouchCallback<ITEM : IDxBaseItem>(private val mAdapter: DxAdapter<ITEM, *>) :
    ItemTouchHelper.Callback() {

    //region
    //fields for onChildDraw() for better performance (onChildDraw() will be called many times)
    private var mSwipeBackgroundForDrawing: DxSwipeBackground? = null
    private var mIsSwipingLeft = false
    private var mDoesBackFit = false
    private var mIconTop = 0
    private var mIconBottom = 0
    private var mIconLeft = 0
    private var mIconRight = 0
    private var mTextX = 0f
    private var mTextY = 0f
    private val mTextRect = Rect()
    //endregion

    //region optional variables
    //todo when documenting note that there is no need to add the feature to the adapter
    var dragFeature: DxFeatureDrag<ITEM>? = null
        set(value) {
            val prevField = field
            field = value

            if (value != null) {
                mAdapter.addFeature(value)
            } else if (prevField != null) {
                mAdapter.removeFeature(prevField)
            }
        }

    //todo when documenting note that there is no need to add the feature to the adapter
    // even though nothing will happen as the map in DxAdapter will override it
    var swipeFeature: DxFeatureSwipe<ITEM>? = null
        set(value) {
            val prevField = field
            field = value

            if (value != null) {
                mAdapter.addFeature(value)
            } else if (prevField != null) {
                mAdapter.removeFeature(prevField)
            }
        }
    //endregion

    internal fun setUpDragWithHandle(@IdRes handleId: Int, touchHelper: ItemTouchHelper) {
        dragFeature?.apply {
            dragHandleId = handleId
            itemTouchHelper = touchHelper
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return dragFeature?.let {
            it.dragOnLongClick && it.isDragEnabled
        } ?: false
    }

    override fun onSelectedChanged(holder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(holder, actionState)

        holder?.apply {
            when (actionState) {
                ItemTouchHelper.ACTION_STATE_DRAG -> dragFeature?.notifyDragStart(mAdapter, this)
                ItemTouchHelper.ACTION_STATE_SWIPE -> swipeFeature?.notifySwipeStart(mAdapter, this)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, holder: ViewHolder) {
        super.clearView(recyclerView, holder)

        //NOTE:
        //there is no bug here - in case of drag the listener for swipe will not be called
        //(and vice-versa). there are flags inside each feature to prevent this
        dragFeature?.notifyDragEnd(mAdapter, holder)
        swipeFeature?.notifySwipeEnd(mAdapter, holder)
    }

    override fun getMovementFlags(recycler: RecyclerView, holder: ViewHolder): Int {
        val item = mAdapter.getDxAdapterItems()[holder.adapterPosition]

        val isDragEnabled = dragFeature?.isDragEnabled ?: false
        val dragFlags =
            if (item !is IDxItemDraggable || !isDragEnabled || dragFeature == null) {
                0
            } else {
                dragFeature!!.dragDirections
            }

        val isSwipeEnabled = swipeFeature?.isSwipeEnabled ?: false
        val swipeFlags =
            if (item !is IDxItemSwipeable || !isSwipeEnabled || swipeFeature == null) {
                0
            } else {
                swipeFeature!!.swipeDirections
            }

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        dragged: ViewHolder,
        target: ViewHolder
    ): Boolean {
        mAdapter.apply {
            val draggedPosition = dragged.adapterPosition
            val targetPosition = target.adapterPosition

            //NOTE:
            //should call this BEFORE actually moving the items. otherwise draggedPosition
            //and targetPosition would ve reversed (because they have been switched...)
            //todo add this note to documentation
            dragFeature?.onItemMoved?.invoke(
                dragged.itemView,
                draggedPosition,
                getItem(draggedPosition),
                target.itemView,
                target.adapterPosition,
                getItem(targetPosition)
            )

            getDxAdapterItems().apply {
                val itemBackup = removeAt(draggedPosition)
                add(targetPosition, itemBackup)
            }

            notifyItemMoved(draggedPosition, targetPosition)
        }

        return true
    }

    override fun onSwiped(holder: ViewHolder, direction: Int) {
        holder.apply {
            swipeFeature?.onItemSwiped?.invoke(
                itemView,
                adapterPosition,
                direction,
                mAdapter.getItem(adapterPosition)
            )
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return swipeFeature?.let { feature ->
            feature.swipeEscapeVelocityMultiplier?.let {
                it * defaultValue
            } ?: feature.swipeEscapeVelocity
        } ?: super.getSwipeEscapeVelocity(defaultValue)
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder) =
        swipeFeature?.swipeThreshold ?: super.getSwipeThreshold(viewHolder)

    //NOTE:
    //even though the parameter swipeBack might always refer to mSwipeBackgroundForDrawing,
    //its safer to have it as a non-null parameter in case i make changes in the future
    private fun calculateIconLeft(swipeBack: DxSwipeBackground, isSwipingLeft: Boolean): Int {
        swipeBack.apply {
            mDoesBackFit = doesBackgroundFitInSwipeArea()
            var temp: Int

            backgroundColorDrawable.bounds.let { bounds ->
                return when {
                    mDoesBackFit && isSwipingLeft -> {
                        temp = bounds.right - iconWidthPx
                        if (iconWidthPx > 0) {
                            temp -= paddingPx
                        }

                        temp
                    }

                    //swiping right
                    mDoesBackFit -> {
                        temp = bounds.left
                        if (iconWidthPx > 0) {
                            temp += paddingPx
                        }

                        temp
                    }

                    //mDoesBackFit is FALSE
                    isSwipingLeft -> {
                        temp = bounds.left + paddingPx
                        if (textWidthPx > 0) {
                            temp += textWidthPx + paddingPx
                        }

                        temp
                    }
                    //swiping right
                    else -> {
                        temp = bounds.right - paddingPx - iconWidthPx
                        //NOTE:
                        //do NOT do "temp -= <expression>" here because the order of
                        //operations is slightly different and with certain values it will
                        //cause bugs
                        if (textWidthPx > 0) {
                            temp = temp - textWidthPx - paddingPx
                        }

                        temp
                    }
                }
            }
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        holder: ViewHolder,
        dx: Float,
        dy: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //dx will be 0 when not swiping, or swiping but item is exactly in the middle.
        //adapter position will be -1 if the item is being removed from the adapter.
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE ||
            dx == 0f ||
            holder.adapterPosition == -1
        ) {
            //we must still call the super method
            super.onChildDraw(canvas, recyclerView, holder, dx, dy, actionState, isCurrentlyActive)
            return
        }

        //unlike the others, this variable is here and not global because we shouldn't keep
        //references to views
        val itemView = holder.itemView
        mIsSwipingLeft = dx < 0

        swipeFeature?.apply {
            mSwipeBackgroundForDrawing =
                when {
                    mIsSwipingLeft -> {
                        getSwipeBackgroundLeft(
                            itemView,
                            holder.adapterPosition,
                            mAdapter.getItem(holder.adapterPosition)
                        )?.apply {
                            backgroundColorDrawable.setBounds(
                                itemView.right + dx.roundToInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                        }
                    }
                    //swiping right
                    else -> {
                        getSwipeBackgroundRight(
                            itemView,
                            holder.adapterPosition,
                            mAdapter.getItem(holder.adapterPosition)
                        )?.apply {
                            backgroundColorDrawable.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.left + dx.roundToInt(),
                                itemView.bottom
                            )
                        }
                    }
                }
        }

        mSwipeBackgroundForDrawing?.apply {
            //NOTE:
            //drawing background MUST come BEFORE drawing the mText
            backgroundColorDrawable.let { backDraw ->
                backDraw.draw(canvas)

                mIconTop = backDraw.bounds.centerY() - mHalfIconHeight
                mIconBottom = backDraw.bounds.centerY() + mHalfIconHeight
                mIconLeft = calculateIconLeft(this, mIsSwipingLeft)
                mIconRight = mIconLeft + iconWidthPx

                dxIcon?.mIconDrawable?.apply {
                    setBounds(mIconLeft, mIconTop, mIconRight, mIconBottom)
                    draw(canvas)
                }

                dxText?.apply {
                    mPaint.getTextBounds(text, 0, text.length, mTextRect)

                    mTextY = backDraw.bounds.exactCenterY() + (mTextRect.height() / 4f)
                    mTextX =
                        if (mIsSwipingLeft) {
                            mIconLeft.toFloat() - paddingPx - textWidthPx
                        } else {
                            //swiping right
                            mIconRight.toFloat() + paddingPx
                        }

                    canvas.drawText(text, mTextX, mTextY, mPaint)
                }
            }
        }

        super.onChildDraw(canvas, recyclerView, holder, dx, dy, actionState, isCurrentlyActive)
    }
}