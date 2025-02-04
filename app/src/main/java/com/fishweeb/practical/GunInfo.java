package com.fishweeb.practical;

enum SHOOTTYPE
{
    S_STRAIGHT,
    S_SPREAD;
}

public class GunInfo
{
    private int WeaponDamage;
    private float FireRate;
    private float FireRatePercentage = 100;
    private int ShootAmount;
    private int BonusShootAmount = 0;
    private SHOOTTYPE shoottype;

    public void SetWeaponDamage(int _damage)
    {
        WeaponDamage = _damage;
    }

    public int GetWeaponDamage()
    {
        return WeaponDamage;
    }

    public void SetFireRate(float _firerate)
    {
        FireRate = _firerate;
    }

    public float GetFireRate()
    {
        return FireRate * (FireRatePercentage/100);
    }

    public void SetShootType(SHOOTTYPE _type)
    {
        shoottype = _type;
    }

    public SHOOTTYPE GetShootType()
    {
        return shoottype;
    }

    public void SetShootAmount(int _amount)
    {
        ShootAmount = _amount;
    }

    public void AddShootAmount(SHOOTTYPE _type)
    {
        if (this.shoottype == _type)
            ++BonusShootAmount;
        else
        {
            this.shoottype = _type;
            BonusShootAmount = 1;
        }
    }

    public void SetBonusShootAmount(int _value)
    {
        BonusShootAmount = _value;
    }

    public void SetFireRatePercentage(float _value)
    {
        FireRatePercentage = _value;
    }

    public int GetShootAmount()
    {
        return ShootAmount + BonusShootAmount;
    }
}
