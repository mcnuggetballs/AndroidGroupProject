package com.fishweeb.practical;

public class HighScore
{
    private int Score;
    private String Name;

    HighScore(int _score,String _name)
    {
        Score = _score;
        Name = _name;
    }

    void SetScore(int _score)
    {
        Score = _score;
    }

    void SetName(String _name)
    {
        Name = _name;
    }

    int GetScore()
    {
        return Score;
    }

    String GetName()
    {
        return Name;
    }
}
