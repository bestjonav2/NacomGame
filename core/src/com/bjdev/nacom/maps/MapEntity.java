package com.bjdev.nacom.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bjdev.nacom.players.Sprite;
import com.bjdev.nacom.players.StationarySprite;

import java.util.HashMap;

public class MapEntity {
    protected TiledMap map;
    protected Array<Sprite> sprites;
    protected Array<StationarySprite> stationarySprites;
    protected HashMap<Vector3, String> exits;
    protected float red;
    protected float green;
    protected float blue;

    public MapEntity() {
        sprites = new Array<Sprite>();
        stationarySprites = new Array<StationarySprite>();
        exits = new HashMap<Vector3, String>();
    }

    public Array<StationarySprite> getStationarySprites() {
        return stationarySprites;
    }

    public TiledMap getMap() {
        return map;
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public HashMap<Vector3, String> getExits() {
        return exits;
    }
}
