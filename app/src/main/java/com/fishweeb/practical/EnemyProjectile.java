package com.fishweeb.practical;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class EnemyProjectile extends Projectile
{
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.enemybullet);

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();

        width = bmp.getWidth();
        height = bmp.getHeight();

        //width = 30;
        //height = 30;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        velocity.y = 300;

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;
    }

    public static EnemyProjectile Create(float Damage,float xPos,float yPos)
    {
        EnemyProjectile result = new EnemyProjectile();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.Damage = Damage;
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    @Override
    public String GetType() {
        return "EnemyBullet";
    }

    @Override
    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == "PlayerEntity")
        {
            if (!_other.GetDead())
                SetIsDone(true);
        }
    }
}
