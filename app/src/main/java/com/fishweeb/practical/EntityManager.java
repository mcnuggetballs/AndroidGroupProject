package com.fishweeb.practical;
import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class EntityManager
{
    public final static EntityManager Instance = new EntityManager();
    private LinkedList<EntityBase> entityList = new LinkedList<EntityBase>();
    private LinkedList<EntityBase> addQueue = new LinkedList<EntityBase>();
    private SurfaceView view = null;

    private EntityManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    public void Update(float _dt)
    {
        LinkedList<EntityBase> removalList = new LinkedList<EntityBase>();

        //Add in the newly created entities
        for (EntityBase newEntity : addQueue)
            entityList.add(newEntity);

        addQueue.clear(); //Clean up of add list

        // Update all
        for (EntityBase currEntity : entityList)
        {
            currEntity.Update(_dt);

            // Check if need to clean up
            if (currEntity.IsDone())
                removalList.add(currEntity);
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList)
            entityList.remove(currEntity);

        removalList.clear(); // Clean up of removal list

        // Collision Check
        for (int i = 0; i < entityList.size(); ++i)
        {
            EntityBase currEntity = entityList.get(i);

            if (currEntity instanceof Collidable)
            {
                Collidable first = (Collidable) currEntity;

                for (int j = i+1; j < entityList.size(); ++j)
                {
                    EntityBase otherEntity = entityList.get(j);

                    if (otherEntity instanceof Collidable)
                    {
                        Collidable second = (Collidable) otherEntity;

                        if (Collision.AABBToAABB(first,second))
                        {
                            first.OnHit(second);
                            second.OnHit(first);
                        }
                    }
                }
            }

            // Check if need to clean up
            if (currEntity.IsDone())
                removalList.add(currEntity);
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList)
            entityList.remove(currEntity);
    }

    public void EmptyEntityList()
    {
            entityList.clear();
            addQueue.clear();
    }

    public void Render(Canvas _canvas)
    {
        Collections.sort(entityList,new Comparator<EntityBase>()
                {
                   public int compare(EntityBase o1,EntityBase o2)
                   {
                       return o1.GetRenderLayer() - o2.GetRenderLayer();
                   }
                }
        );

        for (EntityBase currEntity : entityList)
            currEntity.Render(_canvas);
    }

    public void AddEntity(EntityBase _newEntity)
    {
        _newEntity.Init(view);
        addQueue.add(_newEntity);
    }
}

