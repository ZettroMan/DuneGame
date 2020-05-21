package com.dune.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DuneGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Tank tank;
    private Ball ball;
    private BlackHole hole;

    @Override
    public void create() {
        batch = new SpriteBatch();
        hole = new BlackHole();
        ball = new Ball();
        tank = new Tank(200, 200);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        hole.render(batch);
        ball.render(batch);
        tank.render(batch);
        batch.end();
    }

    public void update(float dt) {
        hole.update(dt);
        tank.update(dt);
        if (tank.getPosition().dst(ball.getPosition()) < 50.0f) {
            ball.setAngle((new Vector2(ball.getPosition())).sub(tank.getPosition()).angle());
            ball.setSpeed(tank.getSpeed());
        }
        ball.update(dt);

        if (ball.getPosition().dst(hole.getPosition()) < 100.0f) {
            Vector2 vector2 = (new Vector2(ball.getPosition())).sub(hole.getPosition());
            vector2.scl(0.9f);
            vector2.rotate(40.0f);
            ball.setPosition((new Vector2(hole.getPosition())).add(vector2));
            ball.collapse();
            hole.collapse();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        tank.dispose();
        ball.dispose();
        hole.dispose();
    }

    // Домашнее задание:
    // - Задать координаты точки, и нарисовать в ней круг (любой круг, радиусом пикселей 50)
    // - Если "танк" подъедет вплотную к кругу, то круг должен переместиться в случайную точку
    // - * Нельзя давать танку заезжать за экран
}