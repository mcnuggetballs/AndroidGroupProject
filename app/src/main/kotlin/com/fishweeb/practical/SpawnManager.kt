package com.fishweeb.practical

import android.view.SurfaceView

class SpawnManager {
    var enemySpawnTimer: Float = 0f

    var depthDifficultyOffset: Float = 0f
    var depthDifficulty: Float = 0f
    var depthDifficultyIncrease: Float = 0f
    var depth: Float = 0f
    var depthIncreaseSpeed: Float = 0f
    var bossTime: Boolean = false
    var FishSpawn_Depth: Float = 300f
    var FrogSpawn_Depth: Float = 300f
    var SnakeSpawn_Depth: Float = 2000f
    var MantisSpawn_Depth: Float = 1500f
    var ToxicSpawn_Depth: Float = 300f
    var SquidSpawn_Depth: Float = 1000f

    var Boss1Spawn_Depth: Float = 12000f
    var bossCounter: Float = 0f

    var Boss1Spawned: Boolean = false

    private var _view: SurfaceView? = null

    fun Init(_view: SurfaceView?) {
        depthDifficultyOffset = 0f
        depthDifficulty = 3000f
        depthDifficultyIncrease = 3000f
        depth = 0f
        depthIncreaseSpeed = 100f
        bossCounter = 0f
        enemySpawnTimer = 0f
        bossTime = false
        this._view = _view
        Boss1Spawned = false
    }

    fun SpawnFish() {
        if (depth < FishSpawn_Depth) return

        for (i in 0..1) {
            if (Math.random() * 100 <= 30 + depthDifficultyOffset) EnemyEntity.Companion.Create(
                30f,
                ResourceManager.Companion.Instance.GetBitmap(R.drawable.sprite_bluefish),
                3,
                4
            )
        }
    }

    fun SpawnSnake() {
        if (depth < SnakeSpawn_Depth) return

        for (i in 0..1) {
            if (Math.random() * 100 <= 10 + depthDifficultyOffset) SnakeEntity.Companion.Create()
        }
    }

    fun SpawnMantis() {
        if (depth < MantisSpawn_Depth) return

        if (Math.random() * 100 <= 15 + depthDifficultyOffset) MantisEntity.Companion.Create()
    }

    fun SpawnToxic() {
        if (depth < ToxicSpawn_Depth) return

        if (Math.random() * 100 <= 2 + depthDifficultyOffset) LitterEntity.Companion.Create()
    }

    fun SpawnFrog() {
        if (depth < FrogSpawn_Depth) return

        if (Math.random() * 100 <= 30 + depthDifficultyOffset) FrogEntity.Companion.Create()
    }

    fun SpawnSquid() {
        if (depth < SquidSpawn_Depth) return

        if (Math.random() * 100 <= 15 + depthDifficultyOffset) SquidEntity.Companion.Create()
    }

    fun SpawnBoss1() {
        BossCrabEntity.Companion.Create()
    }

    fun Update(_dt: Float) {
        //Test
        enemySpawnTimer -= _dt
        if (enemySpawnTimer <= 0) {
            enemySpawnTimer = 2f

            SpawnFish()
            if (!bossTime) {
                SpawnSnake()
                SpawnToxic()
                SpawnMantis()
                SpawnFrog()
                SpawnSquid()
            }
        }


        //Test
        bossCounter += _dt * depthIncreaseSpeed
        if (bossCounter > Boss1Spawn_Depth) {
            SpawnBoss1()
            bossCounter = 0f
        }

        depth += _dt * depthIncreaseSpeed
        if (depth > depthDifficulty) {
            depthDifficultyOffset += 1f
            depthDifficulty += depthDifficultyIncrease
        }
    }

    companion object {
        var Instance: SpawnManager = SpawnManager()
    }
}
