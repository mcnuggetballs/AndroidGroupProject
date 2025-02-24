package com.fishweeb.practical

class PlayerInfo private constructor() {
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
        Weapons.fill(null)

        var LoadMaxHealth = GameSystem.Instance.GetFloatFromSave("maxhealth").takeIf { it > 0f } ?: 10f
        var MainShootCount = GameSystem.Instance.GetIntFromSave("maingunshootcount").takeIf { it > 0 } ?: 1
        var MainDamage = GameSystem.Instance.GetIntFromSave("mainshootdamage").takeIf { it > 0 } ?: 5

        val particle = GameSystem.Instance.GetIntFromSave("effecttype")
        effecttype = PARTICLETYPE.values().getOrNull(particle) ?: PARTICLETYPE.P_BUBBLE

        Weapons[0] = GunInfo().apply {
            SetFireRate(0.25f)
            SetWeaponDamage(MainDamage)
            SetShootAmount(MainShootCount)
            SetShootType(SHOOTTYPE.S_STRAIGHT)
        }

        MaxHealth = LoadMaxHealth
        Health = MaxHealth
        Depth = 0f
        Money = GameSystem.Instance.GetIntFromSave("money")
        Score = 0
    }

    fun RestartGame() {
        Health = MaxHealth
        Depth = 0f
        Score = 0

        Weapons.forEach { it?.apply {
            SetFireRatePercentage(100f)
            SetBonusShootAmount(0)
        } }
    }

    fun SetEffectType(_type: PARTICLETYPE) {
        effecttype = _type
        GameSystem.Instance.SaveEditBegin()
        GameSystem.Instance.SetIntInSave("effecttype", effecttype.ordinal)
        GameSystem.Instance.SaveEditEnd()
    }

    fun GetEffectType(): PARTICLETYPE = effecttype

    fun SetMaxHealth(_value: Float) {
        MaxHealth = _value
    }

    fun AddScore(_value: Int) {
        Score += _value
    }

    fun GetScore(): Int = Score

    fun GetHealth(): Float = Health

    fun GetMaxHealth(): Float = MaxHealth

    fun MinusHealth(_value: Float) {
        Health -= _value
    }

    fun AddHealth(_value: Float) {
        Health += _value
    }

    fun SetHealth(_value: Float) {
        Health = _value
    }

    fun AddMoney(_value: Int) {
        Money += _value
    }

    fun MinusMoney(_value: Int) {
        Money -= _value
    }

    fun GetMoney(): Int = Money

    fun GetDepth(): Float = Depth

    fun SetDepth(_value: Float) {
        Depth = _value
    }

    fun DepthUpdate(_dt: Float) {
        Depth += _dt * DepthIncreaseSpeed
    }

    fun GetPos(): Vector2 = Pos

    fun SetPos(_pos: Vector2) {
        this.Pos = _pos
    }

    fun GetMainWeapon(): GunInfo? = Weapons[0]

    fun GetSubWeaponLeft(): GunInfo? = Weapons[1]

    fun GetSubWeaponRight(): GunInfo? = Weapons[2]

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
        val Instance: PlayerInfo = PlayerInfo()
    }
}
