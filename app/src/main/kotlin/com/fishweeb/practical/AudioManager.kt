package com.fishweeb.practical

import android.media.MediaPlayer
import android.view.SurfaceView

class AudioManager
private constructor() {
    private var view: SurfaceView? = null
    private val audioMap = HashMap<Int, MediaPlayer>()

    fun Init(_view: SurfaceView?) {
        view = _view
    }

    fun PlayAudio(_id: Int, _volume: Float) {
        if (audioMap.containsKey(_id)) {
            val curr = audioMap[_id]
            curr!!.seekTo(0)
            curr.setVolume(_volume, _volume)
            curr.start()
        } else {
            val newAudio = MediaPlayer.create(view!!.context, _id)
            audioMap[_id] = newAudio
            PlayAudio(_id, _volume)
            //newAudio.start();
        }
    }

    /*
    public void PlayAudio(int _id,float _volume)
    {
        if (audioMap.containsKey(_id))
        {
            audioMap.get(_id).reset();;
            audioMap.get(_id).start();
        }

        MediaPlayer newAudio = MediaPlayer.create(view.getContext(),_id);
        audioMap.put(_id,newAudio);

        newAudio.setVolume(_volume,_volume);
        newAudio.start();
    }
    */
    fun IsPlaying(_id: Int): Boolean {
        if (!audioMap.containsKey(_id)) return false
        return audioMap[_id]!!.isPlaying
    }

    fun Exit() {
        for ((_, value) in audioMap) {
            value.stop()
            value.reset()
            value.release()
        }
        audioMap.clear()
    }

    fun StopAudio(_id: Int) {
        val newAudio = audioMap[_id]
        newAudio!!.pause()
    }

    companion object {
        @JvmField
        val Instance: AudioManager = AudioManager()
    }
}
