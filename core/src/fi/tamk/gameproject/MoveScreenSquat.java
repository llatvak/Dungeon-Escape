package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MoveScreenSquat extends MoveScreenMove implements Screen {
    private SpriteBatch batch;

    // Testing for arrow
    private Texture arrowTexture;
    private Rectangle arrowRect;

    public MoveScreenSquat(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreates();
    }

    // Sets up the world for box2D and camera used
    public void onCreates() {
        batch = getGame().getBatch();

        // Arrow trap in squat screen drawn on rectangle
        arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrowRect = new Rectangle(WORLD_WIDTH + arrowTexture.getWidth()/100f, 1.7f, arrowTexture.getWidth()/100f, arrowTexture.getHeight()/100f);

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
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //Drawing everything
        batch.draw(getBackgroundTexture(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(arrowTexture, arrowRect.getX(), arrowRect.getY(), arrowRect.getWidth(), arrowRect.getHeight());
        getPlayer().draw(batch);

        // Font camera
        batch.setProjectionMatrix(fontCamera.combined);
        getFontRoboto().draw(batch, "Squat six times!",80 , 640f - 50f);
        getFontRoboto().draw(batch, "Squats: " + getPlayer().getCountedJumps(), 120, 640f - 100f);

        batch.end();

        update();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void update() {
        // Player jumping and checking user input
        getPlayer().playerSquat();
        getPlayer().checkInput();

        // Moves the arrow
        if(getPlayer().getCountedJumps() >= 6) {
            arrowRect.setX(arrowRect.getX() - 0.1f);
        }

        // Player runs when arrow is passed
        if(arrowRect.getX() + arrowRect.getWidth()/100f < getPlayer().getPlayerX()) {
            getPlayer().playerRun();
        }

        // When player sprite moves out of boundaries go to map screen
        if(getPlayer().getPlayerX() > 8.5f) {
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
