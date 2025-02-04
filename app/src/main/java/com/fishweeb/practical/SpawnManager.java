package com.fishweeb.practical;

import android.graphics.BitmapFactory;
import android.view.SurfaceView;

public class SpawnManager
{
    float enemySpawnTimer;

    float depthDifficultyOffset;
    float depthDifficulty;
    float depthDifficultyIncrease;
    float depth;
    float depthIncreaseSpeed;
    boolean bossTime;
    static SpawnManager Instance = new SpawnManager();

    float FishSpawn_Depth = 300;
    float FrogSpawn_Depth = 300;
    float SnakeSpawn_Depth = 2000;
    float MantisSpawn_Depth = 1500;
    float ToxicSpawn_Depth = 300;
    float SquidSpawn_Depth = 1000;

    float Boss1Spawn_Depth = 12000;
    float bossCounter = 0;

    boolean Boss1Spawned;

    private SurfaceView _view;

    void Init(SurfaceView _view)
    {
        depthDifficultyOffset = 0;
        depthDifficulty = 3000;
        depthDifficultyIncrease = 3000;
        depth = 0;
        depthIncreaseSpeed = 100;
        bossCounter = 0;
        enemySpawnTimer = 0;
        bossTime = false;
        this._view = _view;
        Boss1Spawned = false;
    }

    void SpawnFish()
    {
        if (depth < FishSpawn_Depth)
            return;

        for (int i = 0; i < 2; ++i)
        {
            if (Math.random() * 100 <= 30 + depthDifficultyOffset)
                EnemyEntity.Create(30, ResourceManager.Instance.GetBitmap(R.drawable.sprite_bluefish), 3, 4);
        }
    }

    void SpawnSnake()
    {
        if (depth < SnakeSpawn_Depth)
            return;

        for (int i = 0; i < 2; ++i)
        {
            if (Math.random() * 100 <= 10 + depthDifficultyOffset)
                SnakeEntity.Create();
        }
    }

    void SpawnMantis()
    {
        if (depth < MantisSpawn_Depth)
            return;

        if (Math.random() * 100 <= 15 + depthDifficultyOffset)
            MantisEntity.Create();
    }

    void SpawnToxic()
    {
        if (depth<ToxicSpawn_Depth)
            return;

        if (Math.random() * 100 <= 2 + depthDifficultyOffset)
            LitterEntity.Create();
    }

    void SpawnFrog()
    {
        if (depth < FrogSpawn_Depth)
            return;

        if (Math.random() * 100 <= 30 + depthDifficultyOffset)
            FrogEntity.Create();
    }

    void SpawnSquid()
    {
        if (depth < SquidSpawn_Depth)
            return;

        if (Math.random() * 100 <= 15 + depthDifficultyOffset)
            SquidEntity.Create();
    }

    void SpawnBoss1()
    {
        BossCrabEntity.Create();
    }

    void Update(float _dt)
    {
        //Test
        enemySpawnTimer-= _dt;
        if (enemySpawnTimer <= 0)
        {
            enemySpawnTimer = 2;

            SpawnFish();
            if (!bossTime)
            {
                SpawnSnake();
                SpawnToxic();
                SpawnMantis();
                SpawnFrog();
                SpawnSquid();
            }
        }
        //Test


        bossCounter += _dt * depthIncreaseSpeed;
        if (bossCounter > Boss1Spawn_Depth)
        {
            SpawnBoss1();
            bossCounter = 0;
        }

        depth += _dt * depthIncreaseSpeed;
        if (depth > depthDifficulty)
        {
            depthDifficultyOffset += 1.f;
            depthDifficulty += depthDifficultyIncrease;
        }
    }
}
