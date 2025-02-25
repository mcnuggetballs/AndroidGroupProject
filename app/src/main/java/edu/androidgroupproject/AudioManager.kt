package edu.androidgroupproject

import android.media.MediaPlayer
import android.view.SurfaceView

object AudioManager {
    private var view: SurfaceView? = null
    private val audioMap = HashMap<Int, MediaPlayer>()

    fun init(_view: SurfaceView?) {
        view = _view
    }

    fun playAudio(_id: Int, _volume: Float, loop: Boolean = false) {
        audioMap[_id]?.let { mediaPlayer ->
            mediaPlayer.seekTo(0)
            mediaPlayer.setVolume(_volume, _volume)
            mediaPlayer.isLooping = loop  // ✅ Enable looping if requested
            mediaPlayer.start()
        } ?: run {
            view?.context?.let { context ->
                val newAudio = MediaPlayer.create(context, _id)
                newAudio.setVolume(_volume, _volume)
                newAudio.isLooping = loop  // ✅ Set looping here
                newAudio.start()
                audioMap[_id] = newAudio
            }
        }
    }

    fun isPlaying(_id: Int): Boolean {
        return audioMap[_id]?.isPlaying ?: false
    }

    fun stopAudio(_id: Int) {
        audioMap[_id]?.let { mediaPlayer ->
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)  // Reset to the beginning
        }
    }

    fun exit() {
        audioMap.values.forEach { mediaPlayer ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }
        audioMap.clear()
    }
}
