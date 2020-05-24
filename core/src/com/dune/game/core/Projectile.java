package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private final Vector2 position;
    private final Vector2 velocity;
    private final float width;
    private final float height;
    private float angle;
    private boolean visible;
    private final TextureRegion texture;

    public Projectile(TextureAtlas atlas) {
        visible = false;
        texture = atlas.findRegion("bullet");
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
        position = new Vector2();
        velocity = new Vector2();
    }

    public void fire(Vector2 startPosition, float angle) {
        position.set(startPosition);
        // сделал скорость 200, чтобы танк не обгонял свои снаряды)
        velocity.set(200.0f * MathUtils.cosDeg(angle), 200.0f * MathUtils.sinDeg(angle));
        this.angle = angle;
        visible = true;
    }

    public void update(float dt) {
        if (!visible) return;
        position.mulAdd(velocity, dt);
        if (position.x > 1280 | position.x < 0 | position.y > 720 | position.y < 0)
            visible = false;
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }

    public boolean isVisible() {
        return visible;
    }
}
