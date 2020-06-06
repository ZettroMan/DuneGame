package com.dune.game.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.Owner;
import com.dune.game.core.units.UnitType;


public class AiLogic {
    private GameController gc;

    public AiLogic(GameController gc) {
        this.gc = gc;
    }

    public void update(float dt) {
        for (int i = 0; i < gc.getUnitsController().getAiUnits().size(); i++) {
            unitProcessing(gc.getUnitsController().getAiUnits().get(i));
        }
    }

    public void unitProcessing(AbstractUnit unit) {
        if (unit.isUnderAttack() && !unit.isReacted()) {
            if (unit.getUnitType() == UnitType.HARVESTER) {
                // move to random point
                unit.commandAvoidTo(new Vector2(MathUtils.random(80, 1200), MathUtils.random(80, 640)));
                return;
            }
            if (unit.getUnitType() == UnitType.BATTLE_TANK) {
                if (Math.random() < 0.3f) {
                    // move to random point
                    unit.commandAvoidTo(new Vector2(MathUtils.random(80, 1200), MathUtils.random(80, 640)));
                    return;
                }
                // attack the enemy (enemy is a player :=))
                unit.commandAttack(unit.getAttacker());
                return;
            }
        }

        // in normal circumstances
        if (unit.getUnitType() == UnitType.HARVESTER) {
            float checkResourceX;
            float checkResourceY;
            float optimum = 0.0f;
            float measure;
            Vector2 maxResourcePlace = new Vector2();
            Vector2 temp = new Vector2(0, 0);

            // search for optimal nearest resource cell
            for (int checkRow = 0; checkRow < BattleMap.ROWS_COUNT; checkRow++) {
                for (int checkCol = 0; checkCol < BattleMap.COLUMNS_COUNT; checkCol++) {
                    checkResourceX = ((float) checkCol + 0.5f) * BattleMap.CELL_SIZE;
                    checkResourceY = ((float) checkRow + 0.5f) * BattleMap.CELL_SIZE;
                    temp.set(checkResourceX, checkResourceY);
                    measure = ((float)gc.getMap().getResourceCount(temp)/ unit.getPosition().dst2(temp));
                    if (measure > optimum) {
                        optimum = measure;
                        maxResourcePlace.set(temp);
                    }
                }
            }
            unit.commandMoveTo(maxResourcePlace);
            return;
        }
        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            AbstractUnit nearestEnemyUnit = gc.getUnitsController().getNearestPlayerUnit(unit.getPosition());
            if (nearestEnemyUnit != null) {
                unit.commandAttack(nearestEnemyUnit);
            }

        }
    }
}
