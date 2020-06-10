package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.Owner;
import com.dune.game.core.units.UnitType;

public class PlayerLogic {
    private GameController gc;
    private int money;
    private int unitsCount;
    private int unitsMaxCount;

    public int getMoney() {
        return money;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public int getUnitsMaxCount() {
        return unitsMaxCount;
    }

    public PlayerLogic(GameController gc) {
        this.gc = gc;
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                AbstractUnit u = gc.getSelectedUnits().get(i);
                if (u.getOwnerType() == Owner.PLAYER) {
                    unitProcessing(u);
                }
            }
        }
        // я сделал сбор накопленных на фабриках ресурсов в автоматическом режиме.
        // По идее правильнее это было бы сделать в ручном режиме при наведении
        // на фабрику и, например, щелчком правой клавиши мыши. Позже сделаю.
        for (int i = 0; i < gc.getFactories().size() ; i++) {
            money += gc.getFactories().get(i).flushStock();
        }
    }

    public void unitProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            unit.commandMoveTo(gc.getMouse());
            return;
        }
        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            AbstractUnit aiUnit = gc.getUnitsController().getNearestAiUnit(gc.getMouse());
            if (aiUnit == null) {
                unit.commandMoveTo(gc.getMouse());
            } else {
                unit.commandAttack(aiUnit);
            }
        }
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public boolean spendMoney(int money) {
        if(this.money < money) return false;
        this.money -= money;
        return true;
    }
}
