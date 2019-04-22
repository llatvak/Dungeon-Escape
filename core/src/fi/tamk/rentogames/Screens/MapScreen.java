package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.MyInputProcessor;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Interface.MapScreenUI;
import fi.tamk.rentogames.Interface.MapTutorials;
import fi.tamk.rentogames.Interface.Story;
import fi.tamk.rentogames.Map.MapLevel;
import fi.tamk.rentogames.Map.MapPlayer;

/**
 * Brings together and renders all the elements for map screen.
 *
 *<p>
 * Renders tiled map, story and tutorial windows, player character and user interface.
 * Counts the movement points from the step count and displays both in UI.
 * Implements interface screen which is used to switch screens when necessary.
 *</p>
 *
 * @author Miko Kauhanen
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class MapScreen implements Screen {

    private DungeonEscape game;

    private MapPlayer player;

    private MapLevel mapLevel;

    private MapScreenUI userInterface;

    private Stage stage;

    private OrthogonalTiledMapRenderer tiledMapRenderer;

    /**
     * Tutorial windows
     */
    private MapTutorials mapTutorials;

    /**
     * Story windows
     */
    private Story story;

    /**
     * Total step amount
     */
    private int stepTotal;

    /**
     * Step amount when game rendering is paused
     */
    private int pauseSteps;

    /**
     * Has movement point been added
     */
    private boolean pointAdded;

    /**
     * Step amount when point is added
     */
    private int stepsDuringPointsAdd;

    /**
     * Current level. Starts from first level.
     */
    private int level = 1;

    /**
     * Amount of collectable keys needed to progress to next level
     */
    public static final int KEYS_NEEDED = 3;

    /**
     * Current collected key amount
     */
    public int keyAmount;

    /**
     * Whether all required keys have been collected
     */
    public boolean keysCollected;

    /**
     * Are trap confirmation buttons up
     */
    public boolean trapButtonsUp;


    /**
     * Constructor that receives the main game class.
     *
     *<p>
     * Receives the main game object {@link DungeonEscape}. Calls create method.
     *</p>
     * @param game main game object
     */
    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    /**
     * Creates necessary objects for this screen when it is created.
     *
     *<p>
     * Creates new level, map renderer, player, user interface, tutorials and story windows.
     *</p>
     */
    private void onCreate() {
        mapLevel = new MapLevel(game);
        tiledMapRenderer = mapLevel.getTiledMapRenderer();

        player = new MapPlayer(this, mapLevel);
        userInterface = new MapScreenUI(game, this, player);
        mapTutorials = new MapTutorials(game, userInterface);
        story = new Story(game, userInterface);
        stage = userInterface.getStage();

        if(Save.getStepAmount() > 0) {
            countPausedMovementpoints();
        }
    }

    /**
     * Calls user interaction methods during render call.
     *
     *<p>
     * Updates movement points count, step amount, key amount.
     * Checks whether player has moved and colliding with any tiled map objects.
     * Updates UI elements with changing values and icons.
     *</p>
     */
    private void update() {
        // Adds movement points
        addMovementPointsOnRender();
        // Checks if player can move
        player.checkAllowedMoves();

        // If no button is up allow player movement
        if(!userInterface.isButtonUp()) {
            player.move();
        }

        // Sets
        int oldStepTotal = stepTotal;
        stepTotal = game.getStepCount();

        if(stepTotal > oldStepTotal) {
            userInterface.updateStepsLabel();
            if(stepsAfterPointsAdd < player.STEPSTOMOVE) {
                 stepsAfterPointsAdd = stepTotal - stepsDuringPointsAdd;
            } else {
                stepsAfterPointsAdd = 0;
            }

        }
        checkKeyAmount();
        player.checkCollisions();

        // TODO updating only when values change not every frame
        userInterface.updateMovesLabel();
        userInterface.updateKeyLabel();

        if(!player.allowMovement) {
            if(!userInterface.outOfMovesWindowUp) {
                userInterface.createOutOfMovesWindow();
            }
            userInterface.setOutOfMovesIcon();

        } else {
            userInterface.setMovesIcon();
            userInterface.outOfMovesWindowUp = false;
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

        // View screen camera
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

    /**
     * Moves current camera.
     *
     *<p>
     * Centers camera's x and y position to player's position.
     *</p>
     */
    private void moveCamera() {
        game.getGameCamera().position.x = player.getX()/100f + 32f/100f;
        game.getGameCamera().position.y = player.getY()/100f + 32f/100f;
        game.getGameCamera().update();
    }

    /**
     * Adds movement points when over the threshold.
     *
     *<p>
     * Adds movement points every time step amount is divisible by ten.
     * Only allows one movement point addition until one step is added.
     *</p>
     */
    private int stepsAfterPointsAdd;

    private void addMovementPointsOnRender() {
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % player.STEPSTOMOVE == 0 && !pointAdded) {
                stepsDuringPointsAdd = stepTotal;
                player.addMovementPoints();
                pointAdded = true;
            }

            // checks if step count is above the number required for points
            if(pointAdded) {
                if(stepTotal > stepsDuringPointsAdd) {
                    pointAdded = false;
                }
            }
        }
    }

    /**
     * Adds movement points from steps gathered while paused.
     *
     *<p>
     * Divides step amount while paused with amount needed to add movement points.
     * Adds movement points based on this number.
     *</p>
     */
    private void countAfterPauseMovementPoints() {
        int stepsWhilePaused = countStepsDeltaOnResume() + stepsAfterPointsAdd;
        int pointsToAdd;

        pointsToAdd = stepsWhilePaused / player.STEPSTOMOVE;
        player.addMovementPoints(pointsToAdd);
        if(pointsToAdd > 0) {
            stepsAfterPointsAdd = 0;
        }
    }

    /**
     * Saves current step amount before pause.
     */
    void saveStepsOnPause() {
        pauseSteps = stepTotal;
    }

    /**
     * Sets saved steps to actual step amount
     */
    void setPauseSteps() {
        stepTotal = pauseSteps;
    }

    /**
     * Counts delta between actual steps and steps while paused.
     *
     * @return pause steps and actual steps difference
     */
    private int countStepsDeltaOnResume() {
        int stepDelta = game.getStepCount() - pauseSteps;
        return stepDelta;
    }

    /**
     * Checks if player has enough keys.
     *
     * <p>
     * Checks if player has collected enough keys to pass to next level.
     * Changes keysCollected to true when enough keys.
     * </p>
     */
    private void checkKeyAmount() {
        keysCollected = keyAmount >= KEYS_NEEDED;
    }

    /**
     * Changes level.
     *
     * <p>
     * Saves current level and changes level to next level. Plays sounds when changing level.
     * Changes map to that levels map.
     * </p>
     */
    public void changeLevel() {
        if(Save.getCurrentLevel() < 10) {
            level = Save.getCurrentLevel() + 1;
            GameAudio.playSound("dooropensound", Save.getCurrentAudioSetting());
        } else {
            story.createStoryPart(6);
            GameAudio.playSound("dooropensound", Save.getCurrentAudioSetting());
        }
        Save.saveCurrentLevel(level);
        mapLevel.setLevel(level);
        changeMap();
    }

    /**
     * Changes map.
     *
     * <p>
     * Creates and changes tiled map to for next level.
     * Resets keys, UI elements and spawns player to map starting location.
     * </p>
     */
    private void changeMap() {
        keyAmount = 0;
        userInterface.updateKeyLabel();
        keysCollected = false;
        mapLevel.resetMap();
        mapLevel.createTiledMap();
        player.setMap();
        player.spawn(level);
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
    }

    /**
     * Creates trap buttons.
     *
     * <p>
     * Creates buttons to accept of deny entering movement screen.
     * Changes boolean to disallow player movement when on top of a trap.
     * </p>
     *
     * @param onSquat is player on squatting trap
     * @param onJump is player on jumping trap
     */
    public void trapConfirm(final boolean onSquat, final boolean onJump) {
        trapButtonsUp = true;
        userInterface.createConfirmButtons(onSquat,onJump);
    }

    /**
     * Changes to squat screen.
     *
     * <p>
     * Changes screen to squatting exercise screen.
     * </p>
     */
    public void goToSquatTrap() {
        game.changeScreen(DungeonEscape.SQUATSCREEN);
    }

    /**
     * Changes to jump screen.
     *
     * <p>
     * Changes screen to jumping exercise screen.
     * </p>
     */
    public void goToJumpTrap() {
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }

    /**
     * Creates story window.
     *
     * <p>
     * Creates story UI window when standing on a tiled map object.
     * Window content changes depending on the current story part.
     * </p>
     *
     * @param part current story part
     */
    public void createStoryWindow(int part) {
        story.createStoryPart(part);
    }

    /**
     * Created tutorial window.
     *
     * <p>
     * Creates tutorial UI window when standing on a tiled map object.
     * Window content changes depending on the current tutorial.
     * </p>
     *
     * @param tutorial current tutorial
     */
    public void createTutorial(int tutorial) {
        mapTutorials.createTutorial(tutorial);
    }


    /**
     * Counts paused movement points using steps calculated during game paused.
     */
    private void countPausedMovementpoints() {
        int pausedPoints = Save.getStepAmount() / 10;
        player.addMovementPoints(pausedPoints);
        Save.saveCurrentSteps(0);
    }

    @Override
    public void show() {
        userInterface.setBackButtonInitialized(false);
        if(MainMenu.getResetButtonInitialized()) {
            mapLevel.setLevel(Save.getCurrentLevel());
            player.movementPoints = Save.getMovementPoints();
            changeMap();
            player.setTexture(player.getPlayerUpTexture());
            MainMenu.setResetButtonInitialized(false);
        }
        GameAudio.playMusic("mapscreenmusic");
        GameAudio.setMusicVolume("mapscreenmusic", Save.getCurrentAudioSetting());
        GameAudio.loopMusic("mapscreenmusic");
        InputMultiplexer multiplexer = new InputMultiplexer();
        MyInputProcessor inputProcessor = new MyInputProcessor(player);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        userInterface.createUI();
    }

    @Override
    public void hide() {
        userInterface.setBackButtonInitialized(true);
        GameAudio.stopMusic("mapscreenmusic");
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
        saveStepsOnPause();
        Save.saveCurrentSteps(getStepTotal());
    }

    @Override
    public void resume() {
        countAfterPauseMovementPoints();
    }

    public int getKeyAmount() {
        return keyAmount;
    }

    public int getStepTotal() {
        return stepTotal;
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