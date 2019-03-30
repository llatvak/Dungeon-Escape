package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.MyInputProcessor;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Interface.MapScreenUI;
import fi.tamk.rentogames.Map.MapLevel;
import fi.tamk.rentogames.Map.MapPlayer;

public class MapScreen implements Screen {

    private DungeonEscape game;
    private MapPlayer player;
    private MapLevel mapLevel;
    private MapScreenUI userInterface;
    private boolean paused;

    // Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private Stage stage;

    private int stepTotal = 0;
    private int oldStepTotal;
    private int savedSteps;
    private int leftOverSteps;

    private int level = 1;
    public static final int KEYS_NEEDED = 3;

    public int keyAmount;
    public boolean keysCollected = true;

    public boolean buttonUp;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }
    private void onCreate() {
        mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
        player = new MapPlayer(this, mapLevel);
        userInterface = new MapScreenUI(game, this, player);
        stage = userInterface.getStage();

        countMovementPoints();
        userInterface.updateProgressBar();
        stepTotal = Save.getProgressBarValue();
    }

    private void update() {
        player.receiveSteps(stepTotal);
        countMovementPoints();
        player.checkAllowedMoves();

        if(!userInterface.isButtonUp()) {
            if(player.moving) {
                player.move();
            }
        }

        checkKeyAmount();
        player.checkCollisions();

        // TODO updating only when values change not every frame
        userInterface.updateMovesLabel();
        userInterface.updateKeyLabel();
        userInterface.updateStepsLabel();
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
    }

    private void moveCamera() {
        game.getGameCamera().position.x = player.getX()/100f + 32f/100f;
        game.getGameCamera().position.y = player.getY()/100f + 32f/100f;
        game.getGameCamera().update();
    }

    private void checkKeyAmount() {
        keysCollected = keyAmount >= KEYS_NEEDED;
    }

    public void notEnoughKeys() {
        System.out.println("No keys");
    }

    public void changeLevel() {
        if(Save.getCurrentLevel() < 3) {
            level = Save.getCurrentLevel() + 1;
        } else {
            level = 1;
        }
        Save.saveCurrentLevel(level);
        mapLevel.setLevel(level);
        changeMap();
    }
    private void changeMap() {
        Gdx.app.log("MapLevel", ": " + level);
        keyAmount = 0;
        userInterface.updateKeyLabel();
        keysCollected = false;
        mapLevel.resetMap();
        mapLevel.createTiledMap();
        player.setMap();
        player.spawn();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
    }
    public void trapConfirm(final boolean onSquat, final boolean onJump) {
        Gdx.app.log("Button", "created");
        buttonUp = true;
        userInterface.createConfirmButtons(onSquat,onJump);
    }
    public void goToSquatTrap() {
        Gdx.app.log("Down trap", "going to crouching trap");
        game.changeScreen(DungeonEscape.SQUATSCREEN);
    }
    public void goToJumpTrap() {
        Gdx.app.log("Up trap", "going to jumping trap");
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }
    public void goToStoryScreen() {
        Gdx.app.log("Story", "going to story screen");
        // Needs new class StoryScreen
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }

    public void addStep() {
        stepTotal++;
        System.out.println("Steps: " + stepTotal);
        userInterface.updateStepsLabel();
        userInterface.checkProgressBar();

        if(stepTotal > 10) {
            Save.saveCurrentProgressbar(userInterface.getProgressbarValue() + 1);
        } else {
            Save.saveCurrentProgressbar(userInterface.getProgressbarValue());
        }

        if(!paused) {
            leftOverSteps++;
            //System.out.println("Steps to point: " + leftOverSteps);
        }
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

        userInterface.createUI();
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
                userInterface.resetProgressBar();
                addStep();
            }
        }
    }

    private void countMovementPointsDelta() {
        int stepsDuringPause = getStepsDelta();
        int pointsToAdd;

        pointsToAdd = stepsDuringPause / player.STEPSTOMOVE;
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
    public void saveSteps() {
        savedSteps = stepTotal;
    }

    // Make saved steps actual step count
    public void subtractSteps() {
        stepTotal = savedSteps;
    }

    // Get difference between total steps and saved steps
    private int getStepsDelta() {
        return stepTotal - savedSteps;
    }

    public int getKeyAmount() {
        return keyAmount;
    }

    public int getStepTotal() {
        return stepTotal;
    }

    public void setStepTotal(int stepTotal) {
        this.stepTotal = stepTotal;
    }

    public void setOldStepTotal(int oldStepTotal) {
        this.oldStepTotal = oldStepTotal;
    }

    public int getOldStepTotal() {
        return oldStepTotal;
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
    }
}