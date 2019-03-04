package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DungeonEscape extends Game {
    SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();
        MoveScreen moveScreen = new MoveScreen(this);
        setScreen(moveScreen);
    }

    @Override
    public void render () {
        super.render();
        batch.begin();
        batch.end();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}