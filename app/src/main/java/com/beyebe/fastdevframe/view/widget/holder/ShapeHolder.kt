package com.beyebe.fastdevframe.view.widget.holder

import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable

/**
 * A data structure that holds a Shape and various properties that can be used to define
 * how the shape is drawn.
 */
class ShapeHolder(var shape: ShapeDrawable?) {
    var x = 0f
    var y = 0f
    var color: Int = 0
        set(value) {
            shape!!.paint.color = value
            color = value

        }
    private var alpha = 1f
    var paint: Paint? = null

    fun setAlpha(alpha: Float) {
        this.alpha = alpha
        shape!!.alpha = (alpha * 255f + .5f).toInt()
    }

    var width: Float
        get() = shape!!.shape.width
        set(width) {
            val s = shape!!.shape
            s.resize(width, s.height)
        }

    var height: Float
        get() = shape!!.shape.height
        set(height) {
            val s = shape!!.shape
            s.resize(s.width, height)
        }

    fun resizeShape(width: Float, height: Float) {
        shape!!.shape.resize(width, height)
    }
}
