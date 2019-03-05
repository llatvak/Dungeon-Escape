package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DungeonEscape extends Game {
	SpriteBatch batch;

	// Fonts
	BitmapFont fontRoboto;
	GlyphLayout layout;
	FreeTypeFontGenerator fontGenerator;

    @Override
    public void create () {
        batch = new SpriteBatch();
        MoveScreen moveScreen = new MoveScreen(this);
        MapScreen mapScreen = new MapScreen(this);
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

    @Override
    public void dispose () {
        batch.dispose();
    }
}


