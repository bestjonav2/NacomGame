package com.bjdev.nacom.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import com.bjdev.nacom.MainNacom;
import com.bjdev.nacom.maps.*;
import com.bjdev.nacom.players.Sprite;
import com.bjdev.nacom.players.StationarySprite;

import java.util.HashMap;

public class GameUI implements Screen{

    final MainNacom game;

    private Map1 level01;

    private Array<MapEntity> levels;

    private HashMap<String, State> states;

    private TiledMap map;

    private Array<Sprite> sprites;

    private enum State {
        level01State,
        caveState,
        cave02State
    }

    private State state;

    private boolean stateChanged;

    private MapEntity currentLevel;

    private View view;

    public GameUI (MainNacom game){
        this.game = game;
        level01 = new Map1();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        levels = new Array<MapEntity>();
        levels.add(level01);

        states = new HashMap<String, State>(3);
        states.put("level01", State.level01State);

        state = State.level01State;
        stateChanged = false;
        currentLevel = level01;

        map = currentLevel.getMap();
        sprites = currentLevel.getSprites();

        view = viewPool.obtain();
    }

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };

    private Pool<View> viewPool = new Pool<View>() {
        @Override
        protected View newObject() {
            return new View(currentLevel);
        }
    };

    private Array<Rectangle> tiles = new Array<Rectangle>();

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (stateChanged) {
            switch (state) {
                case level01State:
                    currentLevel = level01;
                    setupLevel();
                    break;
            }
            stateChanged = false;
        }

        view.getInput();
        updateSprites(Gdx.graphics.getDeltaTime());
        view.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        if (stateChanged) {
            switch (state) {
                case level01State:
                    currentLevel = level01;
                    setupLevel();
                    break;
            }
            stateChanged = false;
        }

        view.getInput();
        updateSprites(Gdx.graphics.getDeltaTime());
        view.render();
    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        for (StationarySprite sprite : level01.getStationarySprites()) {
            sprite.getTexture().dispose();
        }
        for (MapEntity level : levels) {
            for (Sprite sprite : level.getSprites()) {
                sprite.getTexture().dispose();
            }
            level.getMap().dispose();
        }
        view.getStage().dispose();
        for (BitmapFont font : view.getFonts()) {
            font.dispose();
        }
    }

    private void updateSprites(float deltaTime) {
        boolean bCruso = false;
        for (Sprite sprite : sprites) {
                // Check for map edges.
            if(sprite.getName().equals("Axel")) {
                if (sprite.getX() < 0) {
                    sprite.setX(0);
                }
                if (sprite.getX() > 100 - Sprite.SIZE) {
                    sprite.setX(100 - Sprite.SIZE);
                }
                if (sprite.getY() < 1) {
                    sprite.setY(1);
                }
                if (sprite.getY() > 100 - Sprite.SIZE) {
                    sprite.setY(100 - Sprite.SIZE);
                }

                // Manage velocity and switch states.
                if (Math.abs(sprite.getDX()) > Sprite.MAX_VELOCITY) {
                    sprite.setDX(Math.signum(sprite.getDX()) * Sprite.MAX_VELOCITY);
                }
                if (Math.abs(sprite.getDY()) > Sprite.MAX_VELOCITY) {
                    sprite.setDY(Math.signum(sprite.getDY()) * Sprite.MAX_VELOCITY);
                }
                if (Math.abs(sprite.getDX()) < 1) {
                    sprite.setDX(0);

                    switch (sprite.getState()) {
                        case walkLeft:
                            sprite.setState(Sprite.State.standLeft);
                            break;
                        case walkRight:
                            sprite.setState(Sprite.State.standRight);
                            break;
                    }
                }
                if (Math.abs(sprite.getDY()) < 1) {
                    sprite.setDY(0);

                    switch (sprite.getState()) {
                        case walkFront:
                            sprite.setState(Sprite.State.standFront);
                            break;
                        case walkBack:
                            sprite.setState(Sprite.State.standBack);
                            break;
                    }
                }
                sprite.getVelocity().scl(deltaTime);

                detectCollisions(sprite, 3);

                // Scale the velocity by the inverse delta time and set the latest position.
                sprite.getPosition().add(sprite.getVelocity());
                sprite.getVelocity().scl(1 / deltaTime);

                // Apply damping to the velocity so the sprite doesn't walk infinitely once a key is pressed.
                sprite.setDX(sprite.getDX() * Sprite.DAMPING);
                sprite.setDY(sprite.getDY() * Sprite.DAMPING);
            }
            else
            {
                if(sprites.get(0).getY()>10&&!sprite.getName().equals("d1")&&sprite.getName().equals("Aldeano")){
                    sprite.setState(Sprite.State.standBack);
                }
            }
        }
    }

    private void setupLevel() {
        map = currentLevel.getMap();
        sprites = currentLevel.getSprites();
        viewPool.free(view);
        viewPool.clear();
        view = viewPool.obtain();
    }

    public void setTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles, int layerIndex) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x + 0.2f, y - 0.2f, 0.8f, 0.8f);
                    tiles.add(rect);
                }
            }
        }
    }

    public void detectCollisions(Sprite sprite, int layerIndex) {
        Rectangle spriteRect = rectPool.obtain();
        spriteRect.set(sprite.getX() / 0.16f, sprite.getY() / 0.16f, Sprite.SIZE, Sprite.SIZE);
        int startX, startY, endX, endY;

        MapLayer collisionObjectLayer = map.getLayers().get(layerIndex);
        MapObjects objects = collisionObjectLayer.getObjects();

        // there are several other types, Rectangle is probably the most common one
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, spriteRect)) {
                Gdx.app.debug("CollisionTiles", "Collision");
                Gdx.app.debug("CollisionTiles", "State: " + sprite.getState());
                if(sprite.getState() == Sprite.State.walkFront){
                    sprite.setPosition(sprite.getX(), sprite.getY() - 0.1f);
                    sprite.setDY(0);
                }
                if(sprite.getState() == Sprite.State.walkBack){
                    sprite.setPosition(sprite.getX(), sprite.getY() + 0.1f);
                    sprite.setDY(0);
                }
                if(sprite.getState() == Sprite.State.walkLeft){
                    sprite.setPosition(sprite.getX() - 0.1f, sprite.getY());
                    sprite.setDX(0);
                }
                if(sprite.getState() == Sprite.State.walkRight){
                    sprite.setPosition(sprite.getX() + 0.1f, sprite.getY());
                    sprite.setDX(0);
                }

            }
        }
        /*
        // X-Axis
        if(sprite.getDX() > 0) {
            startX = endX = (int)(sprite.getX() + Sprite.SIZE + sprite.getDX());
        } else {
            startX = endX = (int)(sprite.getX() + sprite.getDX());
        }
        startY = (int)(sprite.getY());
        endY = (int)(sprite.getY() + Sprite.SIZE);
        setTiles(startX, startY, endX, endY, tiles, layerIndex);
        spriteRect.x += sprite.getDX();

        // Tile collision on the x-axis.
        for (Rectangle tile: tiles) {
            if(spriteRect.overlaps(tile)) {
                sprite.setDX(0);
                if (layerIndex == 5) {
                    exitRoom(new Vector3(tile.getX(), tile.getY(), 0));
                }
                break;
            }
        }

        // Sprite collision on the x-axis.
        for (Sprite element : sprites) {
            Rectangle elemRect = rectPool.obtain();
            elemRect.set(element.getX() + 0.3f, element.getY() - 0.3f,
                    Sprite.SIZE - 0.6f, Sprite.SIZE - 0.6f);
            if (element != sprite) {
                if (spriteRect.overlaps(elemRect)) {
                    sprite.setDX(0);
                    break;
                }
            }
        }

        spriteRect.x = sprite.getX() + 0.3f;

        // Y-Axis
        if (sprite.getDY() > 0) {
            startY = endY = (int)(sprite.getY() + Sprite.SIZE + sprite.getDY());
        } else {
            startY = endY = (int)(sprite.getY() + sprite.getDY());
        }
        startX = (int)(sprite.getX());
        endX = (int)(sprite.getX() + Sprite.SIZE);
        setTiles(startX, startY, endX, endY, tiles, layerIndex);
        spriteRect.y += sprite.getDY();

        // Tile collision on the y-axis.
        for (Rectangle tile: tiles) {
            if (spriteRect.overlaps(tile)) {
                sprite.setDY(0);
                if (layerIndex == 5) {
                    exitRoom(new Vector3(tile.getX(), tile.getY(), 0));
                }
                break;
            }
        }

        // Sprite collision on the y-axis.
        for (Sprite element : sprites) {
            Rectangle elemRect = rectPool.obtain();
            elemRect.set(element.getX() + 0.3f, element.getY() - 0.3f,
                    Sprite.SIZE - 0.6f, Sprite.SIZE - 0.6f);
            if (element != sprite) {
                if (spriteRect.overlaps(elemRect)) {
                    sprite.setDY(0);
                    break;
                }
            }
        }

        spriteRect.y = sprite.getY() - 0.3f;
        */
        rectPool.free(spriteRect);
    }

    private void exitRoom(Vector3 tile) {
        for (Vector3 point : currentLevel.getExits().keySet()) {
            if (point.x == tile.x && point.y == tile.y) {
                state = states.get(currentLevel.getExits().get(point));
                stateChanged = true;
            }
        }
    }

    private void combat() {
        for (Sprite sprite : sprites) {

        }
    }
}
