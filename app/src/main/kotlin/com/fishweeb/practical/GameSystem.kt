package com.fishweeb.practical

import android.content.SharedPreferences
import android.view.SurfaceView
import java.util.Collections
import java.util.LinkedList

class GameSystem
private constructor() {
    private var HighScores: LinkedList<HighScore>? = null

    private var m_speed = 0f

    var m_bubbleTimer: Float = 0f

    private var isPaused = false
    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var ScreenScale: Vector2? = null //Screen width and height

    fun GetHighScores(): LinkedList<HighScore>? {
        return HighScores
    }

    fun AddHighScore(newHighScore: HighScore) {
        HighScores!!.push(newHighScore)
        Collections.sort(
            HighScores
        ) { h1, h2 -> (h2.GetScore() - h1.GetScore()) }
    }

    fun Update(_deltaTime: Float) {
    }

    fun Init(_view: SurfaceView) {
        val metrics = _view.resources.displayMetrics
        ScreenScale = Vector2(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())

        sharedPref = GamePage.Companion.Instance!!.getSharedPreferences(SHARED_PREF_ID, 0)

        if (HighScores == null) {
            HighScores = LinkedList()
        }

        StateManager.Companion.Instance.AddState(IntroState())
        StateManager.Companion.Instance.AddState(MainMenuState())
        StateManager.Companion.Instance.AddState(ShopState())
        StateManager.Companion.Instance.AddState(GarageState())
        StateManager.Companion.Instance.AddState(OptionsState())
        StateManager.Companion.Instance.AddState(ScoresState())
        StateManager.Companion.Instance.AddState(MainGameState())
        StateManager.Companion.Instance.AddState(GameOverState())
        StateManager.Companion.Instance.AddState(scorepage())

        Instance.AddHighScore(
            HighScore(
                GetIntFromSave("highscore1_score"),
                GetStringFromSave("highscore1_name")
            )
        )
        Instance.AddHighScore(
            HighScore(
                GetIntFromSave("highscore2_score"),
                GetStringFromSave("highscore2_name")
            )
        )
        Instance.AddHighScore(
            HighScore(
                GetIntFromSave("highscore3_score"),
                GetStringFromSave("highscore3_name")
            )
        )
        Instance.AddHighScore(
            HighScore(
                GetIntFromSave("highscore4_score"),
                GetStringFromSave("highscore4_name")
            )
        )
        Instance.AddHighScore(
            HighScore(
                GetIntFromSave("highscore5_score"),
                GetStringFromSave("highscore5_name")
            )
        )

        m_bubbleTimer = 0f
        m_speed = 1f
    }

    fun GetIsPaused(): Boolean {
        return isPaused
    }

    // Week 9
    fun SetIsPaused(_newPause: Boolean) {
        isPaused = _newPause
    }

    fun SetGameSpeed(_value: Float) {
        m_speed = _value
    }

    fun GetGameSpeed(): Float {
        return m_speed
    }

    fun SaveEditBegin() {
        // Safety check, only allow if not already editing
        if (editor != null) return

        // Start the editing
        editor = sharedPref!!.edit()
    }

    fun SaveEditEnd() {
        // Check if has editor
        if (editor == null) return

        editor!!.commit()
        editor = null // Safety to ensure other functions will fail once commit done
    }

    fun SetIntInSave(_key: String?, _value: Int) {
        if (editor == null) return

        editor!!.putInt(_key, _value)
    }

    fun SetFloatInSave(_key: String?, _value: Float) {
        if (editor == null) return

        editor!!.putFloat(_key, _value)
    }

    fun SetBoolInSave(_key: String?, _value: Boolean) {
        if (editor == null) return

        editor!!.putBoolean(_key, _value)
    }

    fun SetStringInSave(_key: String?, _value: String?) {
        if (editor == null) return

        editor!!.putString(_key, _value)
    }

    fun GetIntFromSave(_key: String?): Int {
        return sharedPref!!.getInt(_key, 0)
    }

    fun GetBoolFromSave(_key: String?): Boolean {
        return sharedPref!!.getBoolean(_key, false)
    }

    fun GetFloatFromSave(_key: String?): Float {
        return sharedPref!!.getFloat(_key, 0f)
    }

    fun GetStringFromSave(_key: String?): String {
        return sharedPref!!.getString(_key, "NULL")!!
    }


    fun GetScreenScale(): Vector2? {
        return ScreenScale
    }

    companion object {
        protected val TAG: String? = null

        val Instance: GameSystem = GameSystem()

        const val SHARED_PREF_ID: String = "GameSaveFile"
    }
}
