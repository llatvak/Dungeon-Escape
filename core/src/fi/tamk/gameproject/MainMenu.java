package fi.tamk.gameproject;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private DungeonEscape game;

    private Viewport viewport;
    private OrthographicCamera camera;

    private  Texture background;
    protected Skin skin;

    public MainMenu(DungeonEscape game) {
        this.batch = game.getBatch();
        this.game = game;
        onCreate();
    }

    public void onCreate() {

        background = new Texture("menu.png");

        camera = new OrthographicCamera();
        viewport = new FitViewport(360f, 640f, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();


        //atlas = new TextureAtlas("skin.atlas");
        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stage = new Stage(viewport, batch);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table topTable = new Table();
        Table mainTable = new Table();

        //Set table to fill stage
        topTable.setFillParent(true);
        mainTable.setFillParent(true);
        // Debug lines
        topTable.setDebug(false);
        mainTable.setDebug(false);

        //Set alignment of contents in the table.
        topTable.top();
        topTable.left();
        mainTable.center();

        //Create buttons
        TextButton langFinButton = new TextButton("FIN", skin);
        TextButton langEngButton = new TextButton("ENG", skin);

        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //Add listeners to buttons

        langFinButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "Finnish");
            }
        });

        langEngButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "English");
            }
        });

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

        //Add buttons to table
        topTable.row().pad(10,5,0,5);
        topTable.add(langFinButton).width(32).height(32).fillX().uniformX();
        topTable.add(langEngButton).width(32).height(32).fillX().uniformX();


        mainTable.row().pad(0,0,10,0);
        mainTable.add(playButton).width(200).height(70).fillX().uniformX();

        mainTable.row().pad(10,0,10,0);
        mainTable.add(settingsButton).width(200).height(40).fillX().uniformX();

        mainTable.row();
        mainTable.add(exitButton).width(200).height(40).fillX().uniformX();

        //Add table to stage
        stage.addActor(topTable);
        stage.addActor(mainTable);
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
