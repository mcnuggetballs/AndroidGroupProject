package com.fishweeb.practical

interface Collidable {
    fun GetType(): String?

    fun GetPosX(): Float
    fun GetPosY(): Float

    fun GetRadius(): Float
    fun GetWidth(): Float
    fun GetHeight(): Float

    fun OnHit(_other: Collidable?)

    fun GetDamage(): Float
    fun GetDead(): Boolean
}
