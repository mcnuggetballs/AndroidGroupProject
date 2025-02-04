package com.fishweeb.practical;

public class Vector2
{
    public float x;
    public float y;

    public Vector2()
    {
        x = 0;
        y = 0;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void PlusEqual(Vector2 other)
    {
        x += other.x;
        y += other.y;
    }

    public void PlusEqual(Vector2 other,float _dt)
    {
        x += other.x * _dt;
        y += other.y * _dt;
    }

    public void MinusEqual(Vector2 other)
    {
        x -= other.x;
        y -= other.y;
    }

    public void MinusEqual(Vector2 other, float _dt)
    {
        x -= other.x * _dt;
        y -= other.y * _dt;
    }

    public Vector2 Plus(Vector2 other)
    {
        Vector2 tempVector = new Vector2(x,y);
        tempVector.PlusEqual(other);
        return tempVector;
    }

    public Vector2 Minus(Vector2 other)
    {
        Vector2 tempVector = new Vector2(x,y);
        tempVector.MinusEqual(other);
        return tempVector;
    }

    public Vector2 Normalized()
    {
        float distance = (float)Math.sqrt(x * x + y * y);
        return new Vector2(x / distance, y / distance);
    }

    public void Equals(Vector2 otherVector)
    {
        x = otherVector.x;
        y = otherVector.y;
    }

    public Vector2 Times(float num)
    {
        Vector2 tempVector = new Vector2(x,y);
        tempVector.x *= num;
        tempVector.y *= num;
        return tempVector;
    }

    public void TimesEquals(float num)
    {
        x *= num;
        y *= num;
    }

    public float DistanceSquaredFrom(Vector2 other)
    {
        float x = other.x - this.x;
        float y = other.y - this.y;
        float distSquared = x * x + y * y;
        return  distSquared;
    }
}
