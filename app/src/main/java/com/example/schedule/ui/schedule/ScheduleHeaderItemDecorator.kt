package com.example.schedule.ui.schedule

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.schedule.R

class ScheduleHeaderItemDecorator internal constructor(recyclerView: RecyclerView,
                                                       private val mListener: StickyHeaderInterface)
    : ItemDecoration() {
    private var mStickyHeaderHeight = 0
    private val res = recyclerView.resources

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0)
        topChild?.let {
            val topChildPosition = parent.getChildAdapterPosition(topChild)
            if (topChildPosition != RecyclerView.NO_POSITION) {
                val currentHeader = getHeaderViewForItem(topChildPosition, parent)
                fixLayoutSize(parent, currentHeader)

                val contactPoint = currentHeader.bottom.toFloat()
                val childInContact = getChildInContact(parent, contactPoint.toInt())
                val px = toPx(res, R.dimen.card_view_schedule_margin)

                if (childInContact != null) {
                    if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
                        moveHeader(c, currentHeader, childInContact)
                    } else drawHeader(c, currentHeader)

                } else if (getChildInContact(parent, (contactPoint + px).toInt()) != null
                        || getChildInContact(parent, (contactPoint - px).toInt()) != null) {
                    drawHeader(c, currentHeader)
                }
            }
        }
    }

    private fun toPx(res: Resources, id: Int) = res.getDimension(id).toInt()

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View {
        val headerPosition = mListener.getHeaderPositionForItem(itemPosition)
        val layoutResId = mListener.getHeaderLayout(headerPosition)
        val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        mListener.bindHeaderData(header, headerPosition)
        return header
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                childInContact = child
                break
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight.also { mStickyHeaderHeight = it })
    }

    interface StickyHeaderInterface {
        /**
         * This method gets called by [ScheduleHeaderItemDecorator] to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         *
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        fun getHeaderPositionForItem(itemPosition: Int): Int

        /**
         * This method gets called by [ScheduleHeaderItemDecorator] to get layout resource id for the header item at specified adapter's position.
         *
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        fun getHeaderLayout(headerPosition: Int): Int

        /**
         * This method gets called by [ScheduleHeaderItemDecorator] to setup the header View.
         *
         * @param header         View. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        fun bindHeaderData(header: View, headerPosition: Int)

        /**
         * This method gets called by [ScheduleHeaderItemDecorator] to verify whether the item represents a header.
         *
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        fun isHeader(itemPosition: Int): Boolean
    }

    init {

        // On Sticky Header Click
        recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
                // Handle the clicks on the header here ...

                return motionEvent.y <= mStickyHeaderHeight
            }

            override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }
}
