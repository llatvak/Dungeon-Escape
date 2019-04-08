package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Interface.MoveScreenUI;
import fi.tamk.rentogames.Move.MoveScreenMove;

public class MoveScreenSquat extends MoveScreenMove implements Screen {

    private MoveScreenUI userInterface;
    private Stage stage;

    // Arrow trap
    private Texture arrowTexture;
    private Rectangle arrowRect;

    public MoveScreenSquat(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreate();
    }

    // Sets up the world for box2D and camera used
    private void onCreate() {
        userInterface = new MoveScreenUI(getGame(), getPlayer(), getMapScreen());
        stage = userInterface.getStage();
        getGame().batch = getGame().getBatch();

        // Arrow trap in squat screen drawn on rectangle
        arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrowRect = new Rectangle(getGame().gameWidth + arrowTexture.getWidth()/100f, 1.7f, arrowTexture.getWidth()/100f, arrowTexture.getHeight()/100f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        getMapScreen().saveStepsOnPause();
        getGame().setMoveScreenStatus(true);
        userInterface.createUI();
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

        System.out.println("Nuolen sijainti lisätty: " + arrowRect.getX() + arrowRect.getWidth()/100f);
        System.out.println("Pelaajan sijainti: " + getPlayer().getPlayerX());

        // Moves the arrow
        if(getPlayer().getCountedJumps() >= getPlayer().getMovesRequired()) {
            arrowRect.setX(arrowRect.getX() - 0.1f);
        }

        if(arrowRect.getX() + arrowRect.getWidth()/100f < getPlayer().getPlayerX() + 4) {
            getPlayer().playerDodge();
        }

        // Player runs when arrow is passed
        if(arrowRect.getX() + arrowRect.getWidth()/100f < getPlayer().getPlayerX() - 1) {
            getPlayer().setPlayerSquat(false);
            getPlayer().playerRun();
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
        getMapScreen().subtractSteps();
        getGame().setMoveScreenStatus(false);
    }

    @Override
    // Disposes all from move screen
    public void dispose() {
        super.dispose();
        arrowTexture.dispose();
    }
}
