package edu.androidgroupproject

import android.graphics.Bitmap
import android.view.SurfaceView

class BackEntity : ButtonEntity() {
    private var bmpPress: Bitmap? = null
    private var bmpOriginal: Bitmap? = null
    private var nextState: String? = null
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        bmpOriginal = Bitmap.createScaledBitmap(
            ImageManager.Instance.GetImage(IMAGE.I_BACKBUTTON)!!,
            width.toInt(),
            height.toInt(),
            true
        )
        bmpPress = Bitmap.createScaledBitmap(
            ImageManager.Instance.GetImage(IMAGE.I_BACKBUTTON_PRESSED)!!,
            width.toInt(),
            height.toInt(),
            true
        )
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
        StateManager.Instance.ChangeState(nextState)
    }

    companion object {
        fun Create(
            xPos: Float,
            yPos: Float,
            _width: Float,
            _height: Float,
            _nextState: String?
        ): BackEntity {
            val result = BackEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.width = _width
            result.height = _height
            result.nextState = _nextState
            EntityManager.Instance.AddEntity(result)
            return result
        }
    }
}
