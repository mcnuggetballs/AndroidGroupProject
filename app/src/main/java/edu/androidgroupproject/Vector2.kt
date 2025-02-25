package edu.androidgroupproject

import kotlin.math.sqrt

class Vector2 {
    var x: Float
    var y: Float

    constructor() {
        x = 0f
        y = 0f
    }

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun PlusEqual(other: Vector2) {
        x += other.x
        y += other.y
    }

    fun PlusEqual(other: Vector2, _dt: Float) {
        x += other.x * _dt
        y += other.y * _dt
    }

    fun MinusEqual(other: Vector2) {
        x -= other.x
        y -= other.y
    }

    fun MinusEqual(other: Vector2, _dt: Float) {
        x -= other.x * _dt
        y -= other.y * _dt
    }

    fun Plus(other: Vector2): Vector2 {
        val tempVector = Vector2(x, y)
        tempVector.PlusEqual(other)
        return tempVector
    }

    fun Minus(other: Vector2): Vector2 {
        val tempVector = Vector2(x, y)
        tempVector.MinusEqual(other)
        return tempVector
    }

    fun Normalized(): Vector2 {
        val distance = sqrt((x * x + y * y).toDouble()).toFloat()
        return Vector2(x / distance, y / distance)
    }

    fun Equals(otherVector: Vector2) {
        x = otherVector.x
        y = otherVector.y
    }

    fun Times(num: Float): Vector2 {
        val tempVector = Vector2(x, y)
        tempVector.x *= num
        tempVector.y *= num
        return tempVector
    }

    fun TimesEquals(num: Float) {
        x *= num
        y *= num
    }

    fun DistanceSquaredFrom(other: Vector2): Float {
        val x = other.x - this.x
        val y = other.y - this.y
        val distSquared = x * x + y * y
        return distSquared
    }
}
