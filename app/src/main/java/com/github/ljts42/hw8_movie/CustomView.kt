package com.github.ljts42.hw8_movie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var matrix = Matrix()
    private var imageBitmap: Bitmap? = null
    private var xCoordinate = 0f
    private var yCoordinate = 0f
    private var rotationDegree = 0f
    private var velocityX = 5f
    private var velocityY = 5f
    private var isAnimating = false

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
            val imageResource = typedArray.getResourceId(R.styleable.CustomView_image, -1)
            if (imageResource != -1) {
                imageBitmap = BitmapFactory.decodeResource(resources, imageResource)

            }
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (imageBitmap != null && canvas != null) {
            val imageWidth = imageBitmap!!.width.toFloat()
            val imageHeight = imageBitmap!!.height.toFloat()

            xCoordinate += velocityX
            yCoordinate += velocityY
            if ((xCoordinate + imageWidth > width.toFloat() && velocityX > 0) || (xCoordinate < 0 && velocityX < 0)) {
                velocityX = -velocityX
            }
            if ((yCoordinate + imageHeight > height.toFloat() && velocityY > 0) || (yCoordinate < 0 && velocityY < 0)) {
                velocityY = -velocityY
            }

            matrix.setTranslate(xCoordinate, yCoordinate)
            rotationDegree += 6f
            matrix.postRotate(
                rotationDegree, xCoordinate + imageWidth / 2, yCoordinate + imageHeight / 2
            )
            canvas.drawBitmap(imageBitmap!!, matrix, null)

            if (isAnimating) {
                invalidate()
            }
        }
    }

    fun setImage(imageResource: Int) {
        imageBitmap = BitmapFactory.decodeResource(resources, imageResource)
        invalidate()
    }

    fun startAnimating() {
        isAnimating = true
        invalidate()
    }

    fun stopAnimating() {
        isAnimating = false
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val state = CustomViewState(superState)
        state.xCoordinate = xCoordinate
        state.yCoordinate = yCoordinate
        state.rotationDegree = rotationDegree
        state.velocityX = velocityX
        state.velocityY = velocityY
        state.isAnimating = isAnimating
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is CustomViewState) {
            super.onRestoreInstanceState(state.superState)
            xCoordinate = state.xCoordinate
            yCoordinate = state.yCoordinate
            rotationDegree = state.rotationDegree
            velocityX = state.velocityX
            velocityY = state.velocityY
            isAnimating = state.isAnimating
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    class CustomViewState : BaseSavedState {
        var xCoordinate: Float = 0f
        var yCoordinate: Float = 0f
        var rotationDegree: Float = 0f
        var velocityX: Float = 0f
        var velocityY: Float = 0f
        var isAnimating: Boolean = false

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            xCoordinate = parcel.readFloat()
            yCoordinate = parcel.readFloat()
            rotationDegree = parcel.readFloat()
            velocityX = parcel.readFloat()
            velocityY = parcel.readFloat()
            isAnimating = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(xCoordinate)
            out.writeFloat(yCoordinate)
            out.writeFloat(rotationDegree)
            out.writeFloat(velocityX)
            out.writeFloat(velocityY)
            out.writeByte(if (isAnimating) 1 else 0)
        }
    }
}