package com.fishweeb.practical;

public class PlayerInfo
{
    static PlayerInfo Instance = new PlayerInfo();
    private float MaxHealth;
    private float Health;
    private int Money;
    private float Depth;
    private float DepthIncreaseSpeed = 100;
    private Vector2 Pos = new Vector2();
    private int Score;
    private GunInfo[] Weapons;
    private PARTICLETYPE effecttype;

    private PlayerInfo()
    {
        Weapons = new GunInfo[3];
        for (int i = 0;i<3;++i)
        {
            Weapons[i] = null;
        }

        float LoadMaxHealth = GameSystem.Instance.GetFloatFromSave("maxhealth");
        if (LoadMaxHealth == 0)
        {
            LoadMaxHealth = 10;
        }
        int MainShootCount = GameSystem.Instance.GetIntFromSave("maingunshootcount");
        int MainDamage = GameSystem.Instance.GetIntFromSave("mainshootdamage");
        if (MainShootCount == 0)
        {
            MainShootCount = 1;
        }
        if (MainDamage == 0)
        {
            MainDamage = 5;
        }

        int particle = GameSystem.Instance.GetIntFromSave("effecttype");
        effecttype = PARTICLETYPE.values()[particle];
        if (particle == 0)
        {
            effecttype = PARTICLETYPE.P_BUBBLE;
        }

        Weapons[0] = new GunInfo();
        Weapons[0].SetFireRate(0.25f);
        Weapons[0].SetWeaponDamage(MainDamage);
        Weapons[0].SetShootAmount(MainShootCount);
        Weapons[0].SetShootType(SHOOTTYPE.S_STRAIGHT);

        MaxHealth = LoadMaxHealth;
        Health = MaxHealth;
        Depth = 0;
        Money = GameSystem.Instance.GetIntFromSave("money");
        Score = 0;
    }

    public void RestartGame()
    {
        Health = MaxHealth;
        Depth = 0;
        Score = 0;

        for (int i = 0;i<3;++i)
        {
            if (Weapons[i] != null)
            {
                Weapons[i].SetFireRatePercentage(100);
                Weapons[i].SetBonusShootAmount(0);
            }
        }
    }

    public void SetEffectType(PARTICLETYPE _type)
    {
        effecttype = _type;
        GameSystem.Instance.SaveEditBegin();
        GameSystem.Instance.SetIntInSave("effecttype",PlayerInfo.Instance.GetEffectType().ordinal());
        GameSystem.Instance.SaveEditEnd();
    }

    public PARTICLETYPE GetEffectType()
    {
        return effecttype;
    }

    public void SetMaxHealth(float _value)
    {
        MaxHealth = _value;
    }

    public void AddScore(int _value)
    {
        Score += _value;
    }

    public int GetScore()
    {
        return Score;
    }

    public float GetHealth()
    {
        return Health;
    }

    public float GetMaxHealth()
    {
        return MaxHealth;
    }

    public void MinusHealth(float _value)
    {
        Health -= _value;
    }

    public void AddHealth(float _value)
    {
        Health += _value;
    }

    public void SetHealth(float _value){Health = _value;}

    public void AddMoney(float _value)
    {
        Money += _value;
    }

    public float GetDepth()
    {
        return Depth;
    }

    public void SetDepth(float _value)
    {
        Depth = _value;
    }

    public void DepthUpdate(float _dt)
    {
        Depth += _dt*DepthIncreaseSpeed;
    }

    public Vector2 GetPos()
    {
        return Pos;
    }

    public void SetPos(Vector2 Pos)
    {
        this.Pos = Pos;
    }

    public void AddMoney(int _value)
    {
        Money += _value;
    }

    public void MinusMoney(int _value)
    {
        Money -= _value;
    }

    public int GetMoney()
    {
        return Money;
    }

    public GunInfo GetMainWeapon()
    {
        return Weapons[0];
    }

    public GunInfo GetSubWeaponLeft()
    {
        return Weapons[1];
    }

    public GunInfo GetSubWeaponRight()
    {
        return Weapons[2];
    }

    public void SetMainWeapon(GunInfo _newWeapon)
    {
        Weapons[0] = _newWeapon;
    }

    public void SetSubWeaponLeft(GunInfo _newWeapon)
    {
        Weapons[1] = _newWeapon;
    }

    public void SetSubWeaponRight(GunInfo _newWeapon)
    {
        Weapons[2] = _newWeapon;
    }
}
