package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TanksController extends ObjectPool<Tank> {
    private GameController gc;
    private boolean clickAction;
    private Vector2 clickPos;

    @Override
    protected Tank newObject() {
        return new Tank(gc);
    }

    public TanksController(GameController gc) {
        this.gc = gc;
        clickPos = new Vector2();
    }

    public void render(SpriteBatch batch) {
        for (Tank tank : activeList) {
            tank.render(batch);
        }
    }

    public void setup(float x, float y, Tank.Owner ownerType) {
        Tank t = getActiveElement();
        t.setup(ownerType, x, y);
    }

    public void update(float dt) {
        clickAction = false;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            clickPos.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            clickAction = true;
        }

        for (Tank tank : activeList) {
            if (clickAction) {
                if (tank.getPosition().dst(clickPos) < 30.0f) {
                    tank.activate();
                } else {
                    tank.deactivate();
                }
            }
            tank.update(dt, this);
        }
        checkPool();
    }

}
