package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;

public class PlayerEntity implements EntityBase, Collidable
{
    //Init any variables here
    protected static final String TAG = null;
    private Bitmap bmp = null;
    boolean isDone = false;
    private int ScreenWidth,ScreenHeight;
    private Vector2 Pos = new Vector2();
    private Vector2 Vel = new Vector2();
    private float width,height,radius;
    private float MAX_VEL;
    private float MOVE_SPEED;
    private float DeathDelay;

    private float ShootCounterMain;
    private float ShootCounterSubLeft;
    private float ShootCounterSubRight;


    private Sprite spritesheet = null;
    private int sheetRow,sheetCol;

    @Override
    public boolean IsDone() {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.GAMEOBJECTS_LAYER;
    }

    @Override
    public void Init(SurfaceView _view) {
        DeathDelay = 2;
        // Define anything you need to use here
        sheetRow = 1;
        sheetCol = 3;
        spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_spaceship),sheetRow,sheetCol,12);
        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_spaceship);

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;;

        width = spritesheet.GetWidth();
        height = spritesheet.GetHeight();
        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;

        Pos.x = ScreenWidth * 0.5f;
        Pos.y = ScreenHeight - (height*2);
        Vel.x = 0;
        Vel.y = 0;

        MAX_VEL = 600;
        MOVE_SPEED = 1600;

        ShootCounterMain = 0;
        ShootCounterSubLeft = 0;
        ShootCounterSubRight = 0;

        if (PlayerInfo.Instance.GetMainWeapon() != null)
            ShootCounterMain = PlayerInfo.Instance.GetMainWeapon().GetFireRate();

        if (PlayerInfo.Instance.GetSubWeaponLeft() != null)
            ShootCounterSubLeft = PlayerInfo.Instance.GetSubWeaponLeft().GetFireRate();;

        if (PlayerInfo.Instance.GetSubWeaponRight() != null)
            ShootCounterSubRight = PlayerInfo.Instance.GetSubWeaponRight().GetFireRate();
    }

    @Override
    public void Update(float _dt)
    {
        //Health
        if (PlayerInfo.Instance.GetHealth() <= 0)
        {
            PlayerInfo.Instance.SetHealth(0);
            DeathDelay -= _dt;
            if (DeathDelay <= 0)
            {
                GameSystem.Instance.AddHighScore(new HighScore(PlayerInfo.Instance.GetScore(),"PLAYER"));
                StateManager.Instance.ChangeState("GameOver");
                SetIsDone(true);
            }
        }

        if (PlayerInfo.Instance.GetHealth() <= 0)
            return;

        //Movement
        Pos.PlusEqual(Vel, _dt);

        //Out Of Bounds
        if (Pos.x - (width*0.5f)  < 0)
        {
            Pos.x = 0 + (width*0.5f);
            Vel.x = 0;
        }
        else if (Pos.x + (width*0.5f) > ScreenWidth)
        {
            Pos.x = ScreenWidth - (width*0.5f);
            Vel.x = 0;
        }

        if (Pos.y - (height*0.5f)  < 0)
        {
            Pos.y = 0 + (height*0.5f);
            Vel.y = 0;
        }
        else if (Pos.y + (height*0.5f) > ScreenHeight)
        {
            Pos.y = ScreenHeight- (height*0.5f);
            Vel.y = 0;
        }

        //Limit Vel
        if (Vel.x > MAX_VEL)
            Vel.x = MAX_VEL;
        else if (Vel.x < -MAX_VEL)
            Vel.x = -MAX_VEL;

        if (Vel.y > MAX_VEL)
            Vel.y = MAX_VEL;
        else if (Vel.y < -MAX_VEL)
            Vel.y = -MAX_VEL;

        if (TouchManager.Instance.HasTouch())
        {
            Vector2 touchPos = new Vector2(TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY());
            Vector2 direction = touchPos.Minus(Pos).Normalized();
            Vel.PlusEqual(direction.Times(MOVE_SPEED*_dt));
        }
        else
        {
            if (Vel.x > MOVE_SPEED*_dt)
                Vel.x -= MOVE_SPEED*_dt;
            else if (Vel.x < -MOVE_SPEED*_dt)
                Vel.x += MOVE_SPEED*_dt;
            else
                Vel.x = 0;

            if (Vel.y > MOVE_SPEED*_dt)
                Vel.y -= MOVE_SPEED*_dt;
            else if (Vel.y < -MOVE_SPEED*_dt)
                Vel.y += MOVE_SPEED*_dt;
            else
                Vel.y = 0;
        }

        spritesheet.Update(_dt);

        ShootCounterMain -= _dt;
        ShootCounterSubLeft-= _dt;
        ShootCounterSubRight -= _dt;

        if (PlayerInfo.Instance.GetMainWeapon() != null && ShootCounterMain <= 0)
        {
            ShootCounterMain = PlayerInfo.Instance.GetMainWeapon().GetFireRate();
            if (PlayerInfo.Instance.GetMainWeapon().GetShootType() == SHOOTTYPE.S_STRAIGHT)
            {
                for (int i = 0; i < PlayerInfo.Instance.GetMainWeapon().GetShootAmount(); ++i)
                {
                    Projectile currProj = Projectile.Create(PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage(), Pos.x, Pos.y - (height * 0.5f));
                    currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f));

                    if (PlayerInfo.Instance.GetMainWeapon().GetShootAmount() % 2 == 0)
                        currProj.SetPosX(currProj.GetPosX() + i * currProj.GetWidth() - ((PlayerInfo.Instance.GetMainWeapon().GetShootAmount() / 2) - 1) * (currProj.GetWidth()) - (currProj.GetWidth() * 0.5f));
                    else
                        currProj.SetPosX(currProj.GetPosX() + i * currProj.GetWidth() - (PlayerInfo.Instance.GetMainWeapon().GetShootAmount() / 2) * (currProj.GetWidth()));
                }
            }
            else if (PlayerInfo.Instance.GetMainWeapon().GetShootType() == SHOOTTYPE.S_SPREAD)
            {
                for (int i = 0; i < PlayerInfo.Instance.GetMainWeapon().GetShootAmount(); ++i)
                {
                    Projectile currProj = Projectile.Create(PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage(), Pos.x, Pos.y - (height * 0.5f));
                    currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f));

                    if (PlayerInfo.Instance.GetMainWeapon().GetShootAmount() % 2 == 0)
                    {
                        int multiply = PlayerInfo.Instance.GetMainWeapon().GetShootAmount() / 2;
                        int spread = 100;
                        int currentSpread = 0;

                        if (i < multiply)
                            currentSpread = ((multiply - i) * -spread) + (spread/2);
                        else
                            currentSpread = (((i+1) - multiply) * spread) - (spread/2);

                        currProj.SetVelX(currentSpread);
                    }
                    else
                    {
                        int multiply = (PlayerInfo.Instance.GetMainWeapon().GetShootAmount() / 2);
                        int spread = 100;

                        int currentSpread = 0;

                        if (i < multiply)
                            currentSpread = ((multiply - i) * -spread);
                        else
                            currentSpread = ((i - multiply) * spread);

                        currProj.SetVelX(currentSpread);
                    }
                }
            }
        }

        if (PlayerInfo.Instance.GetSubWeaponLeft() != null && ShootCounterSubLeft <= 0)
        {

        }

        if (PlayerInfo.Instance.GetSubWeaponRight() != null && ShootCounterSubRight <= 0)
        {

        }

        PlayerInfo.Instance.SetPos(Pos);
    }

    @Override
    public void Render(Canvas _canvas)
    {
        if (PlayerInfo.Instance.GetHealth() <= 0)
            return;

        spritesheet.Render(_canvas,(int)Pos.x,(int)Pos.y);
    }

    public static PlayerEntity Create()
    {
        PlayerEntity result = new PlayerEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    @Override
    public String GetType() {
        return "PlayerEntity";
    }

    @Override
    public float GetPosX() {
        return Pos.x;
    }

    @Override
    public float GetPosY() {
        return Pos.y;
    }

    @Override
    public float GetRadius()
    {
        return radius;
    }

    @Override
    public float GetWidth()
    {
        return width;
    }

    @Override
    public float GetHeight()
    {
        return height;
    }

    @Override
    public float GetDamage(){return 0;}

    @Override
    public void OnHit(Collidable _other)
    {
        if (PlayerInfo.Instance.GetHealth() <= 0)
            return;

        if (_other.GetType() == "EnemyEntity")
        {
            if (!_other.GetDead())
            {
                VibrateManager.Instance.startVibrate();
                PlayerInfo.Instance.MinusHealth(_other.GetDamage());
            }
        }
        else if (_other.GetType() == "EnemyBullet")
        {
            VibrateManager.Instance.startVibrate();
            PlayerInfo.Instance.MinusHealth(_other.GetDamage());
        }
    }

    @Override
    public boolean GetDead()
    {
        if (PlayerInfo.Instance.GetHealth() <= 0)
            return true;
        return false;
    }
}

