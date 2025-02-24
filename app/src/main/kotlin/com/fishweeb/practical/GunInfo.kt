package com.fishweeb.practical

enum class SHOOTTYPE {
    S_STRAIGHT,
    S_SPREAD
}

class GunInfo {
    private var WeaponDamage = 0
    private var FireRate = 0f
    private var FireRatePercentage = 100f
    private var ShootAmount = 0
    private var BonusShootAmount = 0
    private var shoottype: SHOOTTYPE? = null

    fun SetWeaponDamage(_damage: Int) {
        WeaponDamage = _damage
    }

    fun GetWeaponDamage(): Int {
        return WeaponDamage
    }

    fun SetFireRate(_firerate: Float) {
        FireRate = _firerate
    }

    fun GetFireRate(): Float {
        return FireRate * (FireRatePercentage / 100)
    }

    fun SetShootType(_type: SHOOTTYPE?) {
        shoottype = _type
    }

    fun GetShootType(): SHOOTTYPE? {
        return shoottype
    }

    fun SetShootAmount(_amount: Int) {
        ShootAmount = _amount
    }

    fun AddShootAmount(_type: SHOOTTYPE) {
        if (this.shoottype == _type) ++BonusShootAmount
        else {
            this.shoottype = _type
            BonusShootAmount = 1
        }
    }

    fun SetBonusShootAmount(_value: Int) {
        BonusShootAmount = _value
    }

    fun SetFireRatePercentage(_value: Float) {
        FireRatePercentage = _value
    }

    fun GetShootAmount(): Int {
        return ShootAmount + BonusShootAmount
    }
}
