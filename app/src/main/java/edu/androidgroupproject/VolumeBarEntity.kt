package edu.androidgroupproject

import android.graphics.Color

class VolumeBarEntity : SeekBarEntity() {
    override fun Update(_dt: Float) {
        if (TouchManager.Companion.Instance.HasTouch()) {
            if (Collision.AABBToAABB(
                    Pos.x,
                    Pos.y,
                    width,
                    height,
                    TouchManager.Companion.Instance.GetPosX().toFloat(),
                    TouchManager.Companion.Instance.GetPosY().toFloat(),
                    1f,
                    1f
                )
            ) {
                dragging = true
            }
        } else {
            dragging = false
        }

        if (dragging == true) {
            val xOffset: Float = TouchManager.Companion.Instance.GetPosX() - xStart
            if (xOffset > 0 && xOffset < width) SliderPos.x =
                TouchManager.Companion.Instance.GetPosX().toFloat()
            else if (xOffset <= 0) {
                SliderPos.x = Pos.x - width * 0.5f
            } else if (xOffset >= width) {
                SliderPos.x = Pos.x + width * 0.5f
            }
            val xOffset2 = SliderPos.x - xStart
            val value = ((xOffset2 / width) * 100).toInt()
            SettingsManager.Companion.Instance.SetSoundVolume(value)
        }
    }

    companion object {
        fun Create(_xPos: Float, _yPos: Float, barWidth: Float, barHeight: Float): VolumeBarEntity {
            val result = VolumeBarEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            result.Pos.x = _xPos
            result.Pos.y = _yPos
            result.width = barWidth
            result.height = barHeight
            result.sliderWidth = barWidth * 0.1f
            result.sliderHeight = barHeight
            result.xStart = result.Pos.x - barWidth * 0.5f
            result.textPaint.color = Color.BLACK
            result.textPaint.isFakeBoldText = true
            result.textPaint.textSize = barHeight
            result.textPaint.getTextBounds("100", 0, 2, result.textBounds)

            result.SliderPos.x =
                result.xStart + (SettingsManager.Companion.Instance.GetSoundVolume() / 100f) * barWidth
            result.SliderPos.y = _yPos
            return result
        }
    }
}
