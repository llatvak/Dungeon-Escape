package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Interface.MoveScreenUI;
import fi.tamk.rentogames.Interface.MoveTutorials;
import fi.tamk.rentogames.Move.MoveScreenMove;

/**
 * Class for jump move screen in game.
 *
 * <p>
 * Extends {@link MoveScreenMove} to inherit all common methods and variables for move screens.
 * This class is used to control the data and actions happened in jump screen.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveScreenJump extends MoveScreenMove implements Screen {

    /**
     * Used to create user interface for jump screen.
     */
    private MoveScreenUI userInterface;
    /**
     * Used to create tutorials for jump screen.
     */
    private MoveTutorials tutorials;
    /**
     * Used to draw objects from stage in scene2D.
     */
    private Stage stage;

    /**
     * Texture for spike in jump screen.
     */
    private Texture spikeTexture;
    // Spike attributes
    /**
     * Jump screen's spike x position.
     */
    private float spikeX;
    /**
     * Jump screen's spike y position.
     */
    private float spikeY;
    /**
     * Jump screen's spike width.
     */
    private float spikeWidth;
    /**
     * Jump screen's spike height.
     */
    private float spikeHeight;

    /**
     * Constructor to create jump screen.
     *
     * <p>
     * Receives the game from {@link DungeonEscape} and map screen to switch screen from {@link MapScreen}.
     * Calls {@link MoveScreenMove} to use common methods and variables for move screens in game.
     * Calls create method to create all jump screen specific information.
     * </p>
     *
     * @param game game received from main class to get access to all methods
     * @param mapScreen object to switch screens when jump is done
     */
    public MoveScreenJump(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreate();
    }

    /**
     * Method to create jump screen.
     *
     * <p>
     * Creates user interface from {@link MoveScreenUI} and tutorials from {@link MoveTutorials}.
     * Sets stage in scene2D for user interface and sets spike texture.
     * Sets position and size to spike in jump screen.
     * </p>
     *
     */
    // Sets up the world for box2D and camera used
    private void onCreate() {
        userInterface = new MoveScreenUI(getGame(), getPlayer(), getMapScreen());
        tutorials = new MoveTutorials(getGame(), userInterface);
        stage = userInterface.getStage();
        // Setting the background texture and camera
        spikeTexture = new Texture(Gdx.files.internal("spikenew.png"));

        spikeX = getGame().gameWidth/2 + spikeTexture.getWidth()/100f/2;
        spikeY = spikeTexture.getHeight()/100f - 1.6f;
        spikeWidth = spikeTexture.getWidth()/100f;
        spikeHeight = spikeTexture.getHeight()/100f;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        getMapScreen().saveStepsOnPause();
        userInterface.createUI();

        if(DungeonEscape.tutorials && Save.getShowJumpTutorial()) {
            tutorials.createJumpTutorial();
            Save.saveShowJumpTutorial(false);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debug();

        // World camera
        getGame().batch.setProjectionMatrix(getGame().getGameCamera().combined);

        getGame().batch.begin();

        getGame().batch.draw(getBackgroundTexture(), 0, 0, getGame().gameWidth, getGame().gameHeight);
        getGame().batch.draw(spikeTexture, spikeX, spikeY, spikeWidth, spikeHeight);
        getPlayer().draw(getGame().batch);

        // Font camera
        // getGame().batch.setProjectionMatrix(getGame().getScreenCamera().combined);

        getGame().batch.end();

        update();

        doPhysicsStep(delta);

        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();
        userInterface.updateCounterLabel();
    }

    /**
     * Updates jump screen status.
     *
     * <p>
     * Calls jump method from {@link fi.tamk.rentogames.Move.MoveScreenPlayer} to check if player jumped in real life.
     * Switches screen back to map screen when move screen's player is out of boundaries of the screen.
     * </p>
     *
     */
    private void update() {
        // Player jumping and checking user input
        getPlayer().playerJump();
        //getPlayer().checkInput();

        // When player sprite moves out of boundaries go to map screen
        if(getPlayer().getPlayerY() < -1f) {
            getGame().setScreen(getMapScreen());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        GameAudio.stopMusic("movescreenmusic");
        getMapScreen().setPauseSteps();
    }

    @Override
    // Disposes all from move screen
    public void dispose() {
        super.dispose();
        spikeTexture.dispose();
    }
}

