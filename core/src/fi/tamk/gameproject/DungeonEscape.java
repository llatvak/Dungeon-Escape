package fi.tamk.gameproject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Map;

public class DungeonEscape extends Game {

	SpriteBatch batch;

	// Fonts
	BitmapFont fontRoboto;
	GlyphLayout layout;
	FreeTypeFontGenerator fontGenerator;

	@Override
	public void create () {
		batch = new SpriteBatch();
		MapScreen mapScreen = new MapScreen(this);
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
	}
}
