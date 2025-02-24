package com.fishweeb.practical

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
        sound_volume = GameSystem.Companion.Instance.GetIntFromSave("volume")
        fps_show = GameSystem.Companion.Instance.GetBoolFromSave("showfps")
    }

    fun GetSoundVolume(): Int {
        return sound_volume
    }

    fun SetSoundVolume(_value: Int) {
        sound_volume = _value
        GameSystem.Companion.Instance.SaveEditBegin()
        GameSystem.Companion.Instance.SetIntInSave("volume", _value)
        GameSystem.Companion.Instance.SaveEditEnd()
    }

    fun SetFpsShow(_value: Boolean) {
        fps_show = _value
        GameSystem.Companion.Instance.SaveEditBegin()
        GameSystem.Companion.Instance.SetBoolInSave("showfps", _value)
        GameSystem.Companion.Instance.SaveEditEnd()
    }

    fun GetFpsShow(): Boolean {
        return fps_show
    }

    companion object {
        var Instance: SettingsManager = SettingsManager()
    }
}
