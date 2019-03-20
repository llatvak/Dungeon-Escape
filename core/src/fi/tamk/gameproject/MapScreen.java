package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MapScreen implements Screen {

    final float WORLD_WIDTH = 360f / 100f;
    final float WORLD_HEIGHT = 640f / 100f;

    DungeonEscape game;
    MapPlayer player;
    SpriteBatch batch;
    MoveScreen moveScreen;


    // Camera
    OrthographicCamera camera;
    OrthographicCamera fontCamera;
    Viewport viewport;
    // Map
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    // Textures
    Texture background;

    // Fonts
    private Fonts fonts;
    private BitmapFont fontRoboto;

    protected Skin skin;
    private Stage stage;

    private int stepTotal;

    boolean buttonUp;
    ProgressBar stepsProgressBar;
    boolean resetProgressBar = false;
    int value = 0;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();

    }

    public void onCreate() {
        batch = game.getBatch();
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        background = new Texture("brickwall.png");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // Can these be created in Fonts() -class?
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, 360f, 640f);

        player = new MapPlayer(this);

        fonts = new Fonts();
        fonts.createMediumFont();
        fontRoboto = fonts.getFont(Fonts.MEDIUM);


        viewport = new FitViewport(360f, 640f, fontCamera);
        viewport.apply();

        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );

        stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        stepsProgressBar.setAnimateDuration(1f);

        stage = new Stage(viewport, batch);

        fontCamera.position.set(fontCamera.viewportWidth / 2, fontCamera.viewportHeight / 2, 0);
        fontCamera.update();

    }

    public void update() {

        stepTotal = game.getStepTotal();
        player.receiveSteps(stepTotal);
        player.countMovementPoints();
        player.checkAllowedMoves();

        if(!buttonUp) {
            if(player.moving) {
                player.move();
            }
        }

        player.checkCollisions();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fontCamera.update();

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);
        // Render all layers to screen.
        tiledMapRenderer.render();

        // View font camera
        batch.setProjectionMatrix(fontCamera.combined);

        batch.begin();

        // Draw fonts
        fontRoboto.draw(batch, "STEPS: " + stepTotal, 50 , 640f - 40f);
        fontRoboto.draw(batch,"" + player.movementPoints, 320 , 640f - 12f);

        fontRoboto.draw(batch,"|", 360f / 2f - 5f, 640f / 2f + 70f);
        fontRoboto.draw(batch,"<                    >", 360f / 2 - 80f , 640f / 2f);
        fontRoboto.draw(batch,"|", 360f / 2f - 5f, 640f / 2f - 60f);

        // View game camera
        batch.setProjectionMatrix(camera.combined);

        batch.draw(player.getTexture(),
                camera.position.x - player.getTexture().getWidth()/100f/2,
                camera.position.y - player.getTexture().getHeight()/100f/2,
                player.getTexture().getWidth()/100f,
                player.getTexture().getHeight()/100f);
        batch.end();


        moveCamera();
        update();


        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();

        // Update progress bar
        updateProgressBar();

    }
    public void updateProgressBar() {
        if(game.stepTotal > game.oldStepTotal){
            if(resetProgressBar) {
                value = 0;
                resetProgressBar = false;
            } else {
                value++;
            }

            stepsProgressBar.setValue(value);
        }
        game.oldStepTotal = game.stepTotal;
    }


    public void moveCamera() {
        camera.position.x = player.getX()/100f + 32f/100f;
        camera.position.y = player.getY()/100f + 32f/100f;
        camera.update();

    }

    public void trapConfirm() {
        Gdx.app.log("Button", "created");
        buttonUp = true;

        final TextButton confirmButton = new TextButton("I'm ready!", skin, "maroon");
        confirmButton.setWidth(160f);
        confirmButton.setHeight(70f);
        confirmButton.setPosition(360f / 2 - 80f, 640f / 2 - 110f);

        stage.addActor(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.log("Trap", "going");

                // what if player doesn't want to go to trap?
                // this needs to be changed for different traps
                confirmButton.remove();
                buttonUp = false;
                goToDownTrap();
                player.addMovementPoint();


            }
        });
    }


    public void goToDownTrap() {
        Gdx.app.log("Down trap", "going to jumping trap");
        game.changeScreen(DungeonEscape.MOVESCREEN);
    }
    public void goToUpTrap() {
        Gdx.app.log("Up trap", "going to crouching trap");
        // Needs new class UpScreen
        game.changeScreen(DungeonEscape.MOVESCREEN);
    }
    public void goToStoryTile() {
        Gdx.app.log("Story", "going to story tile");
        // Needs new class StoryScreen
        game.changeScreen(DungeonEscape.MOVESCREEN);
    }

    public void addStep() {
        game.stepTotal++;
    }

    public void subtractStep() {
        game.stepTotal--;
    }

    public TiledMap getWorldMap(){
        return tiledMap;
    }

    public MapScreen getMapScreen() {
        return this;
    }

    @Override
    public void show() {

        InputMultiplexer multiplexer = new InputMultiplexer();
        MyInputProcessor inputProcessor = new MyInputProcessor(player);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        //Create Table
        Table topTable = new Table();
        //Set table to fill stage
        topTable.setFillParent(true);

        // Debug lines
        //topTable.setDebug(true);

        //Set alignment of contents in the table.
        topTable.top();
        topTable.left();

        //Create buttons and bars
        ImageButton settingsButton = new ImageButton(skin, "settings");


        //Add listeners to buttons
        settingsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.log("Settings", "going to settings");
                //((Game)Gdx.app.getApplicationListener()).setScreen(new SettingsScreen(game));
                game.setPreviousScreen(DungeonEscape.MAPSCREEN);
                game.changeScreen(DungeonEscape.SETTINGSSCREEN);
            }
        });

        stage.addActor(settingsButton);
        stage.addActor(stepsProgressBar);


       //Add buttons and progress bar to table
        topTable.add(settingsButton).width(30).fillX().fillY().height(30).pad(5,5,5,5);
        topTable.add(stepsProgressBar).width(260).fillX().fillY().pad(5,5,5,5);
        topTable.row();

        //Add table to stage
        stage.addActor(topTable);
    }
    @Override
    public void resize(int width, int height) {
        // update this
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
        background.dispose();
        fontRoboto.dispose();
        player.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
