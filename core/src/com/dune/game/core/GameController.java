package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameController {
    private BattleMap map;
    private ProjectilesController projectilesController;
    private Tank tank;

    public Tank getTank() {
        return tank;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public BattleMap getMap() {
        return map;
    }

    // Инициализация игровой логики
    public GameController() {
        Assets.getInstance().loadAssets();
        this.map = new BattleMap();
        this.projectilesController = new ProjectilesController(this);
        this.tank = new Tank(this, 200, 200);;
    }

    public void update(float dt) {
        tank.update(dt);
        projectilesController.update(dt);
        checkCollisions(dt);
    }

    public void checkCollisions(float dt) {
        int tankTileRow = MathUtils.floor( tank.getPosition().y / 80);
        int tankTileCol = MathUtils.floor( tank.getPosition().x / 80);

        float tankTileCenterY = tankTileRow * 80 + 40;
        float tankTileCenterX = tankTileCol * 80 + 40;

        if(tank.getPosition().dst(tankTileCenterX, tankTileCenterY) < 30) {
            map.getGizmos()[tankTileRow][tankTileCol] = 0;
        }
    }
}
