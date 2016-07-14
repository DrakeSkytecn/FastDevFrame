package com.beyebe.fastdevframe.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.view.widget.holder.ShapeHolder
import java.util.*

/**
 * Created by xiayong on 2015/9/29.
 */
class CircleIndicator : View {

    private var viewPager: ViewPager? = null
    private var tabItems: MutableList<ShapeHolder>? = null
    private var movingItem: ShapeHolder? = null
    private var start: View? = null
    private var startInAnimation: Animation? = null
    private var startOutAnimation: Animation? = null

    //config list
    private var mCurItemPosition: Int = 0
    private var mCurItemPositionOffset: Float = 0.toFloat()
    private var mIndicatorRadius: Float = 0.toFloat()
    private var mIndicatorMargin: Float = 0.toFloat()
    private var mIndicatorBackground: Int = 0
    private var mIndicatorSelectedBackground: Int = 0
    private var mIndicatorLayoutGravity: Gravity? = null
    private var mIndicatorMode: Mode? = null

    //default value
    private val DEFAULT_INDICATOR_RADIUS = 10
    private val DEFAULT_INDICATOR_MARGIN = 40
    private val DEFAULT_INDICATOR_BACKGROUND = Color.BLUE
    private val DEFAULT_INDICATOR_SELECTED_BACKGROUND = Color.RED
    private val DEFAULT_INDICATOR_LAYOUT_GRAVITY = Gravity.CENTER.ordinal
    private val DEFAULT_INDICATOR_MODE = Mode.SOLO.ordinal

    enum class Gravity {
        LEFT,
        CENTER,
        RIGHT
    }

    enum class Mode {
        INSIDE,
        OUTSIDE,
        SOLO
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

    private fun init(context: Context, attrs: AttributeSet?) {
        tabItems = ArrayList<ShapeHolder>()
        handleTypedArray(context, attrs)
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator)
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_radius, DEFAULT_INDICATOR_RADIUS).toFloat()
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, DEFAULT_INDICATOR_MARGIN).toFloat()
        mIndicatorBackground = typedArray.getColor(R.styleable.CircleIndicator_ci_background, DEFAULT_INDICATOR_BACKGROUND)
        mIndicatorSelectedBackground = typedArray.getColor(R.styleable.CircleIndicator_ci_selected_background, DEFAULT_INDICATOR_SELECTED_BACKGROUND)
        val gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, DEFAULT_INDICATOR_LAYOUT_GRAVITY)
        mIndicatorLayoutGravity = Gravity.values()[gravity]
        val mode = typedArray.getInt(R.styleable.CircleIndicator_ci_mode, DEFAULT_INDICATOR_MODE)
        mIndicatorMode = Mode.values()[mode]
        typedArray.recycle()
    }

    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        createTabItems()
        createMovingItem()
        setUpListener()
    }

    fun setStart(start: View) {
        this.start = start
    }

    fun setStartInAnimation(startInAnimation: Animation) {
        this.startInAnimation = startInAnimation
    }

    fun setStartOutAnimation(startOutAnimation: Animation) {
        this.startOutAnimation = startOutAnimation
    }

    private fun setUpListener() {
        viewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (mIndicatorMode != Mode.SOLO) {
                    trigger(position, positionOffset)
                }
                if (start != null) {
                    if (position == tabItems!!.size - 1) {
                        start!!.visibility = View.VISIBLE
                        if (startInAnimation != null) {
                            start!!.startAnimation(startInAnimation)
                        }
                    } else {
                        if (startOutAnimation != null) {
                            start!!.startAnimation(startOutAnimation)
                        }
                        start!!.visibility = View.GONE
                    }
                }

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mIndicatorMode == Mode.SOLO) {
                    trigger(position, 0f)
                }
            }
        })
    }

    /**
     * trigger to redraw the indicator when the ViewPager's selected item changed!

     * @param position
     * *
     * @param positionOffset
     */
    private fun trigger(position: Int, positionOffset: Float) {
        this@CircleIndicator.mCurItemPosition = position
        this@CircleIndicator.mCurItemPositionOffset = positionOffset
        Log.e("CircleIndicator", "onPageScrolled()$position:$positionOffset")
        requestLayout()
        invalidate()
    }

    private fun createTabItems() {
        for (i in 0..viewPager!!.adapter.count - 1) {
            val circle = OvalShape()
            val drawable = ShapeDrawable(circle)
            val shapeHolder = ShapeHolder(drawable)
            val paint = drawable.paint
            paint.color = mIndicatorBackground
            paint.isAntiAlias = true
            shapeHolder.paint = paint
            tabItems!!.add(shapeHolder)
        }
    }

    private fun createMovingItem() {
        val circle = OvalShape()
        val drawable = ShapeDrawable(circle)
        movingItem = ShapeHolder(drawable)
        val paint = drawable.paint
        paint.color = mIndicatorSelectedBackground
        paint.isAntiAlias = true

        when (mIndicatorMode) {
            CircleIndicator.Mode.INSIDE -> paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            CircleIndicator.Mode.OUTSIDE -> paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            CircleIndicator.Mode.SOLO -> paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }

        movingItem!!.paint = paint
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.e("CircleIndicator", "onLayout()")
        super.onLayout(changed, left, top, right, bottom)
        val width = width
        val height = height
        layoutTabItems(width, height)
        layoutMovingItem(mCurItemPosition, mCurItemPositionOffset)
    }

    private fun layoutTabItems(containerWidth: Int, containerHeight: Int) {
        if (tabItems == null) {
            throw IllegalStateException("forget to create tabItems?")
        }
        val yCoordinate = containerHeight * 0.5f
        val startPosition = startDrawPosition(containerWidth)
        for (i in tabItems!!.indices) {
            val item = tabItems!![i]
            item.resizeShape(2 * mIndicatorRadius, 2 * mIndicatorRadius)
            item.y = yCoordinate - mIndicatorRadius
            val x = startPosition + (mIndicatorMargin + mIndicatorRadius * 2) * i
            item.x = x
        }

    }

    private fun startDrawPosition(containerWidth: Int): Float {
        if (mIndicatorLayoutGravity == Gravity.LEFT)
            return 0f
        val tabItemsLength = tabItems!!.size * (2 * mIndicatorRadius + mIndicatorMargin) - mIndicatorMargin
        if (containerWidth < tabItemsLength) {
            return 0f
        }
        if (mIndicatorLayoutGravity == Gravity.CENTER) {
            return (containerWidth - tabItemsLength) / 2
        }
        return containerWidth - tabItemsLength
    }

    private fun layoutMovingItem(position: Int, positionOffset: Float) {
        if (movingItem == null) {
            throw IllegalStateException("forget to create movingItem?")
        }
        val item = tabItems!![position]
        movingItem!!.resizeShape(item.width, item.height)
        val x = item.x + (mIndicatorMargin + mIndicatorRadius * 2) * positionOffset
        movingItem!!.x = x
        movingItem!!.y = item.y

    }

    override fun onDraw(canvas: Canvas) {
        Log.e("CircleIndicator", "onDraw()")
        super.onDraw(canvas)
        val sc = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,
                Canvas.MATRIX_SAVE_FLAG or
                        Canvas.CLIP_SAVE_FLAG or
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG or
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG or
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG)
        for (item in tabItems!!) {
            canvas.save()
            canvas.translate(item.x, item.y)
            item.shape!!.draw(canvas)
            canvas.restore()
        }

        if (movingItem != null) {
            canvas.save()
            canvas.translate(movingItem!!.x, movingItem!!.y)
            movingItem!!.shape!!.draw(canvas)
            canvas.restore()
        }
        canvas.restoreToCount(sc)
    }

    fun setIndicatorRadius(mIndicatorRadius: Float) {
        this.mIndicatorRadius = mIndicatorRadius
    }

    fun setIndicatorMargin(mIndicatorMargin: Float) {
        this.mIndicatorMargin = mIndicatorMargin
    }

    fun setIndicatorBackground(mIndicatorBackground: Int) {
        this.mIndicatorBackground = mIndicatorBackground
    }

    fun setIndicatorSelectedBackground(mIndicatorSelectedBackground: Int) {
        this.mIndicatorSelectedBackground = mIndicatorSelectedBackground
    }

    fun setIndicatorLayoutGravity(mIndicatorLayoutGravity: Gravity) {
        this.mIndicatorLayoutGravity = mIndicatorLayoutGravity
    }

    fun setIndicatorMode(mIndicatorMode: Mode) {
        this.mIndicatorMode = mIndicatorMode
    }
}
