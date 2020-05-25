package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank extends GameObject {
    private Vector2 destination;
    private TextureRegion[] textures;
    private TextureRegion[][] shotTextures;
    private float angle;
    private float speed;
    private float rotationSpeed;
    private boolean justFired;
    private float shotTimer;
    private Vector2 shotPosition;
    private float moveTimer;
    private float timePerFrame;

    public Tank(GameController gc, float x, float y) {
        super(gc);
        this.position.set(x, y);
        this.destination = new Vector2(position);
        this.textures = Assets.getInstance().getAtlas().findRegion("tankanim").split(64, 64)[0];
        this.shotTextures = Assets.getInstance().getAtlas().findRegion("cannonshot").split(64, 64);
        this.speed = 120.0f;
        this.timePerFrame = 0.08f;
        this.rotationSpeed = 90.0f;
        this.justFired = false;
        this.shotPosition = new Vector2();
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            if (Math.abs(angle - angleTo) > 3.0f) {
                if (angle > angleTo) {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle -= rotationSpeed * dt;
                    } else {
                        angle += rotationSpeed * dt;
                    }
                } else {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle += rotationSpeed * dt;
                    } else {
                        angle -= rotationSpeed * dt;
                    }
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }

            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            position.mulAdd(tmp, dt);
            if (position.dst(destination) < 120.0f && Math.abs(angleTo - angle) > 10) {
                position.mulAdd(tmp, -dt);
            }
        }
        if (justFired) {
            shotTimer += 4 * dt;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            fire();
        }
        checkBounds();
    }

    public void fire() {
        tmp.set(position).add(32 * MathUtils.cosDeg(angle), 32 * MathUtils.sinDeg(angle));
        shotPosition.set(position).add(52 * MathUtils.cosDeg(angle), 52 * MathUtils.sinDeg(angle));
        gc.getProjectilesController().setup(tmp, angle);
        justFired = true;
        shotTimer = 0.0f;
    }

    public void checkBounds() {
        if (position.x < 40) {
            position.x = 40;
        }
        if (position.y < 40) {
            position.y = 40;
        }
        if (position.x > 1240) {
            position.x = 1240;
        }
        if (position.y > 680) {
            position.y = 680;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
        if (justFired) {
            if (shotTimer / timePerFrame > 24) {
                justFired = false;
            } else {
                int shotIndex = ((int) (shotTimer / timePerFrame)) % 25;
                batch.draw(shotTextures[shotIndex / 5][shotIndex - ((shotIndex / 5) * 5)], shotPosition.x - 36, shotPosition.y - 26, 32, 32, 64, 64, 1, 1, angle);
            }
        }
    }
}
