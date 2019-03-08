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
	int stepCount;

	// Fonts
	BitmapFont fontRoboto;
	GlyphLayout layout;
	FreeTypeFontGenerator fontGenerator;
	Stage stage;

    @Override
    public void create () {
        batch = new SpriteBatch();
        MapScreen mapScreen = new MapScreen(this);
        MoveScreen moveScreen = new MoveScreen(this, mapScreen);
        createSmartFonts();
//        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 12;
//        parameter.borderColor = Color.BLACK;
//        parameter.borderWidth = 3;
//        fontRoboto = fontGenerator.generateFont(parameter); // Generates BitmapFont
//
//        layout = new GlyphLayout();

        //setScreen(moveScreen);
        setScreen(mapScreen);
    }
    public void createSmartFonts() {
        SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.local("Roboto-Regular.ttf");
        BitmapFont fontSmall = fontGen.createFont(exoFile, "exo-small", 24);
        BitmapFont fontMedium = fontGen.createFont(exoFile, "exo-medium", 48);
        BitmapFont fontLarge = fontGen.createFont(exoFile, "exo-large", 64);

        stage = new Stage();

        Label.LabelStyle smallStyle = new Label.LabelStyle();
        smallStyle.font = fontSmall;
        Label.LabelStyle mediumStyle = new Label.LabelStyle();
        mediumStyle.font = fontMedium;
        Label.LabelStyle largeStyle = new Label.LabelStyle();
        largeStyle.font = fontLarge;

        Label small = new Label("Steps: " + stepCount, smallStyle);
//        Label medium = new Label("Medium Font", mediumStyle);
//        Label large = new Label("Large Font", largeStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.align(Align.topLeft);
        stage.addActor(table);

        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

        table.add(small).row();
//        table.add(medium).row();
//        table.add(large).row();
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

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose () {
        batch.dispose();
        fontRoboto.dispose();
    }
}


