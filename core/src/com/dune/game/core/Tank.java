package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank extends GameObject implements Poolable {
    public enum Owner {
        PLAYER, AI
    }

    private Owner ownerType;
    private Weapon weapon;
    private Vector2 destination;
    private Vector2 nextPosition;
    private TextureRegion[] textures;
    private TextureRegion progressbarTexture;
    private int hp;
    private float angle;
    private float speed;
    private float rotationSpeed;

    private float moveTimer;
    private float timePerFrame;
    private int container;
    private int containerSize;

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Tank(GameController gc) {
        super(gc);
        this.progressbarTexture = Assets.getInstance().getAtlas().findRegion("progressbar");
        this.timePerFrame = 0.08f;
        this.rotationSpeed = 90.0f;
        activated = false;
        containerSize = 50;
        destination = new Vector2();
        nextPosition = new Vector2();
    }

    public void setup(Owner ownerType, float x, float y) {
        this.textures = Assets.getInstance().getAtlas().findRegion("tankanim").split(64, 64)[0];
        this.position.set(x, y);
        this.nextPosition.set(position);
        this.destination.set(position);
        this.ownerType = ownerType;
        this.speed = 120.0f;
        this.hp = 100;
        this.weapon = new Weapon(Weapon.Type.HARVEST, 3.0f, 1);
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void update(float dt, TanksController tc) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && activated) {
            destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            if (Math.abs(angle - angleTo) > 3.0f) {
                if (angle > angleTo) {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle -= rotationSpeed * dt;
                    } else {
                        angle += rotationSpeed * dt;
                    }
                } else {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle += rotationSpeed * dt;
                    } else {
                        angle -= rotationSpeed * dt;
                    }
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }

            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            nextPosition.set(position).mulAdd(tmp, dt);
            if (nextPosition.dst(destination) < 120.0f && Math.abs(angleTo - angle) > 10) {
                nextPosition.mulAdd(tmp, -dt);
            }
            checkBounds();

            // сначала реализовал коллизии на уровне ячеек мапы (BattleMap), каждой ячейке добавил boolean occupied
            // и проверял его в апдейте. При движении танка также одни ячейки освобождал, другие занимал. Но все это
            // оказалось невероятно муторным и ужасным решением))
            // Поэтому переделал на такой вариант. Конечно же в таком виде (метод сквозного перебора) он будет
            // очень неэффективен для большого количества объектов (танков). Поэтому при необходимости должен быть
            // подвергнут оптимизации (например через поиск ближайших). Но тогда все танки нужно будет организовать
            // в древовидную упорядоченную структуру типа BST и др. И на это мне пока просто не хватило времени и опыта...
            // А так-то, в нашем упрощенном случае эта схема работает, не суперидеально, конечно же, но работает.)
            for (Tank tank : tc.getActiveList()) {
                if (this == tank) continue;
                if (tank.getPosition().dst(nextPosition) < 75.0f) {
                    nextPosition.set(position);
                    moveTimer -= dt;    //останавливаем гусеницы танка (анимацию)
                    break;
                }
            }
            position.set(nextPosition);
        }
        updateWeapon(dt);
    }

    public void updateWeapon(float dt) {
        if ((weapon.getType() == Weapon.Type.HARVEST) && !isFullUp()) {
            if (gc.getMap().getResourceCount(this) > 0) {
                int result = weapon.use(dt);
                if (result > -1) {
                    container += gc.getMap().harvestResource(this, result);
                    if (container > containerSize) container = containerSize;
                }
            } else {
                weapon.reset();
            }
        }
    }

    public boolean isFullUp() {
        return container >= containerSize;
    }

    public void checkBounds() {
        if (nextPosition.x < 40) {
            nextPosition.x = 40;
        }
        if (nextPosition.y < 40) {
            nextPosition.y = 40;
        }
        if (nextPosition.x > 1240) {
            nextPosition.x = 1240;
        }
        if (nextPosition.y > 680) {
            nextPosition.y = 680;
        }
    }

    public void render(SpriteBatch batch) {
        if (activated) batch.setColor(1.0f, 0.7f, 0.7f, 1.0f);
        batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
        if (activated) batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (weapon.getType() == Weapon.Type.HARVEST) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 32, 64, 9);
            batch.setColor(((float) container / containerSize), 1.0f - ((float) container / containerSize), 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 34, 60 * container / containerSize, 5);
            if (weapon.getUsageTimePercentage() > 0.0f) {
                batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
                batch.draw(progressbarTexture, position.x - 32, position.y + 42, 64, 9);
                batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
                batch.draw(progressbarTexture, position.x - 30, position.y + 44, 60 * weapon.getUsageTimePercentage(), 5);
            }
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
