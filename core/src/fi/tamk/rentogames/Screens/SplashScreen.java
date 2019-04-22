package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fi.tamk.rentogames.DungeonEscape;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Class to create and control effects in splash screen.
 *
 * <p>
 * Creates splash screen using scene2D stage and sets all textures to the stage.
 * Draws the stage and adds effect to them.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class SplashScreen implements Screen {

    /**
     * Game received from {@link DungeonEscape} to get access to all methods.
     */
    private final DungeonEscape game;
    /**
     * Used to draw objects from stage in scene2D.
     */
    private Stage stage;

    // Images
    /**
     * Image of rento games team logo.
     */
    private Image rentoImg;
    /**
     * Image of Tampere University of Technology's logo.
     */
    private Image tamkImg;
    /**
     * Image of Business Information Systems of TAMK logo.
     */
    private Image tikoImg;
    /**
     * Image of What Makes Me Wanna Move -project logo.
     */
    private Image msmlImg;

    //Test to count switching screen (does not reflect the actual runtime ATM)!
    /**
     * State time to count when to switch screens.
     */
    private float stateTime = 0;

    /**
     * Constructor to create splash screen.
     *
     * <p>
     * Sets the game received from main class and using that game sets the viewport for screen.
     * Creates all textures and makes and image of them.
     * </p>
     *
     * @param game game received from {@link DungeonEscape} to get access to all methods
     */
    public SplashScreen(final DungeonEscape game) {
        // Using DungeonEscape class camera to set viewport to stage
        this.game = game;
        this.stage = new Stage(game.getGameViewport());

        // Rento texture to img
        Texture rentoTex = new Texture(Gdx.files.internal("rentologo.png"));
        rentoImg = new Image(rentoTex);

        // Tamk texture to img
        Texture tamkTex = new Texture(Gdx.files.internal("tamklogo.png"));
        tamkImg = new Image(tamkTex);

        Texture tikoTex = new Texture(Gdx.files.internal("tikologo.png"));
        tikoImg = new Image(tikoTex);

        Texture msmlTex = new Texture(Gdx.files.internal("msmllogo.jpg"));
        msmlImg = new Image(msmlTex);
    }

    @Override
    public void show() {
        // Setting sizes
        rentoImg.setSize(256,160);
        tamkImg.setSize(367, 88);
        tikoImg.setSize(282, 111);
        msmlImg.setSize(297, 183);

        // Centering images
        rentoImg.setOrigin(rentoImg.getWidth() / 2, rentoImg.getHeight() / 2);
        tamkImg.setOrigin(tamkImg.getWidth() / 2, rentoImg. getHeight() / 2);
        tikoImg.setOrigin(tikoImg.getWidth() / 2, tikoImg.getHeight() / 2);
        msmlImg.setOrigin(msmlImg.getWidth() / 2, msmlImg.getHeight() / 2);

        // Setting position of the images
        rentoImg.setPosition(stage.getWidth() / 2 - 128, stage.getHeight() - 100);
        tamkImg.setPosition(stage.getWidth() / 2 - 183.5f, stage.getHeight()  + 200);
        tikoImg.setPosition(stage.getWidth() / 2 - 141, stage.getHeight() + 100);
        msmlImg.setPosition(stage.getWidth() / 2 - 148.5f, stage.getHeight() + 300);

        // Adding effects to images
        rentoImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 128, stage.getHeight() / 2 - 300, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        tamkImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 183.5f, stage.getHeight() / 2 + 60, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        tikoImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 141, stage.getHeight() / 2 - 120, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        msmlImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 148.5f, stage.getHeight() / 2 + 150, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        // Adding all actors(images) to stage
        stage.addActor(rentoImg);
        stage.addActor(tikoImg);
        stage.addActor(msmlImg);
        stage.addActor(tamkImg);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        game.batch.begin();
        game.batch.end();

    }

    /**
     * Updates splash screen to act and swap screens when effects in splash screen are done.
     *
     * @param delta delta time added to state time to count time passed
     */
    private void update(float delta) {
        stage.act(delta);
        stateTime += delta;

        // if game has run for X time switch screen to setScreen
        if(stateTime >= 3.6) {
            game.changeScreen(1);
        }
    }

    @Override
    public void resize(int width, int height) {
        // Updating viewport when resizing
        stage.getViewport().update(width, height);
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
    public void dispose() {
        stage.dispose();
        game.dispose();
    }
}
