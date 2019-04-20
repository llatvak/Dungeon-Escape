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
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveScreenSquat extends MoveScreenMove implements Screen {

    private MoveScreenUI userInterface;
    private MoveTutorials tutorials;
    private Stage stage;

    // Arrow trap
    private Texture arrowTexture;
    private Rectangle arrowRect;

    private boolean squatInitialized = true;
    private boolean runInitialized = true;

    /**
     * @param game
     * @param mapScreen
     */
    public MoveScreenSquat(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreate();
    }

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

    /**
     *
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        getMapScreen().saveStepsOnPause();
        getGame().setMoveScreenStatus(true);
        userInterface.createUI();

        if(DungeonEscape.tutorials && getGame().isSquatTutorials()) {
            tutorials.createSquatTutorial();
            getGame().setSquatTutorials(false);
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
