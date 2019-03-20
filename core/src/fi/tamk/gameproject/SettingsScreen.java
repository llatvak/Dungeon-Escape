package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SettingsScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private DungeonEscape game;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TextureAtlas atlas;
    protected Skin skin;

    public SettingsScreen(DungeonEscape game) {
        this.batch = game.getBatch();
        this.game = game;
        onCreate();
    }

    public void onCreate() {

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
        Gdx.input.setCatchBackKey(true);
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);

        // Debug lines
        mainTable.setDebug(false);

        //Set alignment of contents in the table.
        mainTable.center();


        //Create buttons
        TextButton playButton = new TextButton("placeholder1", skin);
        TextButton settingsButton = new TextButton("placeholder2", skin);
        TextButton backButton = new TextButton("Back", skin);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                game.changeScreen(DungeonEscape.BACK);
            }
        });

        //Add buttons to table
        mainTable.add(playButton).width(200).height(70).fillX().uniformX();
        mainTable.row().pad(10,0,10,0);
        mainTable.add(settingsButton).width(200).height(40).fillX().uniformX();
        mainTable.row();
        mainTable.add(backButton).width(200).height(40).fillX().uniformX();

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

