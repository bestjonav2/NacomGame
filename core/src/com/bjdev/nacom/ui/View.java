package com.bjdev.nacom.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import com.bjdev.nacom.logic.GameInputProcessor;
import com.bjdev.nacom.maps.MapEntity;
import com.bjdev.nacom.players.Axel;
import com.bjdev.nacom.players.Sprite;
import com.bjdev.nacom.players.SpriteUpComparator;
import com.bjdev.nacom.players.StationarySprite;

public class View {
    private Array<Sprite> sprites;
    private Array<Sprite> sortedUpSprites;
    private Array<Sprite> sortedDownSprites;
    private Array<StationarySprite> stationarySprites;
    private float red;
    private float green;
    private float blue;

    private boolean up,down,right,left;
    private Stage stage;

    private OrthographicCamera camera;

    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] baseLayer;
    private int[] underlayer1;
    private int[] collisionLayer;

    private SpriteBatch spriteBatch;

    private Label hpLabel;
    private Label hp;

    private Skin skin;

    private BitmapFont font25;

    private Array<BitmapFont> fonts;

    private GameInputProcessor inputProcessor;

    public View(MapEntity level) {
        stage = new Stage();
        inputProcessor = new GameInputProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        Table table = new Table();
        table.left().bottom();
        Image imgUp = new Image(new Texture("images/up.png"));
        imgUp.setSize(50,50);
        imgUp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                up=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                up=false;
            }
        });
        Image imgDown = new Image(new Texture("images/down.png"));
        imgDown.setSize(50,50);
        imgDown.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                down=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                down=false;
            }
        });
        Image imgRight = new Image(new Texture("images/right.png"));
        imgRight.setSize(50,50);
        imgRight.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                right=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                right=false;
            }
        });
        Image imgLeft = new Image(new Texture("images/left.png"));
        imgLeft.setSize(50,50);
        imgLeft.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                left=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                left=false;
            }
        });
        table.add();
        table.add(imgUp).size(imgUp.getWidth(),imgUp.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(imgLeft).size(imgLeft.getWidth(),imgLeft.getHeight());
        table.add();
        table.add(imgRight).size(imgRight.getWidth(),imgRight.getHeight());
        table.row().padBottom(55);
        table.add();
        table.add(imgDown).size(imgDown.getWidth(),imgDown.getHeight());
        table.add();
        stage.addActor(table);

        //DIALOGOS
        //Table diag1 = new Table();
        //Image imgDiag1 = new Image(new Texture("images/dialog1.0.png"));
        //imgUp.setSize(50,50);
        //diag1.add(imgDiag1).size(imgDiag1.getWidth(),imgDiag1.getHeight());
        //diag1.padLeft(50).padBottom(100);
        //diag1.left().bottom();
       // stage.addActor(diag1);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 40, Gdx.graphics.getHeight() / 40);
        camera.update();

        sprites = level.getSprites();
        stationarySprites = level.getStationarySprites();
        red = level.getRed();
        green = level.getGreen();
        blue = level.getBlue();
        spriteBatch = new SpriteBatch();

        // Hide the mouse cursor.
        Gdx.input.setCursorCatched(true);

        TiledMap map = level.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map, 0.16f);
        baseLayer = new int[1];
        baseLayer[0] = 0;
        underlayer1 = new int[1];
        underlayer1[0] = 1;
        collisionLayer = new int[1];
        collisionLayer[0] = 2;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/advocut-webfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        //BitmapFont font15 = generator.generateFont(15);
        font25 = generator.generateFont(parameter);
        generator.dispose();

        fonts = new Array<BitmapFont>();
        fonts.add(font25);

        setupHUD();
    }

    public void setupHUD() {
        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", font25);

        // Configure a TextButtonStyle and name it "default".
        // Skin resources are stored by type, so this doesn't overwrite the font.
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().left();
        stage.addActor(table);

        hpLabel = new Label("  HP:  ", skin);
        hp = new Label(Integer.toString(sprites.get(0).getHP()), skin);
        table.add(hpLabel);
        table.add(hp).width(50);
    }

    public void render() {
        Gdx.gl.glClearColor(red, green, blue, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(sprites.get(0).getX() + 1, sprites.get(0).getY(), 0);
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);

        mapRenderer.setView(camera);


        mapRenderer.render(baseLayer);
        mapRenderer.render(underlayer1);
        //mapRenderer.render(collisionLayer);

        sortedUpSprites = new Array<Sprite>(sprites);
        sortedUpSprites.sort(new SpriteUpComparator());

        // sortedDownSprites = new Array<Sprite>(sprites);
        // sortedDownSprites.sort(new SpriteDownComparator());

        spriteBatch.begin();
        renderStationarySprites();
        renderSprites();
        // renderSpritesTop();
        renderStationarySpritesTop();
        spriteBatch.end();

        hp.setText(Integer.toString(sprites.get(0).getHP()));
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        //stage.setViewport(width, height, true);
    }

    private void renderSprites() {
        for (Sprite sprite : sortedUpSprites) {
            sprite.setAnimationTime(Gdx.graphics.getDeltaTime());
                switch (sprite.getState()) {
                    case standFront:
                        if(sprite.getName().equals("Axel"))
                        sprite.setCurrentFrame(sprite.getStandFrontFrame());
                        break;
                    case standBack:

                        sprite.setCurrentFrame(sprite.getStandBackFrame());
                        break;
                    case standLeft:
                        sprite.setCurrentFrame(sprite.getStandLeftFrame());
                        break;
                    case standRight:
                        sprite.setCurrentFrame(sprite.getStandRightFrame());
                        break;
                    case walkFront:
                        sprite.setCurrentFrame(sprite.getWalkFrontFrame());
                        break;
                    case walkBack:
                        sprite.setCurrentFrame(sprite.getWalkBackFrame());
                        break;
                    case walkLeft:
                        sprite.setCurrentFrame(sprite.getWalkLeftFrame());
                        break;
                    case walkRight:
                        sprite.setCurrentFrame(sprite.getWalkRightFrame());
                        break;
                    case attackFront:
                        sprite.setCurrentFrame(sprite.getAttackFrontFrame());
                        break;
                    case attackBack:
                        sprite.setCurrentFrame(sprite.getAttackBackFrame());
                        break;
                    case attackLeft:
                        sprite.setCurrentFrame(sprite.getAttackLeftFrame());
                        break;
                    case attackRight:
                        sprite.setCurrentFrame(sprite.getAttackRightFrame());
                        break;
                    case death:
                        sprite.setCurrentFrame(sprite.getDeathFrame());
                        break;
                }


                if (!sprite.isFacingLeft()) {
                    spriteBatch.draw(sprite.getCurrentFrame(), sprite.getX(), sprite.getY(),
                            Sprite.SIZE, Sprite.SIZE);
                } else {
                    spriteBatch.draw(sprite.getCurrentFrame(), sprite.getX() + Sprite.SIZE, sprite.getY(),
                            -Sprite.SIZE, Sprite.SIZE);
                }
            }
        }

    private void renderSpritesTop() {
        for (Sprite sprite : sortedDownSprites) {
            sprite.setAnimationTime(Gdx.graphics.getDeltaTime());

            switch (sprite.getState()) {
                case standFront:
                    sprite.setCurrentFrameTop(sprite.getWalkFrontFrame());
                    break;
                case standBack:
                    sprite.setCurrentFrameTop(sprite.getStandBackFrame());
                    break;
                case standLeft:
                    sprite.setCurrentFrameTop(sprite.getStandLeftFrame());
                    break;
                case standRight:
                    sprite.setCurrentFrameTop(sprite.getStandRightFrame());
                    break;
                case walkFront:
                    sprite.setCurrentFrameTop(sprite.getWalkFrontFrame());
                    break;
                case walkBack:
                    sprite.setCurrentFrameTop(sprite.getWalkBackFrame());
                    break;
                case walkLeft:
                    sprite.setCurrentFrameTop(sprite.getWalkLeftFrame());
                    break;
                case walkRight:
                    sprite.setCurrentFrameTop(sprite.getWalkRightFrame());
                    break;
                case attackFront:
                    sprite.setCurrentFrameTop(sprite.getAttackFrontFrame());
                    break;
                case attackBack:
                    sprite.setCurrentFrameTop(sprite.getAttackBackFrame());
                    break;
                case attackLeft:
                    sprite.setCurrentFrameTop(sprite.getAttackLeftFrame());
                    break;
                case attackRight:
                    sprite.setCurrentFrameTop(sprite.getAttackRightFrame());
                    break;
                case death:
                    sprite.setCurrentFrameTop(sprite.getDeathFrame());
                    break;
            }

            if (!sprite.isFacingLeft()) {
                spriteBatch.draw(sprite.getCurrentFrameTop(), sprite.getX(), sprite.getY() + 1,
                        Sprite.SIZE, Sprite.SIZE);
            } else {
                spriteBatch.draw(sprite.getCurrentFrameTop(), sprite.getX() + Sprite.SIZE, sprite.getY() + 1,
                        -Sprite.SIZE, Sprite.SIZE);
            }
        }
    }

    private void renderStationarySprites() {
        for (StationarySprite stationarySprite : stationarySprites) {
            stationarySprite.setAnimationTime(Gdx.graphics.getDeltaTime());

            stationarySprite.setCurrentFrame(stationarySprite.getAnimationFrame());

            spriteBatch.draw(stationarySprite.getCurrentFrame(), stationarySprite.getX(), stationarySprite.getY(),
                    stationarySprite.getSize(), stationarySprite.getSize());
        }
    }

    private void renderStationarySpritesTop() {
        for (StationarySprite stationarySprite : stationarySprites) {
            spriteBatch.draw(stationarySprite.getHeadTexture(),
                    stationarySprite.getX(), stationarySprite.getY() + 1,
                    stationarySprite.getSize(), stationarySprite.getSize());
        }
    }

    // TODO Add observer pattern: Game observes View and is notified of input, then modifies the player. 
    public void getInput() {
        Axel player = (Axel) sprites.get(0);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || up) {
            sprites.get(0).setState(Sprite.State.walkFront);
            sprites.get(0).setDY(Sprite.MAX_VELOCITY);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || down) {
            sprites.get(0).setState(Sprite.State.walkBack);
            sprites.get(0).setDY(-Sprite.MAX_VELOCITY);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || left) {
            sprites.get(0).setFacingLeft(true);
            sprites.get(0).setState(Sprite.State.walkRight);
            sprites.get(0).setDX(-Sprite.MAX_VELOCITY);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || right) {
            sprites.get(0).setFacingLeft(true);
            sprites.get(0).setState(Sprite.State.walkLeft);
            sprites.get(0).setDX(Sprite.MAX_VELOCITY);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            player.setHP(player.getHP() - 1);
        }

    }
    public Vector3 getWorldPoint(float x, float y) {
        Vector3 worldPoint = new Vector3(x, y, 0);
        camera.unproject(worldPoint);

        float worldX = sprites.get(0).getX() - 15;
        float worldY = sprites.get(0).getY() - 9.375f;

        worldPoint.set(worldPoint.x + worldX, worldPoint.y + worldY, 0);

        return worldPoint;
    }

    public Stage getStage() {
        return stage;
    }

    public Array<BitmapFont> getFonts() {
        return fonts;
    }
}