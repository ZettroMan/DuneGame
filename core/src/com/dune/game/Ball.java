package com.dune.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Ball {
    private Vector2 position;
    private Texture texture;
    private float angle;
    private float speed;
    private float acceleration;
    private float accuDist;
    private int srcPosition;
    private boolean flipX;
    private boolean flipY;
    private boolean collapse;
    private float scale;

    public Ball() {
        Init();
    }

    private void Init() {
        float x = (float) (Math.random() * 1140 + 32);
        float y = (float) (Math.random() * 600 + 32);
        this.position = new Vector2(x, y);
        this.texture = new Texture("Ball.png");
        this.speed = (float) (100.0f + 100.0f * Math.random());
        this.angle = (float) (Math.random() * 360);
        this.acceleration = -45.0f;
        this.flipX = false;
        this.flipY = false;
        collapse = false;
        scale = 1.0f;
    }

    public void update(float dt) {
        if (collapse) {
            scale -= 1.5f * dt;
            angle -= 240.0f * dt;
            if (scale < 0.0f) {
                Init();
            }
            return;
        }
        speed += acceleration * dt;
        accuDist += speed * dt * 0.28f * (flipX ? -1 : 1);
        if (accuDist >= 32) accuDist = 0;
        if (accuDist < 0) accuDist = 31.1f;
        srcPosition = (int) accuDist;
        if (speed < 0) speed = 0.0f;
        if (speed > 0) {
            position.x += speed * MathUtils.cosDeg(angle) * dt;
            position.y += speed * MathUtils.sinDeg(angle) * dt;
        }
        if (position.x < 32) {
            position.x = 32;
            angle = 180.0f - angle;
            flipX = !flipX;
            flipY = !flipY;
        }
        if (position.y < 32) {
            position.y = 32;
            angle = -angle;
            flipY = !flipY;
        }
        if (position.x > 1248) {
            position.x = 1248;
            angle = 180.0f - angle;
            flipX = !flipX;
            flipY = !flipY;
        }
        if (position.y > 688) {
            position.y = 688;
            angle = -angle;
            flipX = !flipX;
            flipY = !flipY;
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.2f, 0.2f, 0.2f, 0.35f);
        batch.draw(texture, position.x - 24, position.y - 38, 32, 32, 64, 64, scale, scale, angle, (srcPosition - 8 * (srcPosition / 8)) * 64, srcPosition / 8 * 64 - 1, 64, 64, flipX, flipY);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle, (srcPosition - 8 * (srcPosition / 8)) * 64, srcPosition / 8 * 64 - 1, 64, 64, flipX, flipY);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void dispose() {
        texture.dispose();
    }

    public void collapse() {
        collapse = true;
    }
}
