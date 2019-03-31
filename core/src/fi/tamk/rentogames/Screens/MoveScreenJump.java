package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Interface.MoveScreenUI;
import fi.tamk.rentogames.Move.MoveScreenMove;

public class MoveScreenJump extends MoveScreenMove implements Screen {

    private MoveScreenUI userInterface;
    private Stage stage;

    private Texture spikeTexture;
    // Spike attributes
    private float spikeX;
    private float spikeY;
    private float spikeWidth;
    private float spikeHeight;

    public MoveScreenJump(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreates();
    }

    // Sets up the world for box2D and camera used
    private void onCreates() {
        userInterface = new MoveScreenUI(getGame(), getPlayer());
        stage = userInterface.getStage();
        // Setting the background texture and camera
        spikeTexture = new Texture(Gdx.files.internal("floorspikes.png"));

        spikeX = getGame().gameWidth/2 + spikeTexture.getWidth()/100f/2;
        spikeY = spikeTexture.getHeight()/100f/6;
        spikeWidth = spikeTexture.getWidth()/100f;
        spikeHeight = spikeTexture.getHeight()/100f;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        getMapScreen().saveSteps();
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
        getMapScreen().subtractSteps();
        getGame().setMoveScreenStatus(false);
    }

    @Override
    // Disposes all from move screen
    public void dispose() {
        super.dispose();
        spikeTexture.dispose();
    }
}

