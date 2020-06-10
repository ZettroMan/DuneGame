package com.dune.game.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.*;

import static com.dune.game.core.BattleMap.CELL_SIZE;

public class Harvester extends AbstractUnit {

    private static final float unloadSpeed = 1.0f;
    private float unloadTime;
    private boolean unloadStarted;
    private Vector2 tempVelocity;

    public Harvester(GameController gc) {
        super(gc);
        this.textures = Assets.getInstance().getAtlas().findRegion("tankcore").split(64, 64)[0];
        this.weaponTexture = Assets.getInstance().getAtlas().findRegion("harvester");
        this.containerCapacity = 50;
        this.minDstToActiveTarget = 5.0f;
        this.speed = 120.0f;
        this.weapon = new Weapon(4.0f, 1);
        this.hpMax = 500;
        this.unitType = UnitType.HARVESTER;
        this.unloadTime = 0;
        this.unloadStarted = false;
        this.tempVelocity =  new Vector2();
    }

    @Override
    public void setup(Owner ownerType, float x, float y) {
        this.position.set(x, y);
        this.ownerType = ownerType;
        this.hp = this.hpMax;
        this.destination = new Vector2(position);
    }

    public void updateWeapon(float dt) {
        if (gc.getMap().getResourceCount(position) > 0) {
            int result = weapon.use(dt);
            for (int i = 0; i < 20; i++) {
                tmp.set(MathUtils.random(position.x - 40, position.x + 40), MathUtils.random(position.y - 40, position.y + 40));
                tempVelocity.set(position).sub(tmp).rotate(25);
                gc.getParticleController().setup(tmp.x, tmp.y,
                        tempVelocity.x, tempVelocity.y, 0.3f, 0.5f, 0.2f,
                        0, 0, 1, 0.1f, 1, 1, 1, 0.4f);
            }
            if (result > -1) {
                if(container < containerCapacity) {
                    container += gc.getMap().harvestResource(position, result);
                    if (container > containerCapacity) {
                        container = containerCapacity;
                    }

                }
            }
        } else {
            weapon.reset();
        }
    }

    @Override
    public void commandAttack(Targetable target) {
        unloadStarted = false;
        commandMoveTo(target.getPosition());
    }

    @Override
    public void renderGui(SpriteBatch batch) {
        super.renderGui(batch);
        batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
        batch.draw(progressbarTexture, position.x - 15, position.y - 15,
                0, 0, 30, 9, 1.0f, 1.0f, 90.0f);
        batch.setColor(((float)container / containerCapacity), 1.0f - ((float)container / containerCapacity), 0.0f, 1.0f);
        batch.draw(progressbarTexture, position.x - 13, position.y - 13,
                -2, -2, 28 , 5, ((float)container / containerCapacity), 1.0f, 90.0f);
         if(weapon.getUsageTimePercentage() > 0.0f) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 42, 64, 9);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 44, 60 * weapon.getUsageTimePercentage(), 5);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void unloadTo(SpiceFactory factory, float dt) {
        if(!unloadStarted) {
            unloadStarted = true;
            destination.set(position);
        }
        unloadTime += dt;
        while (unloadTime > unloadSpeed) {
            unloadTime -= unloadSpeed;
            if (container > 0) {
                factory.addStock(1);
                container--;
            }  else {
                unloadTime = 0.0f;
                return;
            }
        }
        for (int k = 0; k < 30; k++) {
            tmp.set(factory.getPosition()).sub(position).nor().scl(200);
            gc.getParticleController().setup(
                    MathUtils.random(position.x - 10, position.x + 10), MathUtils.random(position.y - 10, position.y + 10),
                    MathUtils.random(tmp.x - 10, tmp.x + 10), MathUtils.random(tmp.y - 10, tmp.y + 10),
                    0.4f, 1.0f, 0.2f,
                    0, 0.2f, 1, 1, 0, 0, 1, 0.6f);
        }

    }
}
