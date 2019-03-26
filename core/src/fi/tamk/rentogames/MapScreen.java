package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class MapScreen implements Screen {

    private DungeonEscape game;
    private MapPlayer player;
    private MapLevel mapLevel;

    private boolean paused;

    // Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private BitmapFont fontRoboto;

    private Skin skin;
    private Stage stage;

    private int stepTotal;
    private int oldStepTotal;
    private int savedSteps;
    private int leftOverSteps;

    private int level = 1;
    private final int KEYS_NEEDED = 3;
    int keyAmount;
    boolean keysCollected = true;

    boolean buttonUp;
    private ProgressBar stepsProgressBar;
    private boolean resetProgressBar = false;
    private int progressbarValue = 0;

    MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();

        player = new MapPlayer(this, mapLevel);

        // Fonts
        Fonts fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();

        this.stage = new Stage(game.getFontViewport());

        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        stepsProgressBar.setAnimateDuration(0.5f);
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

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(game.getGameCamera());
        // Render all layers to screen.
        tiledMapRenderer.render();

        // View font camera
        game.batch.setProjectionMatrix(game.getScreenCamera().combined);

        game.batch.begin();

        // Draw fonts
        fontRoboto.draw(game.batch, game.getMyBundle().get("stepcounter") + ": " + stepTotal, 10f , game.screenHeight - 40f);
        fontRoboto.draw(game.batch,"" + player.movementPoints, 320 , game.screenHeight - 12f);
        fontRoboto.draw(game.batch,game.getMyBundle().get("keys") + ": " + keyAmount + "/3", 10f , game.screenHeight - 70f);

        // View game camera
        game.batch.setProjectionMatrix(game.getGameCamera().combined);

        game.batch.draw(player.getTexture(),
                game.getGameCamera().position.x - player.getTexture().getWidth()/100f/2,
                game.getGameCamera().position.y - player.getTexture().getHeight()/100f/2,
                player.getTexture().getWidth()/100f,
                player.getTexture().getHeight()/100f);
        game.batch.end();


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
        game.getGameCamera().position.x = player.getX()/100f + 32f/100f;
        game.getGameCamera().position.y = player.getY()/100f + 32f/100f;
        game.getGameCamera().update();
    }

    private void checkKeyAmount() {
        keysCollected = keyAmount == KEYS_NEEDED;
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

        final TextButton confirmButton = new TextButton(game.getMyBundle().get("readybutton"), skin, "maroon");
        confirmButton.setWidth(160f);
        confirmButton.setHeight(70f);
        confirmButton.setPosition(game.screenWidth / 2 - 80f, game.screenHeight / 2 + 50f);

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
        // Updates the stage viewport where font is
        stage.getViewport().update(width, height);
        // Updates game viewport
        game.getGameViewport().update(width, height);
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
        player.dispose();
        stage.dispose();
        tiledMapRenderer.dispose();
        mapLevel.dispose();
        game.dispose();
        skin.dispose();
    }
}