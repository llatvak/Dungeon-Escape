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
import fi.tamk.rentogames.Interface.MapTutorials;
import fi.tamk.rentogames.Map.MapLevel;
import fi.tamk.rentogames.Map.MapPlayer;

public class MapScreen implements Screen {

    private DungeonEscape game;
    private MapPlayer player;
    private MapLevel mapLevel;
    private MapScreenUI userInterface;
    private MapTutorials mapTutorials;
    private boolean paused;

    // Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private Stage stage;

    private int stepTotal = 50;
    private int oldStepTotal;
    private int pauseSteps;
    private int stepsToPoint;
    private boolean pointAdded;
    private int stepsDuringPointsAdd;

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
        System.out.println("create");
        mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
        player = new MapPlayer(this, mapLevel);
        userInterface = new MapScreenUI(game, this, player);
        mapTutorials = new MapTutorials(game, userInterface);
        stage = userInterface.getStage();
        countMovementPointsOnRender();
        // userInterface.updateProgressBar();
        // stepTotal = Save.getProgressBarValue();
    }

    private void update() {
        countMovementPointsOnRender();
        player.checkAllowedMoves();

        if(!userInterface.isButtonUp()) {
            if(player.moving) {
                player.move();
            }
        }

        //  int stepDelta;
        oldStepTotal = stepTotal;
        stepTotal = game.getStepCount();
        if(stepTotal > oldStepTotal) {
            System.out.println("Actual steps: " + game.getStepCount());
            //  stepDelta = stepTotal - oldStepTotal
            //  stepsToPoint++;
            userInterface.updateStepsLabel();
//            userInterface.addProgressBarValue(stepDelta);
//            if(userInterface.getProgressbarValue() % player.STEPSTOMOVE == 0) {
//                userInterface.resetProgressBar();
//            }
//            Save.saveCurrentProgressbar(userInterface.getProgressbarValue());
        }
        checkKeyAmount();
        player.checkCollisions();

        // TODO updating only when values change not every frame
        userInterface.updateMovesLabel();
        userInterface.updateKeyLabel();

        if(!player.allowMovement) {
            userInterface.setOutOfMovesIcon();
        } else {
            userInterface.setMovesIcon();
        }
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

    private void countMovementPointsOnRender() {
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % player.STEPSTOMOVE == 0 && !pointAdded) {
                stepsDuringPointsAdd = stepTotal;
                stepsToPoint = 0;
                player.addMovementPoints();
                // userInterface.resetProgressBar();
                pointAdded = true;
            }

            // checks if step count is above the number required for points
            if(pointAdded) {
                if(stepTotal == stepsDuringPointsAdd + 1) {
                    pointAdded = false;
                }
            }
        }
    }

    private void countMovementPointsToAdd() {
        int stepsWhilePaused = countStepsDeltaOnResume();
        System.out.println("Steps while paused: " + stepsWhilePaused);
        int pointsToAdd;

        pointsToAdd = stepsWhilePaused / player.STEPSTOMOVE;
        player.addMultipleMovementPoints(pointsToAdd);
        stepsToPoint = 0;
    }

    // Save current step count
    public void saveStepsOnPause() {
        pauseSteps = stepTotal;
        System.out.println("Paused steps count: " + pauseSteps);
    }

    // Make saved steps actual step count
    public void subtractSteps() {
        stepTotal = pauseSteps;
    }

    // Get difference between total steps and saved steps
    private int countStepsDeltaOnResume() {
        int stepDelta = game.getStepCount() - pauseSteps;
        System.out.println("Stepdelta: " + stepDelta);
        return stepDelta;
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
    public void createTutorial(int tutorial) {
        mapTutorials.changeTutorialLabel(tutorial);
    }

    // Not used at the moment
//    public void addStep() {
//        stepTotal++;
//        System.out.println("Map Steps: " + stepTotal);
//        userInterface.updateStepsLabel();
//        userInterface.addProgressBarValue();
//        Save.saveCurrentProgressbar(userInterface.getProgressbarValue());

//        if(stepTotal > 10) {
//            Save.saveCurrentProgressbar(userInterface.getProgressbarValue() + 1);
//        } else {
//           Save.saveCurrentProgressbar(userInterface.getProgressbarValue());
//        }
//
//        if(!paused) {
//             stepsToPoint++;
//            //System.out.println("Steps to point: " + stepsToPoint);
//        }
//    }

    @Override
    public void show() {
        System.out.println("showi");
        InputMultiplexer multiplexer = new InputMultiplexer();
        MyInputProcessor inputProcessor = new MyInputProcessor(player);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        userInterface.createUI();
    }
    @Override
    public void hide() {
        Gdx.app.log("Mapscreen", "hidden");
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
        saveStepsOnPause();
    }

    @Override
    public void resume() {
        paused = false;
        Gdx.app.log("Mapscreen", "resume");
        countMovementPointsToAdd();
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
    public MapScreen getMapScreen() {
        return this;
    }

    public MapPlayer getMapPlayer() {
        return player;
    }

    public int getLevel() {
        return this.level;
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