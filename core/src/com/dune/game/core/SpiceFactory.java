package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.Owner;

public class SpiceFactory extends GameObject {
    private TextureRegion factoryTexture;
    private int stock;
    private Owner ownerType;

    public SpiceFactory(GameController gc, Owner ownerType,  Vector2 position) {
        super(gc);
        this.ownerType = ownerType;
        this.position.set(position);
        this.stock = 0;
        this.factoryTexture = Assets.getInstance().getAtlas().findRegion("factory");
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 1.0f);
        batch.draw(factoryTexture, position.x - 80, position.y - 80, 0, 0, 320, 320, 0.5f, 0.5f, 0);
    }

    public void addStock(int resourceAmount)    {
        stock += resourceAmount;
    }

    public int getStock() {
        return stock;
    }

    public  int flushStock() {
        int retval = stock;
        stock = 0;
        return retval;
    }

    public Owner getOwnerType() {
        return ownerType;
    }
}
