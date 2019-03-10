package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DungeonEscape extends Game {
    int screenResolutionWidth;
    int screenResolutionHeight;

	SpriteBatch batch;
	int stepTotal;

    @Override
    public void create () {

        // Get device screen resolution
        screenResolutionWidth = Gdx.graphics.getWidth();
        screenResolutionHeight = Gdx.graphics.getHeight();
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        MapScreen mapScreen = new MapScreen(this);
        MoveScreen moveScreen = new MoveScreen(this, mapScreen);

        //setScreen(moveScreen);
        setScreen(mapScreen);
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

    public void receiveSteps(int stepCount) {
        System.out.println("Steps: " + stepCount);
        this.stepTotal = stepCount;
    }

    public int getStepTotal() {
        return stepTotal;
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}


