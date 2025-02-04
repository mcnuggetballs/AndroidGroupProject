package com.fishweeb.practical;

public class Collision
{
    public static boolean SphereToSphere(float x1,float y1, float radius1, float x2, float y2, float radius2)
    {
        float xVec = x2 - x1;
        float yVec = y2 - y1;
        float distSquared = xVec * xVec + yVec * yVec;
        float rSquared = radius1 + radius2;
        rSquared *= rSquared;
        if (distSquared > rSquared)
        return false;

        return true;
    }

    public static boolean AABBToAABB(float entity1Xpos,float entity1Ypos, float entity1Width,float entity1Height,float entity2Xpos,float entity2Ypos, float entity2Width,float entity2Height)
    {
        if (entity1Xpos-(entity1Width*0.5f) < entity2Xpos - (entity2Width*0.5f) + entity2Width && entity1Xpos-(entity1Width*0.5f) + entity1Width > entity2Xpos - (entity2Width*0.5f) && entity1Ypos - (entity1Height*0.5f) < entity2Ypos - (entity2Height*0.5f) + entity2Height && entity1Ypos - (entity1Height*0.5f) + entity1Height > entity2Ypos - (entity2Height*0.5f))
        {
            return true;
        }
        return false;
    }

    public static boolean AABBToAABB(Collidable entity1,Collidable entity2)
    {
        if (entity1.GetPosX()-(entity1.GetWidth()*0.5f) < entity2.GetPosX() - (entity2.GetWidth()*0.5f) + entity2.GetWidth() && entity1.GetPosX()-(entity1.GetWidth()*0.5f) + entity1.GetWidth() > entity2.GetPosX() - (entity2.GetWidth()*0.5f) && entity1.GetPosY() - (entity1.GetHeight()*0.5f) < entity2.GetPosY() - (entity2.GetHeight()*0.5f) + entity2.GetHeight() && entity1.GetPosY() - (entity1.GetHeight()*0.5f) + entity1.GetHeight() > entity2.GetPosY() - (entity2.GetHeight()*0.5f))
        {
            return true;
        }
        return false;
    }
}
