package com.aclastudios.spaceconquest.Sprites.Resource;

import com.badlogic.gdx.math.Vector2;

public class ResourceDef {
    public Vector2 position;
    public Class<?> type;

    public ResourceDef(Vector2 postion, Class<?> type){
        this.position = postion;
        this.type = type;
    }
}
