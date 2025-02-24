package com.fishweeb.practical

import android.graphics.Canvas
import android.view.SurfaceView
import java.util.Collections
import java.util.LinkedList

class EntityManager
private constructor() {
    private val entityList = LinkedList<EntityBase>()
    private val addQueue = LinkedList<EntityBase>()
    private var view: SurfaceView? = null

    fun Init(_view: SurfaceView?) {
        view = _view
    }

    fun Update(_dt: Float) {
        val removalList = LinkedList<EntityBase>()

        //Add in the newly created entities
        for (newEntity in addQueue) entityList.add(newEntity)

        addQueue.clear() //Clean up of add list

        // Update all
        for (currEntity in entityList) {
            currEntity.Update(_dt)

            // Check if need to clean up
            if (currEntity.IsDone()) removalList.add(currEntity)
        }

        // Remove all entities that are done
        for (currEntity in removalList) entityList.remove(currEntity)

        removalList.clear() // Clean up of removal list

        // Collision Check
        for (i in entityList.indices) {
            val currEntity = entityList[i]

            if (currEntity is Collidable) {
                val first = currEntity as Collidable

                for (j in i + 1 until entityList.size) {
                    val otherEntity = entityList[j]

                    if (otherEntity is Collidable) {
                        val second = otherEntity as Collidable

                        if (Collision.AABBToAABB(first, second)) {
                            first.OnHit(second)
                            second.OnHit(first)
                        }
                    }
                }
            }

            // Check if need to clean up
            if (currEntity.IsDone()) removalList.add(currEntity)
        }

        // Remove all entities that are done
        for (currEntity in removalList) entityList.remove(currEntity)
    }

    fun EmptyEntityList() {
        entityList.clear()
        addQueue.clear()
    }

    fun Render(_canvas: Canvas?) {
        Collections.sort(
            entityList
        ) { o1, o2 -> o1.GetRenderLayer() - o2.GetRenderLayer() }

        for (currEntity in entityList) currEntity.Render(_canvas)
    }

    fun AddEntity(_newEntity: EntityBase) {
        _newEntity.Init(view)
        addQueue.add(_newEntity)
    }

    companion object {
        val Instance: EntityManager = EntityManager()
    }
}

