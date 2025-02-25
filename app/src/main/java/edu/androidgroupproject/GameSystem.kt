package com.edu.androidgroupproject

import android.content.SharedPreferences
import android.view.SurfaceView
import edu.androidgroupproject.GameOverState
import edu.androidgroupproject.GamePage
import edu.androidgroupproject.GarageState
import edu.androidgroupproject.HighScore
import edu.androidgroupproject.IntroState
import edu.androidgroupproject.MainGameState
import edu.androidgroupproject.MainMenuState
import edu.androidgroupproject.OptionsState
import edu.androidgroupproject.ScoresState
import edu.androidgroupproject.ShopState
import edu.androidgroupproject.StateManager
import edu.androidgroupproject.Vector2

class GameSystem private constructor() {
    private val HighScores: MutableList<HighScore> = mutableListOf()
    private var m_speed = 0f
    var m_bubbleTimer: Float = 0f
    private var isPaused = false
    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var ScreenScale: Vector2? = null // Screen width and height

    fun GetHighScores(): List<HighScore> {
        return HighScores
    }

    fun AddHighScore(newHighScore: HighScore) {
        HighScores.add(newHighScore)
        HighScores.sortWith(compareByDescending { it.GetScore() }) // Sort by highest score
    }

    fun Update(_deltaTime: Float) {
    }

    fun Init(_view: SurfaceView) {
        val metrics = _view.resources.displayMetrics
        ScreenScale = Vector2(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())

        sharedPref = GamePage.Companion.Instance!!.getSharedPreferences(SHARED_PREF_ID, 0)

        StateManager.Companion.Instance.apply {
            AddState(IntroState())
            AddState(MainMenuState())
            AddState(ShopState())
            AddState(GarageState())
            AddState(OptionsState())
            AddState(ScoresState())
            AddState(MainGameState())
            AddState(GameOverState())
        }

        // Load high scores from saved data
        repeat(5) { index ->
            val scoreKey = "highscore${index + 1}_score"
            val nameKey = "highscore${index + 1}_name"
            AddHighScore(HighScore(GetIntFromSave(scoreKey), GetStringFromSave(nameKey)))
        }

        m_bubbleTimer = 0f
        m_speed = 1f
    }

    fun GetIsPaused(): Boolean = isPaused

    fun SetIsPaused(_newPause: Boolean) {
        isPaused = _newPause
    }

    fun SetGameSpeed(_value: Float) {
        m_speed = _value
    }

    fun GetGameSpeed(): Float = m_speed

    fun SaveEditBegin() {
        if (editor == null) {
            editor = sharedPref?.edit()
        }
    }

    fun SaveEditEnd() {
        editor?.apply()
        editor = null
    }

    fun SetIntInSave(_key: String, _value: Int) {
        editor?.putInt(_key, _value)
    }

    fun SetFloatInSave(_key: String, _value: Float) {
        editor?.putFloat(_key, _value)
    }

    fun SetBoolInSave(_key: String, _value: Boolean) {
        editor?.putBoolean(_key, _value)
    }

    fun SetStringInSave(_key: String, _value: String) {
        editor?.putString(_key, _value)
    }

    fun GetIntFromSave(_key: String): Int = sharedPref?.getInt(_key, 0) ?: 0

    fun GetBoolFromSave(_key: String): Boolean = sharedPref?.getBoolean(_key, false) ?: false

    fun GetFloatFromSave(_key: String): Float = sharedPref?.getFloat(_key, 0f) ?: 0f

    fun GetStringFromSave(_key: String): String = sharedPref?.getString(_key, "NULL") ?: "NULL"

    fun GetScreenScale(): Vector2? = ScreenScale

    companion object {
        val Instance: GameSystem = GameSystem()
        const val SHARED_PREF_ID: String = "GameSaveFile"
    }
}