package com.fishweeb.practical

import android.graphics.Bitmap
import android.view.SurfaceView

open class DynamicButtonEntity : ButtonEntity() {
    private var bmpPress: Bitmap? = null
    private var bmpOriginal: Bitmap? = null
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        bmp = bmpOriginal

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    override fun OnClickFunction() {
        bmp = bmpPress
    }

    override fun OffClickFunction() {
        bmp = bmpOriginal
    }

    companion object {
        fun Create(
            xPos: Float,
            yPos: Float,
            _width: Float,
            _height: Float,
            buttonImage: Bitmap?,
            buttonPressImage: Bitmap?
        ): DynamicButtonEntity {
            val result = DynamicButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.width = _width
            result.height = _height
            result.bmpOriginal = Bitmap.createScaledBitmap(
                buttonImage!!,
                result.width.toInt(),
                result.height.toInt(),
                true
            )
            result.bmpPress = Bitmap.createScaledBitmap(
                buttonPressImage!!,
                result.width.toInt(),
                result.height.toInt(),
                true
            )
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
