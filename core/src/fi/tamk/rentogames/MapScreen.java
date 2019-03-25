package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;


public class MapScreen implements Screen {

    DungeonEscape game;
    MapPlayer player;
    MapLevel mapLevel;
    SpriteBatch batch;

    private boolean paused;

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
    private int oldStepTotal;
    private int savedSteps;
    private int stepsDelta;
    private int leftOverSteps;

    boolean buttonUp;
    private ProgressBar stepsProgressBar;
    private boolean resetProgressBar = false;
    private int progressbarValue = 0;

    Locale locale;
    I18NBundle myBundle;

    TmxMapLoader mapLoader;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    int level = 1;
    boolean keysCollected = true;

    public void changeLevel() {
        if(level < 4) {
            level++;
        }
        mapLevel.setLevel(level);
        changeMap();
    }

    public void changeMap() {
        Gdx.app.log("MapLevel", ": " + level);
        mapLevel.resetMap();
        mapLevel.createTiledMap();
        player.spawn(level);
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
    }


    public void onCreate() {

        batch = game.getBatch();
        mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();

        camera = game.getGameCamera();

        player = new MapPlayer(this, mapLevel);

        fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();
        fontCamera = fonts.getCamera();

        viewport = new StretchViewport(game.screenWidth, game.screenHeight, fontCamera);
        viewport.apply();

        stage = new Stage(viewport, batch);
        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        stepsProgressBar.setAnimateDuration(0.5f);

        fontCamera.position.set(fontCamera.viewportWidth / 2, fontCamera.viewportHeight / 2, 0);
        fontCamera.update();

        myBundle = DungeonEscape.getMyBundle();
        locale = DungeonEscape.getLocale();
    }


    public void update() {
        player.receiveSteps(stepTotal);
        countMovementPoints();
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
        fontRoboto.draw(batch, myBundle.get("stepcounter") + ": " + stepTotal, 50 , 640f - 40f);
        fontRoboto.draw(batch,"" + player.movementPoints, 320 , 640f - 12f);

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
        if(stepTotal > oldStepTotal){
            if(resetProgressBar) {
                progressbarValue = 0;
                resetProgressBar = false;
            } else {
                progressbarValue++;
            }

            stepsProgressBar.setValue(progressbarValue);
        }
        oldStepTotal = stepTotal;
    }


    public void moveCamera() {
        camera.position.x = player.getX()/100f + 32f/100f;
        camera.position.y = player.getY()/100f + 32f/100f;
        camera.update();

    }

    public void trapConfirm(final boolean onDown, final boolean onUp) {
        Gdx.app.log("Button", "created");
        buttonUp = true;

        final TextButton confirmButton = new TextButton(myBundle.get("readybutton"), skin, "maroon");
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

                // Using boolean values checks trapscreen
                if(onDown) {
                    goToDownTrap();
                }
                if(onUp) {
                    goToUpTrap();
                }

                player.addMovementPoint();
            }
        });
    }


    public void goToDownTrap() {
        Gdx.app.log("Down trap", "going to jumping trap");
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }
    public void goToUpTrap() {
        Gdx.app.log("Up trap", "going to crouching trap");
        // Needs new class UpScreen
        game.changeScreen(DungeonEscape.SQUATSCREEN);
    }
    public void goToStoryTile() {
        Gdx.app.log("Story", "going to story tile");
        // Needs new class StoryScreen
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }

    public void addStep() {
        stepTotal++;
        System.out.println("Steps: " + stepTotal);

        if(!paused) {
            leftOverSteps++;
            System.out.println("Steps to point: " + leftOverSteps);
        }

    }

    public void subtractStep() {
        stepTotal--;
    }

    public TiledMap getWorldMap(){
        return tiledMap;
    }

    public MapScreen getMapScreen() {
        return this;
    }

    @Override
    public void show() {
        Gdx.app.log("Show", "");
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
        paused = true;
        Gdx.app.log("Mapscreen", "paused");
        saveSteps();
    }

    @Override
    public void resume() {
        paused = false;
        Gdx.app.log("Mapscreen", "resume");
        countMovementPointsDelta();
        //System.out.println(stepTotal);
    }

    public void countMovementPoints() {
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % player.STEPSTOMOVE == 0) {
                leftOverSteps = 0;
                player.addMovementPoint();
                addStep();
                resetProgressBar = true;

            }
        }
    }


    public void countMovementPointsDelta() {
        int stepsDuringPause = getStepsDelta();
        int pointsToAdd;

        pointsToAdd = stepsDuringPause / player.STEPSTOMOVE;
        // System.out.println("added steps: " + addedSteps);
       // System.out.println("background steps: " + steps);
       // System.out.println("points to add: " + pointsToAdd);

        addMultipleMovementPoints(pointsToAdd);
        leftOverSteps = 0;
    }

    public void addMultipleMovementPoints(int points) {
        player.movementPoints = player.movementPoints + points;
    }

    public void resetSteps() {
        stepTotal = 0;
    }

    // Save current step count
    public void saveSteps() {
        savedSteps = stepTotal;
    }

    // Make saved steps actual step count
    public void subtractSteps() {
        stepTotal = savedSteps;
    }

    // Get difference between total steps and saved steps
    public int getStepsDelta() {
        stepsDelta = stepTotal - savedSteps;
        return stepsDelta;
    }

    @Override
    public void hide() {
        Gdx.app.log("Mapscreen", "hidden");
    }

    @Override
    public void dispose() {
        background.dispose();
        fontRoboto.dispose();
        player.dispose();
        tiledMap.dispose();
        batch.dispose();
        stage.dispose();
    }
}
