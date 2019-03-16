package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class MapScreen implements Screen {

    private final String MAIN_TITLE = "Map screen";
    final float WORLD_WIDTH = 360f/100f;
    final float WORLD_HEIGHT = 640f/100f;

    DungeonEscape game;
    MapPlayer player;
    SpriteBatch batch;
    MoveScreen moveScreen;



    // Camera
    OrthographicCamera camera;
    OrthographicCamera fontCamera;

    // Map
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    // Textures
    Texture background;

    // Fonts
    private Fonts fonts;
    private BitmapFont fontRoboto;


    private final int TILE_STEPS = 5;
    private int stepTotal;
    private int stepsOnPause;
    private int stepDelta;
    int movementPoints = 20;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();

    }

    public void onCreate() {
        batch = game.getBatch();
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        background = new Texture("brickwall.png");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, 360f, 640f);

        player = new MapPlayer(this);

        fonts = new Fonts();
        fonts.createMediumFont();
        fontRoboto = fonts.getFont();

    }

    public void update() {

        stepTotal = game.getStepTotal();
        player.receiveSteps(stepTotal);
        countMovementPoints();
        checkAllowedMoves();

        if(player.moving) {
            player.move();
        }

        player.checkCollisions();
        player.checkInput();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);
        // Render all layers to screen.
        tiledMapRenderer.render();

        // View font camera
        batch.setProjectionMatrix(fontCamera.combined);
        batch.begin();

        //batch.draw(background,0,0, WORLD_WIDTH,WORLD_HEIGHT);

        fontRoboto.draw(batch, "Steps: " + stepTotal, 10 , 640f - 10f);
        fontRoboto.draw(batch, "Moves: " + movementPoints, 200 , 640f - 10f);

        // View game camera
        batch.setProjectionMatrix(camera.combined);

        //player.draw(batch);
        batch.draw(player.getTexture(),
                camera.position.x - player.getTexture().getWidth()/100f/2,
                camera.position.y - player.getTexture().getHeight()/100f/2,
                player.getTexture().getWidth()/100f,
                player.getTexture().getHeight()/100f);
        batch.end();

        moveCamera();
        update();

    }

    public void moveCamera() {
        camera.position.x = player.getX()/100f + 32f/100f;
        camera.position.y = player.getY()/100f + 32f/100f;
        camera.update();
    }

    public void goToDownTrap() {
        System.out.println("DOWN TRAP!");
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }
    public void goToUpTrap() {
        System.out.println("UP TRAP!");
        // Needs new class UpScreen
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }
    public void goToStoryTile() {
        System.out.println("STORY TILE!");
        // Needs new class StoryScreen
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }

    public void countMovementPoints() {
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % TILE_STEPS == 0) {
                addMovementPoint();
                addStep();
            }
        }
    }

    public void addMovementPoint() {
        movementPoints++;
    }

    public void removeMovementPoint() {
        if(movementPoints > 0) {
            movementPoints--;
        }

    }

    public void checkAllowedMoves() {
        if(movementPoints > 0) {
            player.allowMovement = true;
        } else {
            player.allowMovement = false;
        }
    }

    public int countStepDelta() {
        stepDelta = stepTotal - stepsOnPause;
        return stepDelta;
    }

    public void addStep() {
        game.stepTotal++;
    }

    public void subtractStep() {
        game.stepTotal--;
    }

    public TiledMap getWorldMap(){
        return tiledMap;
    }

    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        stepsOnPause = stepTotal;
        System.out.println("on pause "+stepsOnPause);
    }

    @Override
    public void resume() {
        System.out.println("total "+ stepTotal);
        System.out.println("delta " + countStepDelta());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        fontRoboto.dispose();
        player.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
