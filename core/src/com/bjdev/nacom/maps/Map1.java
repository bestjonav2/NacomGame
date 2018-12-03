package com.bjdev.nacom.maps;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bjdev.nacom.players.Aldeano;
import com.bjdev.nacom.players.Anciano;
import com.bjdev.nacom.players.Axel;
import com.bjdev.nacom.players.Entity;
import com.bjdev.nacom.players.Sprite;
import com.bjdev.nacom.players.StationarySprite;

public class Map1 extends MapEntity {

    public Map1() {
        map = new TmxMapLoader().load("maps/map.tmx");

        Axel player = new Axel("sprites/player_bitmap_16.png", 0, 0, 16);
        Aldeano player2 = new Aldeano("sprites/ald.png", 5, 10, 32);
        Anciano player3 = new Anciano("sprites/ald.png", 3, 20, 32);
        StationarySprite dialogo1 = new StationarySprite("images/dialog1.0.png",6,11,1,100,1);
        dialogo1.setName("d1");
        dialogo1.setCurrentFrame(dialogo1.getAnimationFrame());
        sprites.add(player);
        player2.setCurrentFrame(player2.getStandFrontFrame());
        player3.setCurrentFrame(player3.getStandFrontFrame());
        sprites.add(player2);
        sprites.add(dialogo1);
        sprites.add(player3);


        exits.put(new Vector3(73.2f, 35.8f, 0), "cave");

        red = 74f/255f;
        green = 160f/255f;
        blue = 223f/255f;
    }
}
