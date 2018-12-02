package com.bjdev.nacom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bjdev.nacom.MainNacom;

public class MainMenuUI implements Screen {

    final MainNacom game;
    private Texture logoImg;
    private Texture nameImg;
    private Sprite logoSprite;
    private Sprite nameSprite;
    private OrthographicCamera camera;

    public MainMenuUI(final MainNacom game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        logoImg = new Texture(Gdx.files.internal("images/main_logo.png"));
        logoImg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        nameImg = new Texture(Gdx.files.internal("images/main_name.png"));
        nameImg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logoSprite = new Sprite(logoImg);
        nameSprite = new Sprite(nameImg);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(logoSprite, (camera.viewportWidth - 400)/2, camera.viewportHeight - 250, 400, 250);
        game.batch.draw(nameSprite, (camera.viewportWidth - 400)/2, camera.viewportHeight - 400, 400, 150);
        game.font.draw(game.batch, "¡Presiona para empezar a jugar!",(camera.viewportWidth - 300)/2,camera.viewportHeight - 450, 300, 1,true);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameUI(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        nameImg.dispose();
        logoImg.dispose();
    }
}
