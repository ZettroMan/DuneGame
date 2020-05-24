package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {
    private final SpriteBatch batch;
    private final GameController gc;

    public WorldRenderer(SpriteBatch batch, GameController gc) {
        this.batch = batch;
        this.gc = gc;
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        gc.getMap().render(batch);
        gc.getTank().render(batch);
        batch.end();
    }
}
