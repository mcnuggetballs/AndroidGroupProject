package edu.androidgroupproject

class HighScore
internal constructor(private var Score: Int, private var Name: String) {
    fun SetScore(_score: Int) {
        Score = _score
    }

    fun SetName(_name: String) {
        Name = _name
    }

    fun GetScore(): Int {
        return Score
    }

    fun GetName(): String {
        return Name
    }
}
