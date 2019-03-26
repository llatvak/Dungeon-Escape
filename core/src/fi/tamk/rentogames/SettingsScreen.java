package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SettingsScreen implements Screen {

    private Stage stage;
    private DungeonEscape game;

    private Texture background;
    private Skin skin;

    private I18NBundle myBundle;

    SettingsScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        background = new Texture("settings.png");

        skin = new Skin( Gdx.files.internal("uiskin.json") );

        this.stage = new Stage(new StretchViewport(game.screenWidth, game.screenHeight, game.getScreenCamera()));

        myBundle = DungeonEscape.getMyBundle();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        //Create Table
        Table menuTable = new Table();
        //Set table to fill stage
        menuTable.setFillParent(true);
        // Debug lines
        menuTable.setDebug(false);
        //Set alignment of contents in the table.
        menuTable.center();

        //Create buttons

        TextButton tutorialButton = new TextButton(myBundle.get("howtoplaybutton"), skin);
        TextButton soundButton = new TextButton(myBundle.get("soundbutton"), skin);
        TextButton musicButton = new TextButton(myBundle.get("musicbutton"), skin);
        TextButton creditsButton = new TextButton(myBundle.get("creditbutton"), skin);
        TextButton exitButton = new TextButton(myBundle.get("exitbutton"), skin);
        TextButton backButton = new TextButton(myBundle.get("backbutton"), skin);

        soundButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Settings", "Tutorial");
            }
        });

        soundButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Settings", "Sound");
            }
        });

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Settings", "Music");
            }
        });

        creditsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Settings", "Credits");
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(DungeonEscape.MAINMENU);
            }
        });

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(DungeonEscape.BACK);
            }
        });


        int buttonWidth = 200;
        int buttonHeight = 40;
        int betweenPadding = 10;

        //Add buttons to table
        menuTable.row().pad(0,0,0,0);
        menuTable.add(tutorialButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        menuTable.row().pad(betweenPadding,0,0,0);
        menuTable.add(soundButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        menuTable.row().pad(betweenPadding,0,0,0);
        menuTable.add(musicButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        menuTable.row().pad(betweenPadding,0,0,0);
        menuTable.add(creditsButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        menuTable.row().pad(20,0,0,0);
        menuTable.add(exitButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        menuTable.row().pad(betweenPadding,0,0,0);
        menuTable.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        //Add table to stage
        stage.addActor(menuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.getScreenCamera().combined);

        game.batch.begin();
        game.batch.draw(background,0,0, game.screenWidth, game.screenHeight);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}

