/*
 * Copyright (c) 2018. 广州中爆数字信息科技股份有限公司.
 */

package cn.cbsd.mvplibrary.kit

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 用于设置RecyclerView的分隔线<br></br>
 * https://github.com/kymjs/RecyclerViewDemo/blob/master/RecyclerViewDemo/recycler/src/main/java/com/kymjs/recycler/Divider.java <br></br>
 * `
 * Divider divider = new Divider(new ColorDrawable(0xffff0000), OrientationHelper.VERTICAL);
 * //单位:px
 * divider.setMargin(50, 50, 50, 50);
 * divider.setHeight(20);
` *
 */
class Divider(divider: Drawable, orientation: Int) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    private var leftMargin: Int = 0
    private var rightMargin: Int = 0
    private var topMargin: Int = 0
    private var bottomMargin: Int = 0

    var width: Int = 0
    var height: Int = 0

    private var mOrientation: Int = 0

    init {
        setDivider(divider)
        setOrientation(orientation)
    }

    private fun setDivider(divider: Drawable) {
        this.mDivider = divider
        if (mDivider == null) {
            mDivider = ColorDrawable(-0x10000)
        }
        width = mDivider!!.intrinsicWidth
        height = mDivider!!.intrinsicHeight
    }

    private fun setOrientation(orientation: Int) {
        require(!(orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL)) { "invalid orientation" }
        mOrientation = orientation
    }

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        this.leftMargin = left
        this.topMargin = top
        this.rightMargin = right
        this.bottomMargin = bottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent)
        } else {
            drawVertical(c, parent)
        }
    }

    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + topMargin
        val bottom = parent.height - parent.paddingBottom - bottomMargin

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin + leftMargin
            val right = left + width
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + leftMargin
        val right = parent.width - parent.paddingRight - rightMargin

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + topMargin
            val bottom = top + height
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, leftMargin + width + rightMargin, 0)
        } else {
            outRect.set(0, 0, 0, topMargin + height + bottomMargin)
        }
    }

    companion object {

        /**
         * 获取默认的Divider
         *
         * @return
         */
        //divider.setMargin(10,10,10,10);
        @JvmStatic
        val defaultDividerItemDecoration: Divider
            get() {
                val divider = Divider(ColorDrawable(0x00000000), OrientationHelper.VERTICAL)
                divider.height = 5
                return divider
            }

        /**
         * 获取默认的Divider
         *
         * @return
         */
        @JvmStatic
        fun transparentDivider(height: Int): Divider {
            val divider = Divider(ColorDrawable(Color.TRANSPARENT), OrientationHelper.VERTICAL)
            divider.height = height
            return divider
        }

        /**
         * 获取默认的Divider
         *
         * @return
         */
        @JvmStatic
        fun defaultDivider(): Divider {
            val divider = Divider(ColorDrawable(Color.parseColor("#d9d9d9")), OrientationHelper.VERTICAL)
            divider.height = 1
            divider.setMargin(20, 0, 20, 0)
            return divider
        }
    }
}