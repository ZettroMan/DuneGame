package com.dune.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.core.Assets;

public class MenuScreen extends AbstractScreen {

    private Stage stage;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        createGuiAndPrepareGameInput();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenManager.getInstance().resetCamera();
        stage.draw();

    }

    public void update(float dt) {
        stage.act(dt);
   }

    public void createGuiAndPrepareGameInput() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), ScreenManager.getInstance().getBatch());
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        BitmapFont font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("simpleButton"), null, null, font72);
        final TextButton playBtn = new TextButton("Play", textButtonStyle);
        final TextButton exitBtn = new TextButton("Exit", textButtonStyle);
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        Group menuGroup = new Group();
        playBtn.setPosition(0, 250);
        exitBtn.setPosition(0, 120);
        menuGroup.addActor(playBtn);
        menuGroup.addActor(exitBtn);
        menuGroup.setPosition(450, 200);
        stage.addActor(menuGroup);
        skin.dispose();
    }


    @Override
    public void dispose() {
    }
}