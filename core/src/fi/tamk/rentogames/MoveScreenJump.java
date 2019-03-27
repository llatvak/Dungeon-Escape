package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MoveScreenJump extends MoveScreenMove implements Screen {

    private Texture spikeTexture;

    // Spike attributes
    private float spikeX;
    private float spikeY;
    private float spikeWidth;
    private float spikeHeight;

    MoveScreenJump(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreates();
    }

    // Sets up the world for box2D and camera used
    private void onCreates() {
        // Setting the background texture and camera
        spikeTexture = new Texture(Gdx.files.internal("floorspikes.png"));

        spikeX = getGame().gameWidth/2 + spikeTexture.getWidth()/100f/2;
        spikeY = spikeTexture.getHeight()/100f/6;
        spikeWidth = spikeTexture.getWidth()/100f;
        spikeHeight = spikeTexture.getHeight()/100f;
    }

    @Override
    public void show() {
        getMapScreen().saveSteps();
        getGame().setMoveScreenStatus(true);
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
        getGame().batch.setProjectionMatrix(getGame().getScreenCamera().combined);
        getFontRoboto().draw(getGame().batch, getGame().getMyBundle().get("jumptext"),80 , getGame().screenHeight - 50f);
        getFontRoboto().draw(getGame().batch, getGame().getMyBundle().get("jumpcount") + ": " + getPlayer().getCountedJumps(), 120, getGame().screenHeight - 100f);

        getGame().batch.end();

        update();

        doPhysicsStep(delta);
    }

    private void update() {
        // Player jumping and checking user input
        getPlayer().playerJump();
        getPlayer().checkInput();

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

