package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
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

public class MapScreen implements Screen {

    private DungeonEscape game;
    private MapPlayer player;
    private SpriteBatch batch;
    private MapLevel mapLevel;

    private boolean paused;

    // Camera
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;

    // Map
    TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private BitmapFont fontRoboto;

    private Skin skin;
    private Stage stage;

    private int stepTotal;
    private int oldStepTotal;
    private int savedSteps;
    private int leftOverSteps;


    int level = 1;
    private final int KEYS_NEEDED = 3;
    int keyAmount;
    boolean keysCollected = true;

    boolean buttonUp;
    private ProgressBar stepsProgressBar;
    private boolean resetProgressBar = false;
    private int progressbarValue = 0;

    private I18NBundle myBundle;

    MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        batch = game.getBatch();
        MapLevel mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();

        camera = game.getGameCamera();

        player = new MapPlayer(this, mapLevel);

        // Fonts
        Fonts fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();
        fontCamera = fonts.getCamera();

        Viewport viewport = new StretchViewport(game.screenWidth, game.screenHeight, fontCamera);
        viewport.apply();

        stage = new Stage(viewport, batch);
        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        stepsProgressBar.setAnimateDuration(0.5f);

        fontCamera.position.set(fontCamera.viewportWidth / 2, fontCamera.viewportHeight / 2, 0);
        fontCamera.update();

        myBundle = DungeonEscape.getMyBundle();
    }

    private void update() {
        player.receiveSteps(stepTotal);
        countMovementPoints();
        player.checkAllowedMoves();

        if(!buttonUp) {
            if(player.moving) {
                player.move();
            }
        }

        checkKeyAmount();
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
        fontRoboto.draw(batch, myBundle.get("stepcounter") + ": " + stepTotal, 10f , 640f - 40f);
        fontRoboto.draw(batch,"" + player.movementPoints, 320 , 640f - 12f);
        fontRoboto.draw(batch,myBundle.get("keys") + ": " + keyAmount + "/3", 10f , 640f - 70f);

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

    private void updateProgressBar() {
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

    private void moveCamera() {
        camera.position.x = player.getX()/100f + 32f/100f;
        camera.position.y = player.getY()/100f + 32f/100f;
        camera.update();
    }

    private void checkKeyAmount() {
        if(keyAmount == KEYS_NEEDED) {
            keysCollected = true;
        } else {
            keysCollected = false;
        }
    }

    void notEnoughKeys() {
        System.out.println("No keys");
    }

    void changeLevel() {
        if(level < 3) {
            level++;
        }
        mapLevel.setLevel(level);
        changeMap();
    }

    private void changeMap() {
        Gdx.app.log("MapLevel", ": " + level);
        keyAmount = 0;
        keysCollected = false;
        mapLevel.resetMap();
        mapLevel.createTiledMap();
        player.setMap();
        player.spawn();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
    }

    void trapConfirm(final boolean onSquat, final boolean onJump) {
        Gdx.app.log("Button", "created");
        buttonUp = true;

        final TextButton confirmButton = new TextButton(myBundle.get("readybutton"), skin, "maroon");
        confirmButton.setWidth(160f);
        confirmButton.setHeight(70f);
        confirmButton.setPosition(360f / 2 - 80f, 640f / 2 + 50f);

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
                if(onSquat) {
                    goToSquatTrap();
                }
                if(onJump) {
                    goToJumpTrap();
                }
                player.addMovementPoint();
            }
        });
    }


    private void goToSquatTrap() {
        Gdx.app.log("Down trap", "going to crouching trap");
        game.changeScreen(DungeonEscape.SQUATSCREEN);
    }
    private void goToJumpTrap() {
        Gdx.app.log("Up trap", "going to jumping trap");
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }
    private void goToStoryScreen() {
        Gdx.app.log("Story", "going to story screen");
        // Needs new class StoryScreen
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }

    void addStep() {
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

    MapScreen getMapScreen() {
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

    private void countMovementPoints() {
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

    private void countMovementPointsDelta() {
        int stepsDuringPause = getStepsDelta();
        int pointsToAdd;

        pointsToAdd = stepsDuringPause / player.STEPSTOMOVE;
        // System.out.println("added steps: " + addedSteps);
       // System.out.println("background steps: " + steps);
       // System.out.println("points to add: " + pointsToAdd);

        addMultipleMovementPoints(pointsToAdd);
        leftOverSteps = 0;
    }

    private void addMultipleMovementPoints(int points) {
        player.movementPoints = player.movementPoints + points;
    }

    public void resetSteps() {
        stepTotal = 0;
    }

    // Save current step count
    void saveSteps() {
        savedSteps = stepTotal;
    }

    // Make saved steps actual step count
    void subtractSteps() {
        stepTotal = savedSteps;
    }

    // Get difference between total steps and saved steps
    private int getStepsDelta() {
        return stepTotal - savedSteps;
    }

    @Override
    public void hide() {
        Gdx.app.log("Mapscreen", "hidden");
    }

    @Override
    public void dispose() {
        fontRoboto.dispose();
        player.dispose();
        tiledMap.dispose();
        batch.dispose();
        stage.dispose();
    }
}
