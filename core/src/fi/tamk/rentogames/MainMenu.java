package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainMenu implements Screen {

    private Stage stage;
    //private SpriteBatch batch;
    private DungeonEscape game;

    private  Texture background;
    private Skin skin;

    private I18NBundle myBundle;

    MainMenu(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        background = new Texture("menu.png");

        //atlas = new TextureAtlas("skin.atlas");
        skin = new Skin( Gdx.files.internal("uiskin.json") );

        this.stage = new Stage(new StretchViewport(game.screenWidth, game.screenHeight, game.getScreenCamera()));
    }

    @Override
    public void show() {
        myBundle = DungeonEscape.getMyBundle();

        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table topTable = new Table();

        //Set table to fill stage
        topTable.setFillParent(true);
        // Debug lines
        topTable.setDebug(false);

        //Set alignment of contents in the table.
        topTable.top();
        topTable.left();

        //Create buttons
        TextButton langFinButton = new TextButton("FIN", skin);
        TextButton langEngButton = new TextButton("ENG", skin);

        //Add listeners to buttons
        langFinButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "Finnish");
                DungeonEscape.setLanguage("fi", "FI", "MyBundle_fi_FI");
                myBundle = DungeonEscape.getMyBundle();
                setLocaleFin();
            }
        });

        langEngButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "English");
                DungeonEscape.setLanguage("en", "US", "MyBundle_en_US");
                myBundle = DungeonEscape.getMyBundle();
                setLocaleEng();
            }
        });

        //Add buttons to table
        topTable.row().pad(10,5,0,5);
        topTable.add(langFinButton).width(32).height(32).fillX().uniformX();
        topTable.add(langEngButton).width(32).height(32).fillX().uniformX();


        //Add table to stage
        stage.addActor(topTable);

        gameInit();
    }

    private void gameInit() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(false);
        mainTable.center();

        TextButton playButton = new TextButton(myBundle.get("playbutton"), skin);
        TextButton settingsButton = new TextButton(myBundle.get("settingsbutton"), skin);
        TextButton exitButton = new TextButton(myBundle.get("exitbutton"), skin);

        playButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(DungeonEscape.MAPSCREEN);
            }
        });

        settingsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setPreviousScreen(DungeonEscape.MAINMENU);
                game.changeScreen(DungeonEscape.SETTINGSSCREEN);
            }
        });

        exitButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        mainTable.row().pad(0,0,10,0);
        mainTable.add(playButton).width(200).height(70).fillX().uniformX();

        mainTable.row().pad(10,0,10,0);
        mainTable.add(settingsButton).width(200).height(40).fillX().uniformX();

        mainTable.row();
        mainTable.add(exitButton).width(200).height(40).fillX().uniformX();

        stage.addActor(mainTable);
    }

    private void setLocaleFin() {
        gameInit();
    }

    private void setLocaleEng() {
        gameInit();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.getScreenCamera().combined);

        game.batch.begin();
        game.batch.draw(background,0,0, game.screenWidth, game.screenHeight);
        game.batch.end();

        stage.act(delta);
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