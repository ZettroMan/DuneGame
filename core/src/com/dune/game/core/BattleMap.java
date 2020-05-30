package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BattleMap {
    private TextureRegion grassTexture;
    private TextureRegion gemTexture;
    private TextureRegion[] flameTextures;
    private TextureRegion donutsTexture;
    int[][] gizmos;
    float timePerFrame = 0.08f;
    float flameTimer = 0.0f;


    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.gemTexture = Assets.getInstance().getAtlas().findRegion("gem");
        this.flameTextures = Assets.getInstance().getAtlas().findRegion("flame").split(64, 64)[0];
        this.donutsTexture = Assets.getInstance().getAtlas().findRegion("donuts");

        gizmos = new int[9][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                int temp = MathUtils.random(15);
                if (temp < 3) {
                    gizmos[j][i] = temp + 1;
                } else gizmos[j][i] = 0;
            }
        }

    }

    public void update(float dt) {
        flameTimer += dt;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(grassTexture, i * 80, j * 80);
                int gizmo = gizmos[j][i];
                if (gizmo == 0 || gizmo > 3) continue;
                switch (gizmo) {
                    case 1:
                        batch.draw(gemTexture, i * 80 + 8, j * 80 + 8);
                        break;
                    case 2:
                        batch.draw(flameTextures[((int) (flameTimer / timePerFrame) + 3 * i + 4 * j) % flameTextures.length], i * 80 + 8, j * 80 + 8);
                        break;
                    case 3:
                        batch.draw(donutsTexture, i * 80 + 8, j * 80 + 8);
                        break;
                }
            }
        }
    }

    public int[][] getGizmos() {
        return gizmos;
    }
}
