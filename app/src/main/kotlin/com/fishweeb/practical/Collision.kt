package com.fishweeb.practical

object Collision {
    fun SphereToSphere(
        x1: Float,
        y1: Float,
        radius1: Float,
        x2: Float,
        y2: Float,
        radius2: Float
    ): Boolean {
        val xVec = x2 - x1
        val yVec = y2 - y1
        val distSquared = xVec * xVec + yVec * yVec
        var rSquared = radius1 + radius2
        rSquared *= rSquared
        if (distSquared > rSquared) return false

        return true
    }

    fun AABBToAABB(
        entity1Xpos: Float,
        entity1Ypos: Float,
        entity1Width: Float,
        entity1Height: Float,
        entity2Xpos: Float,
        entity2Ypos: Float,
        entity2Width: Float,
        entity2Height: Float
    ): Boolean {
        if (entity1Xpos - (entity1Width * 0.5f) < entity2Xpos - (entity2Width * 0.5f) + entity2Width && entity1Xpos - (entity1Width * 0.5f) + entity1Width > entity2Xpos - (entity2Width * 0.5f) && entity1Ypos - (entity1Height * 0.5f) < entity2Ypos - (entity2Height * 0.5f) + entity2Height && entity1Ypos - (entity1Height * 0.5f) + entity1Height > entity2Ypos - (entity2Height * 0.5f)) {
            return true
        }
        return false
    }

    fun AABBToAABB(entity1: Collidable, entity2: Collidable): Boolean {
        if (entity1.GetPosX() - (entity1.GetWidth() * 0.5f) < entity2.GetPosX() - (entity2.GetWidth() * 0.5f) + entity2.GetWidth() && entity1.GetPosX() - (entity1.GetWidth() * 0.5f) + entity1.GetWidth() > entity2.GetPosX() - (entity2.GetWidth() * 0.5f) && entity1.GetPosY() - (entity1.GetHeight() * 0.5f) < entity2.GetPosY() - (entity2.GetHeight() * 0.5f) + entity2.GetHeight() && entity1.GetPosY() - (entity1.GetHeight() * 0.5f) + entity1.GetHeight() > entity2.GetPosY() - (entity2.GetHeight() * 0.5f)) {
            return true
        }
        return false
    }
}
