package com.fishweeb.practical

class PlayerInfo
private constructor() {
    private var MaxHealth: Float
    private var Health: Float
    private var Money: Int
    private var Depth: Float
    private val DepthIncreaseSpeed = 100f
    private var Pos = Vector2()
    private var Score: Int
    private val Weapons = arrayOfNulls<GunInfo>(3)
    private var effecttype: PARTICLETYPE

    init {
        for (i in 0..2) {
            Weapons[i] = null
        }

        var LoadMaxHealth: Float = GameSystem.Companion.Instance.GetFloatFromSave("maxhealth")
        if (LoadMaxHealth == 0f) {
            LoadMaxHealth = 10f
        }
        var MainShootCount: Int = GameSystem.Companion.Instance.GetIntFromSave("maingunshootcount")
        var MainDamage: Int = GameSystem.Companion.Instance.GetIntFromSave("mainshootdamage")
        if (MainShootCount == 0) {
            MainShootCount = 1
        }
        if (MainDamage == 0) {
            MainDamage = 5
        }

        val particle: Int = GameSystem.Companion.Instance.GetIntFromSave("effecttype")
        effecttype = PARTICLETYPE.entries[particle]
        if (particle == 0) {
            effecttype = PARTICLETYPE.P_BUBBLE
        }

        Weapons[0] = GunInfo()
        Weapons[0]!!.SetFireRate(0.25f)
        Weapons[0]!!.SetWeaponDamage(MainDamage)
        Weapons[0]!!.SetShootAmount(MainShootCount)
        Weapons[0]!!.SetShootType(SHOOTTYPE.S_STRAIGHT)

        MaxHealth = LoadMaxHealth
        Health = MaxHealth
        Depth = 0f
        Money = GameSystem.Companion.Instance.GetIntFromSave("money")
        Score = 0
    }

    fun RestartGame() {
        Health = MaxHealth
        Depth = 0f
        Score = 0

        for (i in 0..2) {
            if (Weapons[i] != null) {
                Weapons[i]!!.SetFireRatePercentage(100f)
                Weapons[i]!!.SetBonusShootAmount(0)
            }
        }
    }

    fun SetEffectType(_type: PARTICLETYPE) {
        effecttype = _type
        GameSystem.Companion.Instance.SaveEditBegin()
        GameSystem.Companion.Instance.SetIntInSave("effecttype", Instance.GetEffectType().ordinal)
        GameSystem.Companion.Instance.SaveEditEnd()
    }

    fun GetEffectType(): PARTICLETYPE {
        return effecttype
    }

    fun SetMaxHealth(_value: Float) {
        MaxHealth = _value
    }

    fun AddScore(_value: Int) {
        Score += _value
    }

    fun GetScore(): Int {
        return Score
    }

    fun GetHealth(): Float {
        return Health
    }

    fun GetMaxHealth(): Float {
        return MaxHealth
    }

    fun MinusHealth(_value: Float) {
        Health -= _value
    }

    fun AddHealth(_value: Float) {
        Health += _value
    }

    fun SetHealth(_value: Float) {
        Health = _value
    }

    fun AddMoney(_value: Float) {
        Money = (Money + _value).toInt()
    }

    fun GetDepth(): Float {
        return Depth
    }

    fun SetDepth(_value: Float) {
        Depth = _value
    }

    fun DepthUpdate(_dt: Float) {
        Depth += _dt * DepthIncreaseSpeed
    }

    fun GetPos(): Vector2 {
        return Pos
    }

    fun SetPos(Pos: Vector2) {
        this.Pos = Pos
    }

    fun AddMoney(_value: Int) {
        Money += _value
    }

    fun MinusMoney(_value: Int) {
        Money -= _value
    }

    fun GetMoney(): Int {
        return Money
    }

    fun GetMainWeapon(): GunInfo? {
        return Weapons[0]
    }

    fun GetSubWeaponLeft(): GunInfo? {
        return Weapons[1]
    }

    fun GetSubWeaponRight(): GunInfo? {
        return Weapons[2]
    }

    fun SetMainWeapon(_newWeapon: GunInfo?) {
        Weapons[0] = _newWeapon
    }

    fun SetSubWeaponLeft(_newWeapon: GunInfo?) {
        Weapons[1] = _newWeapon
    }

    fun SetSubWeaponRight(_newWeapon: GunInfo?) {
        Weapons[2] = _newWeapon
    }

    companion object {
        var Instance: PlayerInfo = PlayerInfo()
    }
}
