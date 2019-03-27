package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class MoveScreenSquat extends MoveScreenMove implements Screen {
    // Arrow trap
    private Texture arrowTexture;
    private Rectangle arrowRect;

    MoveScreenSquat(DungeonEscape game, MapScreen mapScreen) {
        super(game, mapScreen);
        onCreates();
    }

    // Sets up the world for box2D and camera used
    private void onCreates() {
        getGame().batch = getGame().getBatch();

        // Arrow trap in squat screen drawn on rectangle
        arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrowRect = new Rectangle(getGame().gameWidth + arrowTexture.getWidth()/100f, 1.7f, arrowTexture.getWidth()/100f, arrowTexture.getHeight()/100f);
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

        //Drawing everything
        getGame().batch.draw(getBackgroundTexture(), 0, 0, getGame().gameWidth, getGame().gameHeight);
        getGame().batch.draw(arrowTexture, arrowRect.getX(), arrowRect.getY(), arrowRect.getWidth(), arrowRect.getHeight());
        getPlayer().draw(getGame().batch);

        // Font camera
        getGame().batch.setProjectionMatrix(getGame().getScreenCamera().combined);
        getFontRoboto().draw(getGame().batch, getGame().getMyBundle().get("squattext"),80 , getGame().screenHeight - 50f);
        getFontRoboto().draw(getGame().batch, getGame().getMyBundle().get("squatcount") + ": " + getPlayer().getCountedJumps(), 120, getGame().screenHeight - 100f);

        getGame().batch.end();

        update();

        doPhysicsStep(delta);
    }

    private void update() {
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
