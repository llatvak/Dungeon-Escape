package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DungeonEscape extends Game {
	SpriteBatch batch;
	int stepCount;

	// Fonts
	BitmapFont fontRoboto;
	GlyphLayout layout;
	FreeTypeFontGenerator fontGenerator;

    @Override
    public void create () {
        batch = new SpriteBatch();
        MapScreen mapScreen = new MapScreen(this);
        MoveScreen moveScreen = new MoveScreen(this, mapScreen);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        fontRoboto = fontGenerator.generateFont(parameter); // Generates BitmapFont

        layout = new GlyphLayout();

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
        this.stepCount = stepCount;
    }

    public int getStepCount() {
        return stepCount;
    }

    public BitmapFont getFont() {
        //System.out.println("getting font");
        return fontRoboto;
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    @Override
    public void dispose () {
        batch.dispose();
        fontRoboto.dispose();
    }
}


