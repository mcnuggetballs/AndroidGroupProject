package edu.androidgroupproject

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.SurfaceView

class ResourceManager
private constructor() {
    private var res: Resources? = null
    private val resMap = HashMap<Int, Bitmap>()

    fun Init(_view: SurfaceView) {
        res = _view.resources
    }

    fun GetBitmap(_id: Int): Bitmap? {
        if (resMap.containsKey(_id)) return resMap[_id]

        val result = BitmapFactory.decodeResource(res, _id)
        resMap[_id] = result
        return result
    }

    companion object {
        val Instance: ResourceManager = ResourceManager()
    }
}