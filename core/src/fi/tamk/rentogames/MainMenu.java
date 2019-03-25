package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

public class MainMenu implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private DungeonEscape game;

    private Viewport viewport;
    private OrthographicCamera camera;

    private  Texture background;
    protected Skin skin;

    Locale locale;
    I18NBundle myBundle;

    TextButton playButton;
    TextButton settingsButton;
    TextButton exitButton;

    Table topTable;
    Table mainTable;


    public MainMenu(DungeonEscape game) {
        this.batch = game.getBatch();
        this.game = game;
        onCreate();
    }

    public void onCreate() {

        background = new Texture("menu.png");

        camera = new OrthographicCamera();
        viewport = new StretchViewport(360f, 640f, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();


        //atlas = new TextureAtlas("skin.atlas");
        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stage = new Stage(viewport, batch);


    }

    @Override
    public void show() {
        locale = DungeonEscape.getLocale();
        myBundle = DungeonEscape.getMyBundle();

        Gdx.input.setInputProcessor(stage);

        //Create Table
        topTable = new Table();

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
                locale = DungeonEscape.getLocale();
                myBundle = DungeonEscape.getMyBundle();
                setLocaleFin();
            }
        });

        langEngButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "English");
                DungeonEscape.setLanguage("en", "US", "MyBundle_en_US");
                locale = DungeonEscape.getLocale();
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

    public void gameInit() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(false);
        mainTable.center();

        playButton = new TextButton(myBundle.get("playbutton"), skin);
        settingsButton = new TextButton(myBundle.get("settingsbutton"), skin);
        exitButton = new TextButton(myBundle.get("exitbutton"), skin);

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

    public void setLocaleFin() {
        gameInit();
    }

    public void setLocaleEng() {
        gameInit();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background,0,0, 360,640);

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        batch.dispose();
    }
}