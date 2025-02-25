package edu.androidgroupproject

import android.graphics.BitmapFactory
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
            if (Math.random() * 100 <= 10 + depthDifficultyOffset) SnakeEntity.Create()
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

        if (Math.random() * 100 <= 15 + depthDifficultyOffset) SquidEntity.Create()
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

    class SnakeEntity : EnemyEntity() {
        private var goingRight = false

        override fun Init(_view: SurfaceView) {
            // Define anything you need to use here
            goingRight = true

            health = 40f
            sheetRow = 3
            sheetCol = 3
            SheetInfectedStart = 1
            SheetInfectedEnd = 3
            SheetHitStart = 4
            SheetHitEnd = 6
            SheetNormalStart = 7
            SheetNormalEnd = 9
            bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_snake)
            spritesheet = Sprite(bmp, sheetRow, sheetCol, 12)
            spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

            damage = 5f
            score = 30
            gold = 13

            val metrics = _view.resources.displayMetrics
            ScreenWidth = metrics.widthPixels
            ScreenHeight = metrics.heightPixels



            width = spritesheet!!.GetWidth().toFloat()
            height = spritesheet!!.GetHeight().toFloat()

            radius = if (width > height) width * 0.5f
            else height * 0.5f

            Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
            Pos.y = -height
            Vel.x = Math.random().toFloat() * 360 - 180
            Vel.y = Math.random().toFloat() * 360 + 180
        }

        override fun Contrain() {
            //Out Of Bounds
            if (!dead) {
                if (Pos.x - (width * 0.5f) < 0) {
                    Pos.x = 0 + (width * 0.5f)
                    Vel.x = 0f
                } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
                    Pos.x = ScreenWidth - (width * 0.5f)
                    Vel.x = 0f
                }
            } else {
                if (Pos.x + (width * 0.5f) < 0) {
                    SetIsDone(true)
                } else if (Pos.x - (width * 0.5f) > ScreenWidth) {
                    SetIsDone(true)
                }
            }
        }

        override fun Update(_dt: Float) {
            //Check if dead
            DieFunction(_dt)


            //Movement
            Pos.PlusEqual(Vel, _dt)

            Contrain()

            if (!dead) {
                var direction = Vector2()
                direction = (PlayerInfo.Companion.Instance.GetPos().Minus(Pos)).Normalized()
                Vel.x += direction.x * 300 * _dt
            }


            if (Pos.y - height > ScreenHeight) {
                SetIsDone(true)
            }

            if (hit) {
                hitcounter -= _dt
                if (hitcounter <= 0) {
                    if (dead) spritesheet!!.ContinueAnimationFrames(
                        SheetNormalStart,
                        SheetNormalEnd
                    )
                    else spritesheet!!.ContinueAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

                    hit = false
                }
            }

            spritesheet!!.Update(_dt)
        }

        companion object {
            fun Create(): SnakeEntity {
                val result = SnakeEntity()
                EntityManager.Companion.Instance.AddEntity(result)
                return result
            }
        }
    }

    companion object {
        var Instance: SpawnManager = SpawnManager()
    }
}
