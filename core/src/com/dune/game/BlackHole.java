package com.dune.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class BlackHole {
    private Vector2 position;
    private Texture texture;
    private float scale;
    private float angle;
    private boolean appears;
    private boolean collapse;

    public BlackHole() {
        Init();
    }

    private void Init() {
        float x = (float) (Math.random() * 1140 + 60);
        float y = (float) (Math.random() * 600 + 60);
        this.position = new Vector2(x, y);
        this.texture = new Texture("BlackHole.png");
        scale = 0;
        appears = false;
        collapse = false;
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
        if (!appears) {
            scale += 0.7f * dt;
            angle += 240.0f * dt;
            if (scale > 1.0f) {
                appears = true;
                scale = 1.0f;
            }
        } else {
            angle += 10.0f * dt;
            scale = 1.0f + 0.15f * MathUtils.cosDeg(angle * 6);
        }

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 60, position.y - 60, 60, 60, 120, 120, scale, scale, angle, 0, 0, 120, 120, false, false);
    }

    public void dispose() {
        texture.dispose();
    }

    public void collapse() {
        collapse = true;
    }

    public Vector2 getPosition() {
        return position;
    }
}
