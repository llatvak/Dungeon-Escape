package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DungeonEscape extends Game {
	SpriteBatch batch;
	int stepTotal;

    @Override
    public void create () {
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


