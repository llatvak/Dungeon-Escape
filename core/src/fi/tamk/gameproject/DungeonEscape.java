package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DungeonEscape extends Game {
    int screenResolutionWidth;
    int screenResolutionHeight;

	SpriteBatch batch;
	int stepTotal;
    MapScreen mapScreen;

    @Override
    public void create () {

        // Get device screen resolution
        screenResolutionWidth = Gdx.graphics.getWidth();
        screenResolutionHeight = Gdx.graphics.getHeight();
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        setScreen( new MainMenu(this) );
        //setScreen( new MoveScreen(this, mapScreen ));
        //setScreen( new MapScreen(this) );
    }

    @Override
    public void render () {
        super.render();
        batch.begin();
        batch.end();
    }

    public void changeScreen(int screen) {
        if(screen == 1) {
            setScreen(mapScreen);
        }
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


