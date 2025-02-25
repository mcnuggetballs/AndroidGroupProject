package edu.androidgroupproject

import android.graphics.Bitmap
import android.view.SurfaceView

class SwitchButtonEntity : DynamicButtonEntity() {
    private var bmpOff: Bitmap? = null
    private var bmpOnn: Bitmap? = null
    private var type: SETTINGTYPE? = null
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        if (type == SETTINGTYPE.ST_FPSSHOW) {
            bmp = if (SettingsManager.Companion.Instance.GetFpsShow()) {
                bmpOnn
            } else {
                bmpOff
            }
        }

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    override fun OnClickFunction() {
    }

    override fun OffClickFunction() {
        if (type == SETTINGTYPE.ST_FPSSHOW) {
            SettingsManager.Companion.Instance.SetFpsShow(!SettingsManager.Companion.Instance.GetFpsShow())
            bmp = if (SettingsManager.Companion.Instance.GetFpsShow()) {
                bmpOnn
            } else {
                bmpOff
            }
        }
    }

    companion object {
        fun Create(
            xPos: Float,
            yPos: Float,
            _width: Float,
            _height: Float,
            buttonImageOff: Bitmap?,
            buttonImageOnn: Bitmap?,
            _settingtype: SETTINGTYPE?
        ): SwitchButtonEntity {
            val result = SwitchButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.width = _width
            result.height = _height
            result.bmpOff = Bitmap.createScaledBitmap(
                buttonImageOff!!,
                result.width.toInt(),
                result.height.toInt(),
                true
            )
            result.bmpOnn = Bitmap.createScaledBitmap(
                buttonImageOnn!!,
                result.width.toInt(),
                result.height.toInt(),
                true
            )
            result.type = _settingtype
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
