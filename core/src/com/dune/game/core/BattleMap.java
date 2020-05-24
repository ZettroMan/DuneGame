package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BattleMap {
    private TextureRegion grassTexture;
    private TextureRegion gemTexture;
    private TextureRegion fireTexture;
    private TextureRegion waterTexture;
    int[][] gizmos;

    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.gemTexture = Assets.getInstance().getAtlas().findRegion("gem");
        this.fireTexture = Assets.getInstance().getAtlas().findRegion("fire");
        this.waterTexture = Assets.getInstance().getAtlas().findRegion("water");

        gizmos = new int[9][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                int temp = MathUtils.random(15);
                if(temp < 3) { gizmos[j][i] = temp + 1;
                } else gizmos[j][i] = 0;
            }
        }

    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(grassTexture, i * 80, j * 80);
                int gizmo = gizmos[j][i];
                if(gizmo == 0 || gizmo > 3) continue;
                switch(gizmo) {
                    case 1:
                        batch.draw(gemTexture, i * 80 + 20, j * 80 + 20);
                        break;
                    case 2:
                        batch.draw(fireTexture, i * 80 + 20, j * 80 + 20);
                        break;
                    case 3:
                        batch.draw(waterTexture, i * 80 + 20, j * 80 + 20);
                        break;
                }
            }
        }
    }

    public int[][] getGizmos() {
        return gizmos;
    }
}
