package cn.cbsd.mvplibrary.widget

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2018/12/13 11:17
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import cn.cbsd.mvplibrary.R

/**
 * 带清除的Editext
 * Created by opprime on 16-7-21.
 */
class ClearEditText : AppCompatEditText {
    private var mContext: Context? = null
    private var mClearButton: Bitmap? = null
    private var mPaint: Paint? = null

    val isShowing: Boolean = false

    //按钮显示方式
    private var mClearButtonMode: ClearButtonMode? = null
    //初始化输入框右内边距
    private var mInitPaddingRight: Int = 0
    //按钮的左右内边距，默认为3dp
    private var mButtonPadding = dp2px(3f)

    /**
     * 按钮显示方式 NEVER   不显示清空按钮 ALWAYS  始终显示清空按钮 WHILEEDITING   输入框内容不为空且有获得焦点 UNLESSEDITING  输入框内容不为空且没有获得焦点
     */
    enum class ClearButtonMode {
        NEVER, ALWAYS, WHILEEDITING, UNLESSEDITING
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * 初始化
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        this.mContext = context
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClearEditText)

        when (typedArray.getInteger(R.styleable.ClearEditText_clearButtonMode, 0)) {
            1 -> mClearButtonMode = ClearButtonMode.ALWAYS
            2 -> mClearButtonMode = ClearButtonMode.WHILEEDITING
            3 -> mClearButtonMode = ClearButtonMode.UNLESSEDITING
            else -> mClearButtonMode = ClearButtonMode.NEVER
        }

        val clearButton = typedArray.getResourceId(R.styleable.ClearEditText_clearButtonDrawable,
                R.drawable.ic_cancel_grey_400_18dp)
        typedArray.recycle()

        //按钮的图片
        mClearButton = (getDrawableCompat(clearButton) as BitmapDrawable).bitmap

        mPaint = Paint()
        mPaint!!.isAntiAlias = true

        mInitPaddingRight = paddingRight
    }

    /**
     * 按钮状态管理
     *
     * @param canvas onDraw的Canvas
     */
    private fun buttonManager(canvas: Canvas) {
        when (mClearButtonMode) {
            ClearEditText.ClearButtonMode.ALWAYS -> drawBitmap(canvas, getRect(true))
            ClearEditText.ClearButtonMode.WHILEEDITING -> drawBitmap(canvas, getRect(hasFocus() && text?.length!! > 0))
            ClearEditText.ClearButtonMode.UNLESSEDITING -> {
            }
            else -> drawBitmap(canvas, getRect(false))
        }
    }

    /**
     * 设置输入框的内边距
     *
     * @param isShow 是否显示按钮
     */
    private fun setPadding(isShow: Boolean) {
        val paddingRight = mInitPaddingRight + if (isShow) mClearButton!!.width + mButtonPadding + mButtonPadding else 0

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    /**
     * 取得显示按钮与不显示按钮时的Rect
     *
     * @param isShow 是否显示按钮
     */
    private fun getRect(isShow: Boolean): Rect {
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int

        right = if (isShow) measuredWidth + scrollX - mButtonPadding - mButtonPadding else 0
        left = if (isShow) right - mClearButton!!.width else 0
        top = if (isShow) (measuredHeight - mClearButton!!.height) / 2 else 0
        bottom = if (isShow) top + mClearButton!!.height else 0


        //更新输入框内边距
        setPadding(isShow)


        return Rect(left, top, right, bottom)
    }

    /**
     * 绘制按钮图片
     *
     * @param canvas onDraw的Canvas
     * @param rect 图片位置
     */
    private fun drawBitmap(canvas: Canvas, rect: Rect?) {
        if (rect != null) {
            canvas.drawBitmap(mClearButton!!, null, rect, mPaint)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        buttonManager(canvas)

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP ->
                //判断是否点击到按钮所在的区域
                if (event.x - (measuredWidth - paddingRight) >= 0) {
                    error = null
                    this.setText("")
                }
        }

        return super.onTouchEvent(event)
    }

    /**
     * 获取Drawable
     *
     * @param resourseId 资源ID
     */
    private fun getDrawableCompat(resourseId: Int): Drawable {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(resourseId, mContext!!.theme)
        } else {
            resources.getDrawable(resourseId)
        }
    }

    /**
     * 设置按钮左右内边距
     *
     * @param buttonPadding 单位为dp
     */
    fun setButtonPadding(buttonPadding: Int) {
        this.mButtonPadding = dp2px(buttonPadding.toFloat())
    }

    /**
     * 设置按钮显示方式
     *
     * @param clearButtonMode 显示方式
     */
    fun setClearButtonMode(clearButtonMode: ClearButtonMode) {
        this.mClearButtonMode = clearButtonMode
    }

    fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}