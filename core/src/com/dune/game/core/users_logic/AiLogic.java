package com.dune.game.core.users_logic;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.Building;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public class AiLogic extends BaseLogic {
    private float timer;
    List<Vector2> occupiedCells;


    private List<Harvester> tmpAiHarvesters;
    private List<BattleTank> tmpAiBattleTanks;
    private List<Harvester> tmpPlayerHarvesters;
    private List<BattleTank> tmpPlayerBattleTanks;

    public AiLogic(GameController gc) {
        this.gc = gc;
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.ownerType = Owner.AI;
        this.tmpAiHarvesters = new ArrayList<>();
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
        this.timer = 10000.0f;
        this.occupiedCells = new ArrayList<>();
    }

    public void update(float dt) {
        timer += dt;
        if (timer > 2.0f) {
            timer = 0.0f;
            gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);
            gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(), UnitType.HARVESTER);
            gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(), UnitType.BATTLE_TANK);
            for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
                BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
                aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerBattleTanks));
            }
            occupiedCells.clear();
            for (int i = 0; i < tmpAiHarvesters.size(); i++) {
                Harvester aiHarvester = tmpAiHarvesters.get(i);
                int cellX = aiHarvester.getCellX();
                int cellY = aiHarvester.getCellY();
                occupiedCells.add(aiHarvester.getDestination());
                if (aiHarvester.getHealth() - aiHarvester.getFullness() < 0.01f) {
                    // it's time to go to base!
                    // first - find nearest AI-base
                    float minDistance = 10000.0f;
                    float checkDistance;
                    Vector2 foundBase = null;
                    // использовал foreach, так как зданий у нас совсем немного
                    for (Building building : gc.getBuildingsController().getActiveList()) {
                        if (building.getType() == Building.Type.STOCK && building.getOwnerLogic() == this) {
                            checkDistance = building.getEntrancePosition().dst(aiHarvester.getPosition());
                            if (checkDistance < minDistance) {
                                minDistance = checkDistance;
                                foundBase = building.getEntrancePosition();
                            }
                        }
                    }
                    // then - go to found base
                    if (foundBase != null) {
                        aiHarvester.commandMoveTo(foundBase);
                    }
                } else if (gc.getMap().getResourceCount(aiHarvester.getPosition()) == 0) {
                    // find nearest and largest resource
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
                            measure = ((float) gc.getMap().getResourceCount(checkCol, checkRow) / aiHarvester.getPosition().dst2(temp));
                            if (measure > optimum && !aiHarvester.getDestination().equals(temp) && !occupiedCells.contains(temp))
                            {
                                optimum = measure;
                                maxResourcePlace.set(temp);
                            }
                        }
                    }
                    if (optimum > 0.0f) {
                        aiHarvester.commandMoveTo(maxResourcePlace);
                        occupiedCells.add(maxResourcePlace);
                    }
                }
                // else - do nothing, just continue staying still and harvest resources
            }
        }
    }

    public <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentTank, List<T> possibleTargetList) {
        T target = null;
        float minDist = 1000000.0f;
        for (int i = 0; i < possibleTargetList.size(); i++) {
            T possibleTarget = possibleTargetList.get(i);
            float currentDst = currentTank.getPosition().dst(possibleTarget.getPosition());
            if (currentDst < minDist) {
                target = possibleTarget;
                minDist = currentDst;
            }
        }
        return target;
    }
}


