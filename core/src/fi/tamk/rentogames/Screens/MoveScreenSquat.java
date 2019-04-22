package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Interface.MoveScreenUI;
import fi.tamk.rentogames.Interface.MoveTutorials;
import fi.tamk.rentogames.Move.MoveScreenMove;

/**
 * Class for squat move screen in game.
 *
 * <p>
 * Extends {@link MoveScreenMove} to inherit all common methods and variables for move screens.
 * This class is used to control the data and actions happened in squat screen.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveScreenSquat extends MoveScreenMove implements Screen {

    /**
     * Used to create user interface for squat screen.
     */
    private MoveScreenUI userInterface;
    /**
     * Used to create tutorials for squat screen.
     */
    private MoveTutorials tutorials;
    /**
     * Used to draw objects from stage in scene2D.
     */
    private Stage stage;

    // Arrow trap
    /**
     * Texture for arrow in jump screen.
     */
    private Texture arrowTexture;
    /**
     * Rectangle object to draw arrow texture on it.
     */
    private Rectangle arrowRect;

    /**
     * Boolean value to check if player character is squatting in squat screen.
     */
    private boolean squatInitialized = true;
    /**
     * Boolean value to check if player character is running in squat screen.
     */
    private boolean runInitialized = true;

    /**
     * Constructor to create squat screen.
     *
     * <p>
     * Receives the game from {@link DungeonEscape} and map screen to switch screen from {@link MapScreen}.
     * Calls {@link MoveScreenMove} to use common methods and variables for move screens in game.
     * Calls create method to create all squat screen specific information.
     * </p>
     *
     * @param game game received from main class to get access to all methods
     * @param mapScreen object to switch screens when squat is done
     */
    public MoveScreenSquat(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreate();
    }

    /**
     * Method to create squat screen.
     *
     * <p>
     * Creates user interface from {@link MoveScreenUI} and tutorials from {@link MoveTutorials}.
     * Sets stage in scene2D for user interface and sets arrow texture and rectangle as well as it's size and position.
     * </p>
     */
    // Sets up the world for box2D and camera used
    private void onCreate() {
        userInterface = new MoveScreenUI(getGame(), getPlayer(), getMapScreen());
        tutorials = new MoveTutorials(getGame(), userInterface);
        stage = userInterface.getStage();
        getGame().batch = getGame().getBatch();

        // Arrow trap in squat screen drawn on rectangle
        arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrowRect = new Rectangle(getGame().gameWidth + arrowTexture.getWidth()/100f, 1.8f, arrowTexture.getWidth()/100f, arrowTexture.getHeight()/100f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        getMapScreen().saveStepsOnPause();
        getGame().setMoveScreenStatus(true);
        userInterface.createUI();

        if(DungeonEscape.tutorials && Save.getShowSquatTutorial()) {
            tutorials.createSquatTutorial();
            Save.saveShowSquatTutorial(false);
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

        //Drawing everything
        getGame().batch.draw(getBackgroundTexture(), 0, 0, getGame().gameWidth, getGame().gameHeight);
        getGame().batch.draw(arrowTexture, arrowRect.getX(), arrowRect.getY(), arrowRect.getWidth(), arrowRect.getHeight());
        getPlayer().draw(getGame().batch);

        // Font camera
        //getGame().batch.setProjectionMatrix(getGame().getScreenCamera().combined);

        getGame().batch.end();

        update();

        doPhysicsStep(delta);

        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();
        userInterface.updateCounterLabel();
    }

    /**
     * Updates squat screen status.
     *
     * <p>
     * Calls squat method from {@link fi.tamk.rentogames.Move.MoveScreenPlayer} to check if player squatted in real life.
     * Moves arrow in screen when enough squats is done by player in real life.
     * When arrow is in specific position sets player character to do squat animation and stay squatted till arrow passes.
     * After that sets player character to run and sets run animation to character.
     * Plays sound when player does squat or when player runs.
     * Switches screen back to map screen when move screen's player is out of boundaries of the screen.
     * </p>
     *
     */
    private void update() {
        // Player jumping and checking user input
        getPlayer().playerSquat();
        //getPlayer().checkInput();

        // Moves the arrow
        if(getPlayer().getCountedJumps() >= getPlayer().getMovesRequired()) {
            arrowRect.setX(arrowRect.getX() - 0.08f);
        }

        if(arrowRect.getX() + arrowRect.getWidth()/100f < getPlayer().getPlayerX() + 2f) {
            getPlayer().playerDodge();
            if(squatInitialized) {
                GameAudio.playSound("jumpsound", Save.getCurrentAudioSetting());
            }
            squatInitialized = false;
        }

        // Player runs when arrow is passed
        if(arrowRect.getX() + arrowRect.getWidth()/100f < getPlayer().getPlayerX() - 2) {
            if(runInitialized) {
                GameAudio.playSound("runsound", Save.getCurrentAudioSetting());
            }
            getPlayer().setPlayerSquat(false);
            getPlayer().playerRun();
            runInitialized = false;
        }

        // When player sprite moves out of boundaries go to map screen
        if(getPlayer().getPlayerX() > 8.5f) {
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
        getGame().setMoveScreenStatus(false);
    }

    @Override
    // Disposes all from move screen
    public void dispose() {
        super.dispose();
        arrowTexture.dispose();
    }
}
