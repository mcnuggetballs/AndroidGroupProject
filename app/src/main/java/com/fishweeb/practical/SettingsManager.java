package com.fishweeb.practical;

enum SETTINGTYPE
{
    ST_FPSSHOW,
    ST_NUM;
    private static final int size = SETTINGTYPE.values().length;

    public int GetSize()
    {
        return size;
    }
}

public class SettingsManager
{
    static SettingsManager Instance = new SettingsManager();
    private int sound_volume;
    private boolean fps_show;

    private SettingsManager()
    {
        sound_volume = GameSystem.Instance.GetIntFromSave("volume");
        fps_show = GameSystem.Instance.GetBoolFromSave("showfps");
    }

    public int GetSoundVolume()
    {
        return sound_volume;
    }

    public void SetSoundVolume(int _value)
    {
        sound_volume = _value;
        GameSystem.Instance.SaveEditBegin();
        GameSystem.Instance.SetIntInSave("volume",_value);
        GameSystem.Instance.SaveEditEnd();
    }

    public void SetFpsShow(boolean _value)
    {
        fps_show = _value;
        GameSystem.Instance.SaveEditBegin();
        GameSystem.Instance.SetBoolInSave("showfps",_value);
        GameSystem.Instance.SaveEditEnd();
    }

    public boolean GetFpsShow()
    {
        return fps_show;
    }

}
