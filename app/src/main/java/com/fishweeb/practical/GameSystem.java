package com.fishweeb.practical;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class GameSystem
{
    protected static final String TAG = null;

    public final static GameSystem Instance = new GameSystem();

    public static final String SHARED_PREF_ID = "GameSaveFile";

    private LinkedList<HighScore> HighScores = null;

    private float m_speed;

    public float m_bubbleTimer;

    private boolean isPaused = false;
    SharedPreferences sharedPref = null;
    SharedPreferences.Editor editor = null;
    Vector2 ScreenScale; //Screen width and height

    public LinkedList<HighScore> GetHighScores()
    {
        return HighScores;
    }

    public void AddHighScore(HighScore newHighScore)
    {
        HighScores.push(newHighScore);
        Collections.sort(HighScores,new Comparator<HighScore>()
                {
                    public int compare(HighScore h1,HighScore h2)
                    {
                        return (int)(h2.GetScore() - h1.GetScore());
                    }
                }
        );
    }

    private GameSystem()
    {
    }

    public void Update(float _deltaTime)
    {
    }

    public void Init(SurfaceView _view)
    {
        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenScale = new Vector2(metrics.widthPixels,metrics.heightPixels);

        sharedPref = GamePage.Instance.getSharedPreferences(SHARED_PREF_ID,0);

        if (HighScores == null)
        {
            HighScores = new LinkedList<HighScore>();
        }

        StateManager.Instance.AddState(new IntroState());
        StateManager.Instance.AddState(new MainMenuState());
        StateManager.Instance.AddState(new ShopState());
        StateManager.Instance.AddState(new GarageState());
        StateManager.Instance.AddState(new OptionsState());
        StateManager.Instance.AddState(new ScoresState());
        StateManager.Instance.AddState(new MainGameState());
        StateManager.Instance.AddState(new GameOverState());
        StateManager.Instance.AddState(new scorepage());

        GameSystem.Instance.AddHighScore(new HighScore(GetIntFromSave("highscore1_score"),GetStringFromSave("highscore1_name")));
        GameSystem.Instance.AddHighScore(new HighScore(GetIntFromSave("highscore2_score"),GetStringFromSave("highscore2_name")));
        GameSystem.Instance.AddHighScore(new HighScore(GetIntFromSave("highscore3_score"),GetStringFromSave("highscore3_name")));
        GameSystem.Instance.AddHighScore(new HighScore(GetIntFromSave("highscore4_score"),GetStringFromSave("highscore4_name")));
        GameSystem.Instance.AddHighScore(new HighScore(GetIntFromSave("highscore5_score"),GetStringFromSave("highscore5_name")));

        m_bubbleTimer = 0;
        m_speed = 1;
    }

    public boolean GetIsPaused()
    {
        return isPaused;
    }

    // Week 9
    public void SetIsPaused(boolean _newPause)
    {
        isPaused = _newPause;
    }

    public void SetGameSpeed(float _value)
    {
        m_speed = _value;
    }

    public float GetGameSpeed()
    {
        return m_speed;
    }

    public void SaveEditBegin()
    {
        // Safety check, only allow if not already editing
        if (editor != null)
            return;

        // Start the editing
        editor = sharedPref.edit();
    }

    public void SaveEditEnd()
    {
        // Check if has editor
        if (editor == null)
            return;

        editor.commit();
        editor = null; // Safety to ensure other functions will fail once commit done
    }

    public void SetIntInSave(String _key, int _value)
    {
        if (editor == null)
            return;

        editor.putInt(_key, _value);
    }

    public void SetFloatInSave(String _key, float _value)
    {
        if (editor == null)
            return;

        editor.putFloat(_key, _value);
    }

    public void SetBoolInSave(String _key, boolean _value)
    {
        if (editor == null)
            return;

        editor.putBoolean(_key, _value);
    }

    public void SetStringInSave(String _key, String _value)
    {
        if (editor == null)
            return;

        editor.putString(_key, _value);
    }

    public int GetIntFromSave(String _key)
    {
        return sharedPref.getInt(_key, 0);
    }

    public boolean GetBoolFromSave(String _key)
    {
        return sharedPref.getBoolean(_key,false);
    }

    public float GetFloatFromSave(String _key)
    {
        return sharedPref.getFloat(_key, 0.f);
    }

    public String GetStringFromSave(String _key)
    {
        return sharedPref.getString(_key, "NULL");
    }


    public Vector2 GetScreenScale()
    {
        return ScreenScale;
    }
}
