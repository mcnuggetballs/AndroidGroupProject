package com.fishweeb.practical;

import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;

public class AudioManager
{
    public final static AudioManager Instance = new AudioManager();

    private SurfaceView view = null;
    private HashMap<Integer,MediaPlayer> audioMap = new HashMap<Integer,MediaPlayer>();

    private AudioManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    public void PlayAudio(int _id,float _volume)
    {
        if (audioMap.containsKey(_id))
        {
            MediaPlayer curr = audioMap.get(_id);
            curr.seekTo(0);
            curr.setVolume(_volume,_volume);
            curr.start();
        }
        else
        {
            MediaPlayer newAudio = MediaPlayer.create(view.getContext(),_id);
            audioMap.put(_id,newAudio);
            PlayAudio(_id,_volume);
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

    public boolean IsPlaying(int _id)
    {
        if (!audioMap.containsKey(_id))
            return false;
        return audioMap.get(_id).isPlaying();
    }

    public void Exit()
    {
        for (HashMap.Entry<Integer,MediaPlayer> entry:audioMap.entrySet())
        {
            entry.getValue().stop();
            entry.getValue().reset();
            entry.getValue().release();
        }
        audioMap.clear();
    }

    public void StopAudio(int _id)
    {
        MediaPlayer newAudio = audioMap.get(_id);
        newAudio.pause();
    }
}
