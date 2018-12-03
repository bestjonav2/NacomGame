package com.bjdev.nacom.players;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by iOwner on 02/12/2018.
 */

public class Anciano extends Sprite {
    public Anciano(String textureFile, float x, float y, int size) {
        super(textureFile, x, y);

        standFrontAnimation = new Animation(0.3f, getFrames(texture, 4, 10, size));
        standBackAnimation = new Animation(0.3f, getFrames(texture, 4, 10, size));
        standLeftAnimation = new Animation(0.3f, getFrames(texture, 4, 10, size));
        standRightAnimation = new Animation(0.3f, getFrames(texture, 4, 10, size));
        walkFrontAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        walkBackAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        walkLeftAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        walkRightAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        attackFrontAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        attackBackAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        attackLeftAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        attackRightAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        deathAnimation = new Animation(0.3f, getFrames(texture, 4, 8, size));
        name = "Anciano";
    }
}
