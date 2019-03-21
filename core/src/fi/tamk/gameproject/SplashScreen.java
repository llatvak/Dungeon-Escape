package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    private final DungeonEscape app;
    private Stage stage;

    // Images
    private Image rentoImg;
    private Image tamkImg;
    private Image tikoImg;

    //Test to count switching screen (does not reflect the actual runtime ATM)!
    private float stateTime = 0;

    public SplashScreen(final DungeonEscape app) {
        // Using DungeonEscape class camera to set viewport to stage
        this.app = app;
        this.stage = new Stage(new StretchViewport(app.screenResolutionWidth, app.screenResolutionHeight, app.camera));

        // Rento texture to img
        Texture rentoTex = new Texture(Gdx.files.internal("rentologo.png"));
        rentoImg = new Image(rentoTex);

        // Tamk texture to img
        Texture tamkTex = new Texture(Gdx.files.internal("tamklogo.png"));
        tamkImg = new Image(tamkTex);

        Texture tikoTex = new Texture(Gdx.files.internal("tikologo.jpg"));
        tikoImg = new Image(tikoTex);
    }

    @Override
    public void show() {
        // Setting sizes
        rentoImg.setSize(120,100);
        tamkImg.setSize(120, 100);
        tikoImg.setSize(120, 100);

        // Centering images
        rentoImg.setOrigin(rentoImg.getWidth() / 2, rentoImg.getHeight() / 2);
        tamkImg.setOrigin(tamkImg.getWidth() / 2, rentoImg. getHeight() / 2);
        tikoImg.setOrigin(tikoImg.getWidth() / 2, tikoImg.getHeight() / 2);

        // Setting position of the images
        rentoImg.setPosition(stage.getWidth() / 2 - 60, stage.getHeight() + 100);
        tamkImg.setPosition(stage.getWidth() / 2 - 60, stage.getHeight()  - 100);
        tikoImg.setPosition(stage.getWidth() / 2 - 60, stage.getHeight() + 200);

        // Adding effects to images
        rentoImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 60, stage.getHeight() / 2 - 100, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        tamkImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 60, stage.getHeight() / 2 - 250, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        tikoImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f, 1f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 60, stage.getHeight() / 2 + 50, 2f, Interpolation.swing)),
                delay(0.5f),  fadeOut(0.5f)));

        // Adding all actors(images) to stage
        stage.addActor(rentoImg);
        stage.addActor(tamkImg);
        stage.addActor(tikoImg);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        app.batch.begin();
        app.batch.end();

    }

    public void update(float delta) {
        stage.act(delta);
        stateTime += delta;

        // if game has run for X time switch screen to setScreen
        if(stateTime >= 3.6) {
            app.changeScreen(1);
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
    }
}
