package com.bjdev.nacom.maps;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.bjdev.nacom.players.Axel;
import com.bjdev.nacom.players.StationarySprite;

public class Map1 extends MapEntity {

    public Map1() {
        map = new TmxMapLoader().load("maps/map.tmx");

        Axel player = new Axel("sprites/player_bitmap_16.png", 16, 0, 16);

        sprites.add(player);

        exits.put(new Vector3(73.2f, 35.8f, 0), "cave");

        red = 74f/255f;
        green = 160f/255f;
        blue = 223f/255f;
    }
}
