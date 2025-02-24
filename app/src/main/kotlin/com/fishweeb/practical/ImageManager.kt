package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.SurfaceView

enum class IMAGE
    (private val value: Int) {
    I_POWERUP_SPREAD(0),
    I_POWERUP_STRAIGHT(1),
    I_MENUBACKGROUND(2),
    I_LOGO(3),
    I_BUBBLE(4),
    I_BACKBUTTON(5),
    I_BACKBUTTON_PRESSED(6),
    I_BUTTON_ONN(7),
    I_BUTTON_OFF(8),
    I_NUM(9);

    fun GetValue(): Int {
        return value
    }
}

class ImageManager {
    private val Images =
        arrayOfNulls<Bitmap>(IMAGE.I_NUM.GetValue())
    private var crabTest: Sprite? = null

    fun Init(_view: SurfaceView) {
        Images[IMAGE.I_POWERUP_SPREAD.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.powerup_duplicate_spread)
        Images[IMAGE.I_POWERUP_STRAIGHT.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.powerup_duplicate_straight)
        Images[IMAGE.I_MENUBACKGROUND.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.backgnd3)
        Images[IMAGE.I_LOGO.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.logo)
        Images[IMAGE.I_BUBBLE.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.bubble)
        Images[IMAGE.I_BACKBUTTON.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.back_normal)
        Images[IMAGE.I_BACKBUTTON_PRESSED.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.back_pressed)
        Images[IMAGE.I_BUTTON_OFF.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.offswitch)
        Images[IMAGE.I_BUTTON_ONN.GetValue()] =
            BitmapFactory.decodeResource(_view.resources, R.drawable.onnswitch)

        val sheetRow = 3
        val sheetCol = 17
        crabTest = Sprite(
            BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_crab),
            sheetRow,
            sheetCol,
            2
        )
    }

    fun GetImage(name: IMAGE): Bitmap? {
        return Images[name.GetValue()]
    }

    fun GetCrabSpriteSheet(): Sprite? {
        return crabTest
    }

    companion object {
        var Instance: ImageManager = ImageManager()
    }
}
