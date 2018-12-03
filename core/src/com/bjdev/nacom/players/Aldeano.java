package com.bjdev.nacom.players;

/**
 * Created by iOwner on 02/12/2018.
 */

import com.badlogic.gdx.graphics.g2d.Animation;

public class Aldeano extends Sprite {

    public Aldeano(String textureFile, float x, float y, int size) {
        super(textureFile, x, y);

        standFrontAnimation = new Animation(0.3f, getFrames(texture, 0, 2, size));
        standBackAnimation = new Animation(0.3f, getFrames(texture, 3, 1, size));
        standLeftAnimation = new Animation(0.3f, getFrames(texture, 1, 1, size));
        standRightAnimation = new Animation(0.3f, getFrames(texture, 2, 1, size));
        walkFrontAnimation = new Animation(0.3f, getFrames(texture, 3, 2, size));
        walkBackAnimation = new Animation(0.3f, getFrames(texture, 0, 2, size));
        walkLeftAnimation = new Animation(0.3f, getFrames(texture, 1, 2, size));
        walkRightAnimation = new Animation(0.3f, getFrames(texture, 2, 2, size));
        attackFrontAnimation = new Animation(0.3f, getFrames(texture, 3, 3, size));
        attackBackAnimation = new Animation(0.3f, getFrames(texture, 0, 3, size));
        attackLeftAnimation = new Animation(0.3f, getFrames(texture, 1, 3, size));
        attackRightAnimation = new Animation(0.3f, getFrames(texture, 2, 3, size));
        deathAnimation = new Animation(0.3f, getFrames(texture, 0, 3, size));
        name = "Aldeano";
    }

}
