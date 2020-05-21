package com.dune.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Tank {
    private final Vector2 position;
    private final Texture texture;
    private float angle;
    private float speed;
    private final float acceleration;

    public Tank(float x, float y) {
        this.position = new Vector2(x, y);
        this.texture = new Texture("Tank.png");
        this.speed = 0.0f;
        this.acceleration = 200.0f;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angle -= 180.0f * dt;
        }
        float startspeed = 200.0f;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (speed == 0) speed = startspeed;
            speed += acceleration * dt;
        } else if (speed > 0) {
            speed -= acceleration * dt;
            if (speed < 0) speed = 0;
        }
        if (speed > 0) {
            position.x += speed * MathUtils.cosDeg(angle) * dt;
            position.y += speed * MathUtils.sinDeg(angle) * dt;
        }
        if (position.x < 30) {
            position.x = 30;
            speed = startspeed;
        }
        if (position.y < 30) {
            position.y = 30;
            speed = startspeed;
        }
        if (position.x > 1250) {
            position.x = 1250;
            speed = startspeed;
        }
        if (position.y > 690) {
            position.y = 690;
            speed = startspeed;
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.2f, 0.2f, 0.2f, 0.3f);
        batch.draw(texture, position.x - 34, position.y - 44, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
        batch.draw(texture, position.x - 36, position.y - 43, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
        batch.draw(texture, position.x - 38, position.y - 42, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public void dispose() {
        texture.dispose();
    }
}
