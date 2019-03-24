package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MoveScreenJump extends MoveScreenMove implements Screen {

    private SpriteBatch batch;

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
    public void onCreates() {
        batch = getGame().getBatch();

        // Setting the background texture and camera
        spikeTexture = new Texture(Gdx.files.internal("floorspikes.png"));

        spikeX = WORLD_WIDTH/2 + spikeTexture.getWidth()/100f/2;
        spikeY = spikeTexture.getHeight()/100f/6;
        spikeWidth = spikeTexture.getWidth()/100f;
        spikeHeight = spikeTexture.getHeight()/100f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debug();

        // World camera
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(getBackgroundTexture(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(spikeTexture, spikeX, spikeY, spikeWidth, spikeHeight);
        getPlayer().draw(batch);

        // Font camera
        batch.setProjectionMatrix(fontCamera.combined);
        getFontRoboto().draw(batch, "Jump six times!",80 , 640f - 50f);
        getFontRoboto().draw(batch, "Jumps: " + getPlayer().getCountedJumps(), 120, 640f - 100f);

        batch.end();

        update();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void update() {
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    // Disposes all from move screen
    public void dispose() {
        super.dispose();
        spikeTexture.dispose();
    }
}

