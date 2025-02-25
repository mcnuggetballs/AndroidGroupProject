package edu.androidgroupproject

import com.edu.androidgroupproject.GameSystem

enum class SETTINGTYPE {
    ST_FPSSHOW,
    ST_NUM;

    fun GetSize(): Int {
        return size
    }

    companion object {
        private val size = values().size
    }
}

class SettingsManager
private constructor() {
    private var sound_volume: Int
    private var fps_show: Boolean

    init {
        sound_volume = GameSystem.Instance.GetIntFromSave("volume")
        fps_show = GameSystem.Instance.GetBoolFromSave("showfps")
    }

    fun GetSoundVolume(): Int {
        return sound_volume
    }

    fun SetSoundVolume(_value: Int) {
        sound_volume = _value
        GameSystem.Instance.SaveEditBegin()
        GameSystem.Instance.SetIntInSave("volume", _value)
        GameSystem.Instance.SaveEditEnd()
    }

    fun SetFpsShow(_value: Boolean) {
        fps_show = _value
        GameSystem.Instance.SaveEditBegin()
        GameSystem.Instance.SetBoolInSave("showfps", _value)
        GameSystem.Instance.SaveEditEnd()
    }

    fun GetFpsShow(): Boolean {
        return fps_show
    }

    companion object {
        var Instance: SettingsManager = SettingsManager()
    }
}
