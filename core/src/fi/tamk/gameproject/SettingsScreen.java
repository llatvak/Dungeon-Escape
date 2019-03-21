package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SettingsScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private DungeonEscape game;

    private Viewport viewport;
    private OrthographicCamera camera;

    private Texture background;
    protected Skin skin;

    public SettingsScreen(DungeonEscape game) {
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
        TextButton langButton = new TextButton("Language", skin);
        TextButton notificationButton = new TextButton("Notifications", skin);
        TextButton soundButton = new TextButton("Sound", skin);
        TextButton musicButton = new TextButton("Music", skin);
        TextButton meterButton = new TextButton("Sensitivity", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        TextButton exitButton = new TextButton("Exit to menu", skin);
        TextButton backButton = new TextButton("Back", skin);


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



        int buttonWidth = 250;
        int buttonHeight = 50;
        int topPadding = 10;

        //Add buttons to table
        menuTable.add(langButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(notificationButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(soundButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(musicButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(meterButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(creditsButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(exitButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);

        menuTable.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        menuTable.row().pad(topPadding,0,0,0);



        //Add table to stage
        stage.addActor(menuTable);
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
        background.dispose();
    }
}

